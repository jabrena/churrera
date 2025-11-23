package info.jab.churrera.cli.service.example;

import info.jab.churrera.cli.repository.JobRepository;
import info.jab.churrera.cli.service.CLIAgent;
import info.jab.churrera.cli.service.WorkflowFileService;
import info.jab.churrera.cli.service.TimeoutManager;
import info.jab.churrera.cli.service.AgentLauncher;
import info.jab.churrera.cli.service.PromptProcessor;
import info.jab.churrera.cli.service.FallbackExecutor;
import info.jab.churrera.cli.service.ResultExtractor;
import info.jab.churrera.cli.service.handler.SequenceWorkflowHandler;
import info.jab.churrera.cli.service.handler.ParallelWorkflowHandler;
import info.jab.churrera.cli.service.handler.ChildWorkflowHandler;
import info.jab.churrera.workflow.WorkflowParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Factory for creating JobProcessor with all its dependencies properly wired.
 * 
 * This factory encapsulates the complex dependency graph creation logic,
 * making it easier to manage and test dependency injection.
 * 
 * Benefits:
 * - Centralized dependency creation logic
 * - Easier to test (can mock dependencies)
 * - Clear separation of concerns
 * - Can be replaced with a DI framework later
 */
public class JobProcessorFactory {

    private static final Logger logger = LoggerFactory.getLogger(JobProcessorFactory.class);

    /**
     * Creates a JobProcessor with all dependencies properly wired.
     * 
     * This method creates the dependency graph:
     * - Services: WorkflowFileService, TimeoutManager, AgentLauncher, PromptProcessor, 
     *             FallbackExecutor, ResultExtractor
     * - Handlers: SequenceWorkflowHandler, ParallelWorkflowHandler, ChildWorkflowHandler
     * - JobProcessor: receives all handlers
     * 
     * @param jobRepository the job repository
     * @param cliAgent the CLI agent
     * @param workflowParser the workflow parser
     * @return a fully configured JobProcessor instance
     */
    public static JobProcessorRefactored create(
            JobRepository jobRepository,
            CLIAgent cliAgent,
            WorkflowParser workflowParser) {
        
        logger.debug("Creating JobProcessor with dependencies");

        // Step 1: Create services (leaf dependencies)
        WorkflowFileService workflowFileService = new WorkflowFileService(workflowParser);
        TimeoutManager timeoutManager = new TimeoutManager(jobRepository);
        
        // Step 2: Create services that depend on other services
        AgentLauncher agentLauncher = new AgentLauncher(cliAgent, jobRepository, workflowFileService);
        PromptProcessor promptProcessor = new PromptProcessor(cliAgent, workflowFileService);
        FallbackExecutor fallbackExecutor = new FallbackExecutor(cliAgent, jobRepository, workflowFileService);
        ResultExtractor resultExtractor = new ResultExtractor(cliAgent, jobRepository);

        // Step 3: Create handlers (depend on services)
        SequenceWorkflowHandler sequenceWorkflowHandler = new SequenceWorkflowHandler(
            jobRepository, 
            cliAgent, 
            agentLauncher, 
            promptProcessor, 
            timeoutManager, 
            fallbackExecutor
        );
        
        ParallelWorkflowHandler parallelWorkflowHandler = new ParallelWorkflowHandler(
            jobRepository, 
            cliAgent, 
            agentLauncher, 
            timeoutManager, 
            fallbackExecutor, 
            resultExtractor
        );
        
        ChildWorkflowHandler childWorkflowHandler = new ChildWorkflowHandler(
            jobRepository, 
            cliAgent, 
            agentLauncher, 
            promptProcessor, 
            timeoutManager, 
            fallbackExecutor
        );

        // Step 4: Create JobProcessor with all handlers injected
        return new JobProcessorRefactored(
            jobRepository,
            workflowFileService,
            sequenceWorkflowHandler,
            parallelWorkflowHandler,
            childWorkflowHandler
        );
    }

    /**
     * Alternative factory method that allows injecting pre-created services.
     * Useful for testing or when you want to reuse service instances.
     * 
     * @param jobRepository the job repository
     * @param workflowFileService the workflow file service (can be shared)
     * @param sequenceWorkflowHandler the sequence workflow handler
     * @param parallelWorkflowHandler the parallel workflow handler
     * @param childWorkflowHandler the child workflow handler
     * @return a fully configured JobProcessor instance
     */
    public static JobProcessorRefactored createWithHandlers(
            JobRepository jobRepository,
            WorkflowFileService workflowFileService,
            SequenceWorkflowHandler sequenceWorkflowHandler,
            ParallelWorkflowHandler parallelWorkflowHandler,
            ChildWorkflowHandler childWorkflowHandler) {
        
        return new JobProcessorRefactored(
            jobRepository,
            workflowFileService,
            sequenceWorkflowHandler,
            parallelWorkflowHandler,
            childWorkflowHandler
        );
    }
}
