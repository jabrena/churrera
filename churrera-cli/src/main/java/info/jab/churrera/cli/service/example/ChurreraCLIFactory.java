package info.jab.churrera.cli.service.example;

import info.jab.churrera.cli.ChurreraCLI;
import info.jab.churrera.cli.repository.JobRepository;
import info.jab.churrera.cli.service.CLIAgent;
import info.jab.churrera.cli.service.JobProcessor;
import info.jab.churrera.util.CursorApiKeyResolver;
import info.jab.churrera.util.PropertyResolver;
import info.jab.churrera.util.PmlConverter;
import info.jab.churrera.workflow.WorkflowParser;
import info.jab.churrera.workflow.WorkflowValidator;
import info.jab.churrera.workflow.PmlValidator;
import info.jab.cursor.client.impl.CursorAgentManagementImpl;
import info.jab.cursor.client.impl.CursorAgentInformationImpl;
import info.jab.cursor.client.impl.CursorAgentGeneralEndpointsImpl;
import info.jab.cursor.generated.client.ApiClient;
import info.jab.cursor.generated.client.api.DefaultApi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Factory for creating ChurreraCLI with all its dependencies properly wired.
 * 
 * This factory encapsulates the complex dependency graph creation logic for ChurreraCLI,
 * replacing the manual construction in the default constructor.
 * 
 * Benefits:
 * - Centralized dependency creation logic
 * - Easier to test (can inject mocks)
 * - Clear separation of concerns
 * - Can be replaced with a DI framework (Dagger, Guice) later
 * - Makes dependency relationships explicit
 */
public class ChurreraCLIFactory {

    private static final Logger logger = LoggerFactory.getLogger(ChurreraCLIFactory.class);

    private static final String API_BASE_URL = "https://api.cursor.com";

    /**
     * Creates a ChurreraCLI instance with all dependencies properly wired.
     * 
     * This method creates the dependency graph:
     * 1. Infrastructure: CursorApiKeyResolver, PropertyResolver
     * 2. Repository: JobRepository
     * 3. API Client: ApiClient, DefaultApi, Cursor client implementations
     * 4. Services: CLIAgent, WorkflowParser, JobProcessor (via JobProcessorFactory)
     * 5. Validators: WorkflowValidator, PmlValidator
     * 6. ChurreraCLI: receives all dependencies
     * 
     * @return a fully configured ChurreraCLI instance
     * @throws IOException if API key resolution or property loading fails
     */
    public static ChurreraCLI create() throws IOException {
        logger.debug("Creating ChurreraCLI with dependencies");

        // Step 1: Create infrastructure components
        CursorApiKeyResolver apiKeyResolver = new CursorApiKeyResolver();
        String apiKey = apiKeyResolver.resolveApiKey();
        logger.debug("CURSOR_API_KEY validated");

        PropertyResolver propertyResolver = new PropertyResolver();

        // Step 2: Create repository
        JobRepository jobRepository = new JobRepository(propertyResolver);
        logger.debug("JobRepository initialized");

        // Step 3: Create API client components
        ApiClient apiClient = new ApiClient();
        apiClient.updateBaseUri(API_BASE_URL);
        DefaultApi defaultApi = new DefaultApi(apiClient);

        // Step 4: Create Cursor client implementations
        CursorAgentManagementImpl agentManagement = new CursorAgentManagementImpl(apiKey, defaultApi);
        CursorAgentInformationImpl agentInformation = new CursorAgentInformationImpl(apiKey, defaultApi);
        CursorAgentGeneralEndpointsImpl agentGeneralEndpoints = new CursorAgentGeneralEndpointsImpl(apiKey, defaultApi);

        // Step 5: Create CLIAgent
        CLIAgent cliAgent = new CLIAgent(
            jobRepository,
            agentManagement,
            agentInformation,
            agentGeneralEndpoints,
            new PmlConverter()
        );

        // Step 6: Create WorkflowParser
        WorkflowParser workflowParser = new WorkflowParser();

        // Step 7: Create JobProcessor using factory (with all its dependencies)
        // Note: In a real implementation, you would refactor JobProcessor to match JobProcessorRefactored
        // and update JobProcessorFactory to return JobProcessor instead of JobProcessorRefactored.
        // For now, we create it the old way to match the existing ChurreraCLI constructor signature.
        JobProcessor jobProcessor = new JobProcessor(jobRepository, cliAgent, workflowParser);

        // Step 8: Create validators
        WorkflowValidator workflowValidator = new WorkflowValidator();
        PmlValidator pmlValidator = new PmlValidator();

        // Step 9: Create ChurreraCLI with all dependencies injected
        // Note: This uses the package-private constructor for testing
        // In a real implementation, you'd modify ChurreraCLI to accept these dependencies
        return new ChurreraCLI(
            apiKeyResolver,
            apiKey,
            propertyResolver,
            jobRepository,
            apiClient,
            defaultApi,
            cliAgent,
            workflowParser,
            jobProcessor,
            workflowValidator,
            pmlValidator
        );
    }

    /**
     * Creates a ChurreraCLI instance with pre-created dependencies.
     * Useful for testing or when you want to inject custom implementations.
     * 
     * @param apiKeyResolver the API key resolver
     * @param apiKey the API key
     * @param propertyResolver the property resolver
     * @param jobRepository the job repository
     * @param apiClient the API client
     * @param defaultApi the default API
     * @param cliAgent the CLI agent
     * @param workflowParser the workflow parser
     * @param jobProcessor the job processor
     * @param workflowValidator the workflow validator
     * @param pmlValidator the PML validator
     * @return a fully configured ChurreraCLI instance
     */
    public static ChurreraCLI createWithDependencies(
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
        
        return new ChurreraCLI(
            apiKeyResolver,
            apiKey,
            propertyResolver,
            jobRepository,
            apiClient,
            defaultApi,
            cliAgent,
            workflowParser,
            jobProcessor,
            workflowValidator,
            pmlValidator
        );
    }

    /**
     * Alternative factory method that uses the refactored JobProcessorFactory.
     * This demonstrates how it would work after refactoring JobProcessor to match JobProcessorRefactored.
     * 
     * Note: This method won't compile until JobProcessor is refactored to accept services via constructor.
     * 
     * @return a fully configured ChurreraCLI instance
     * @throws IOException if API key resolution or property loading fails
     */
    /*
    public static ChurreraCLI createRefactored() throws IOException {
        logger.debug("Creating ChurreraCLI with refactored dependencies");

        // Step 1-6: Same as create() method above
        CursorApiKeyResolver apiKeyResolver = new CursorApiKeyResolver();
        String apiKey = apiKeyResolver.resolveApiKey();
        PropertyResolver propertyResolver = new PropertyResolver();
        JobRepository jobRepository = new JobRepository(propertyResolver);
        
        ApiClient apiClient = new ApiClient();
        apiClient.updateBaseUri(API_BASE_URL);
        DefaultApi defaultApi = new DefaultApi(apiClient);
        
        CursorAgentManagementImpl agentManagement = new CursorAgentManagementImpl(apiKey, defaultApi);
        CursorAgentInformationImpl agentInformation = new CursorAgentInformationImpl(apiKey, defaultApi);
        CursorAgentGeneralEndpointsImpl agentGeneralEndpoints = new CursorAgentGeneralEndpointsImpl(apiKey, defaultApi);
        
        CLIAgent cliAgent = new CLIAgent(
            jobRepository,
            agentManagement,
            agentInformation,
            agentGeneralEndpoints,
            new PmlConverter()
        );
        
        WorkflowParser workflowParser = new WorkflowParser();

        // Step 7: Use JobProcessorFactory to create JobProcessor with all dependencies
        // This would work after refactoring JobProcessor to match JobProcessorRefactored
        JobProcessor jobProcessor = JobProcessorFactory.create(jobRepository, cliAgent, workflowParser);

        WorkflowValidator workflowValidator = new WorkflowValidator();
        PmlValidator pmlValidator = new PmlValidator();

        return new ChurreraCLI(
            apiKeyResolver,
            apiKey,
            propertyResolver,
            jobRepository,
            apiClient,
            defaultApi,
            cliAgent,
            workflowParser,
            jobProcessor,
            workflowValidator,
            pmlValidator
        );
    }
    */
}
