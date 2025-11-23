package info.jab.churrera.cli;

import info.jab.churrera.cli.command.run.RunCommand;
import info.jab.churrera.cli.repository.JobRepository;
import info.jab.churrera.workflow.WorkflowParser;
import info.jab.churrera.cli.service.JobProcessor;
import info.jab.churrera.cli.service.CLIAgent;
import info.jab.churrera.util.CursorApiKeyResolver;
import info.jab.churrera.util.PropertyResolver;
import info.jab.churrera.util.PmlConverter;
import info.jab.churrera.workflow.WorkflowValidator;
import info.jab.churrera.workflow.PmlValidator;
import info.jab.cursor.client.impl.CursorAgentManagementImpl;
import info.jab.cursor.client.impl.CursorAgentInformationImpl;
import info.jab.cursor.client.impl.CursorAgentGeneralEndpointsImpl;
import info.jab.cursor.generated.client.ApiClient;
import info.jab.cursor.generated.client.api.DefaultApi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import picocli.CommandLine;

import info.jab.churrera.cli.util.GitInfo;

import java.io.IOException;
import java.util.function.Supplier;

/**
 * Main CLI application for churrera.
 * Root command that provides subcommands for managing jobs stored in BaseX.
 */
@CommandLine.Command(
    name = "churrera",
    mixinStandardHelpOptions = true,
    usageHelpAutoWidth = true
)
public class ChurreraCLI implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(ChurreraCLI.class);

    // Dependencies
    private final CursorApiKeyResolver apiKeyResolver;
    final PropertyResolver propertyResolver;
    final JobRepository jobRepository;
    private final ApiClient apiClient;
    private final DefaultApi defaultApi;
    final CLIAgent cliAgent;
    private final WorkflowParser workflowParser;
    final JobProcessor jobProcessor;
    final WorkflowValidator workflowValidator;
    final PmlValidator pmlValidator;
    private final String apiKey;

    /**
     * Default constructor that initializes all dependencies.
     */
    public ChurreraCLI() throws IOException {
        // Validate API key at startup
        this.apiKeyResolver = new CursorApiKeyResolver();
        this.apiKey = apiKeyResolver.resolveApiKey();
        logger.debug("CURSOR_API_KEY validated");

        this.propertyResolver = new PropertyResolver();
        this.jobRepository = new JobRepository(propertyResolver);
        logger.debug("JobRepository initialized");

        // Create CLIAgent with dependencies
        String apiBaseUrl = "https://api.cursor.com";
        this.apiClient = new ApiClient();
        this.apiClient.updateBaseUri(apiBaseUrl);
        this.defaultApi = new DefaultApi(apiClient);
        this.cliAgent = new CLIAgent(
            jobRepository,
            new CursorAgentManagementImpl(apiKey, defaultApi),
            new CursorAgentInformationImpl(apiKey, defaultApi),
            new CursorAgentGeneralEndpointsImpl(apiKey, defaultApi),
            new PmlConverter()
        );

        // Create WorkflowParser
        this.workflowParser = new WorkflowParser();

        this.jobProcessor = new JobProcessor(jobRepository, cliAgent, workflowParser);

        // Create validators
        this.workflowValidator = new WorkflowValidator();
        this.pmlValidator = new PmlValidator();
    }

    /**
     * Constructor for testing that accepts all dependencies.
     */
    ChurreraCLI(
            CursorApiKeyResolver apiKeyResolver,
            String apiKey,
            PropertyResolver propertyResolver,
            JobRepository jobRepository,
            ApiClient apiClient,
            DefaultApi defaultApi,
            CLIAgent cliAgent,
            WorkflowParser workflowParser,
            JobProcessor jobProcessor,
            WorkflowValidator workflowValidator,
            PmlValidator pmlValidator) {
        this.apiKeyResolver = apiKeyResolver;
        this.apiKey = apiKey;
        this.propertyResolver = propertyResolver;
        this.jobRepository = jobRepository;
        this.apiClient = apiClient;
        this.defaultApi = defaultApi;
        this.cliAgent = cliAgent;
        this.workflowParser = workflowParser;
        this.jobProcessor = jobProcessor;
        this.workflowValidator = workflowValidator;
        this.pmlValidator = pmlValidator;
    }


    /**
     * Creates and initializes the Run command with all required dependencies.
     */
    RunCommand createRunCmd() {
        logger.debug("JobRepository initialized");

        // Read polling interval from properties
        int pollingIntervalSeconds = propertyResolver.getProperty("application.properties", "cli.polling.interval.seconds")
                .map(Integer::parseInt)
                .orElseThrow(() -> new RuntimeException("Required property 'cli.polling.interval.seconds' not found in application.properties"));

        return new RunCommand(jobRepository, jobProcessor, workflowValidator, workflowParser, pmlValidator, pollingIntervalSeconds, cliAgent);
    }

    @Override
    public void run() {
        // Root command without subcommand - show custom message
        logger.info("Please specify a command. Use 'churrera --help' for available commands.");
    }

    /**
     * Main entry point for the application.
     *
     * @param args command line arguments
     */
    public static void main(String[] args) {
        // Print banner first in all cases
        printBanner(GitInfo::new);

        try {
            // Create ChurreraCLI instance with dependencies initialized
            final ChurreraCLI cli = new ChurreraCLI();

            // Create CommandLine with root command
            CommandLine commandLine = new CommandLine(cli);

            // Create and register subcommands manually
            final RunCommand runCommand = cli.createRunCmd();

            commandLine.addSubcommand("run", runCommand);

            // Add shutdown hook to ensure proper cleanup
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                logger.trace("Shutdown hook triggered");
                // Cleanup for RunCommand
                if (runCommand != null && runCommand.getJobRepository() != null) {
                    runCommand.getJobRepository().close();
                }
            }));

            int exitCode = commandLine.execute(args);

            System.exit(exitCode);

        } catch (Exception e) {
            logger.error("Failed to start Churrera CLI: {}", e.getMessage(), e);
            System.exit(1);
        }
    }

    /**
     * Prints the application banner.
     * Package-private method for testing with injected GitInfo supplier.
     *
     * @param gitInfoSupplier Supplier for creating GitInfo instance
     */
    static void printBanner(Supplier<GitInfo> gitInfoSupplier) {
        try {
            logger.info("Running Churrera");
            gitInfoSupplier.get().print();
        } catch (RuntimeException e) {
            logger.error("Error printing banner: {}", e.getMessage(), e);
        }
    }

}
