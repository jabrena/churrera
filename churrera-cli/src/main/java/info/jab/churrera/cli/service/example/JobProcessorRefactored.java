package info.jab.churrera.cli.service.example;

import info.jab.churrera.cli.repository.JobRepository;
import info.jab.churrera.cli.model.Job;
import info.jab.churrera.cli.model.Prompt;
import info.jab.churrera.cli.service.WorkflowFileService;
import info.jab.churrera.cli.service.handler.SequenceWorkflowHandler;
import info.jab.churrera.cli.service.handler.ParallelWorkflowHandler;
import info.jab.churrera.cli.service.handler.ChildWorkflowHandler;
import info.jab.churrera.workflow.WorkflowData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * REFACTORED EXAMPLE: JobProcessor with all services injected via constructor.
 * 
 * This demonstrates how JobProcessor should receive all its dependencies
 * instead of creating them internally.
 * 
 * Compare with the original JobProcessor which creates services in its constructor.
 */
public class JobProcessorRefactored {

    private static final Logger logger = LoggerFactory.getLogger(JobProcessorRefactored.class);

    private final JobRepository jobRepository;
    private final WorkflowFileService workflowFileService;
    private final SequenceWorkflowHandler sequenceWorkflowHandler;
    private final ParallelWorkflowHandler parallelWorkflowHandler;
    private final ChildWorkflowHandler childWorkflowHandler;

    /**
     * Refactored constructor: All services are injected instead of created internally.
     * 
     * @param jobRepository the job repository
     * @param workflowFileService the workflow file service (injected)
     * @param sequenceWorkflowHandler the sequence workflow handler (injected)
     * @param parallelWorkflowHandler the parallel workflow handler (injected)
     * @param childWorkflowHandler the child workflow handler (injected)
     */
    public JobProcessorRefactored(
            JobRepository jobRepository,
            WorkflowFileService workflowFileService,
            SequenceWorkflowHandler sequenceWorkflowHandler,
            ParallelWorkflowHandler parallelWorkflowHandler,
            ChildWorkflowHandler childWorkflowHandler) {
        this.jobRepository = jobRepository;
        this.workflowFileService = workflowFileService;
        this.sequenceWorkflowHandler = sequenceWorkflowHandler;
        this.parallelWorkflowHandler = parallelWorkflowHandler;
        this.childWorkflowHandler = childWorkflowHandler;
    }

    /**
     * Main processing loop that finds and processes unfinished jobs.
     * This method can be called periodically by a ScheduledExecutorService.
     */
    public void processJobs() {
        try {
            List<Job> unfinishedJobs = jobRepository.findUnfinishedJobs();

            if (unfinishedJobs.isEmpty()) {
                logger.debug("No unfinished jobs found");
                return; // No jobs to process
            }

            logger.debug("Found {} unfinished job(s): {}", unfinishedJobs.size(),
                unfinishedJobs.stream().map(Job::jobId).toList());

            for (Job job : unfinishedJobs) {
                processSingleJob(job);
            }
        } catch (Exception e) {
            logger.error("Error finding unfinished jobs: {}", e.getMessage());
        }
    }

    /**
     * Process a single job by launching agents and executing prompts.
     */
    private void processJob(Job job) {
        try {
            logger.debug("Starting to process job: {} (current status: {})", job.jobId(), job.status());

            // Get job details with prompts
            var jobDetailsOpt = jobRepository.findJobWithDetails(job.jobId());
            if (jobDetailsOpt.isEmpty()) {
                logger.error("Job details not found for job: {}", job.jobId());
                return;
            }

            var jobDetails = jobDetailsOpt.get();
            List<Prompt> prompts = jobDetails.getPrompts();

            if (prompts.isEmpty()) {
                logger.error("No prompts found for job: {}", job.jobId());
                return;
            }

            logger.debug("Found {} prompts for job: {} (status before workflow: {})", prompts.size(), job.jobId(), job.status());

            // Process the job workflow
            processJobWorkflow(job, prompts);

            logger.trace("Finished processing job: {}", job.jobId());

        } catch (Exception e) {
            logger.error("Error processing job {}: {}", job.jobId(), e.getMessage(), e);
        }
    }

    /**
     * Process a job workflow by launching it if needed and executing prompts.
     * Routes to the appropriate handler based on workflow type.
     */
    private void processJobWorkflow(Job job, List<Prompt> prompts) {
        try {
            logger.trace("Starting to parse workflow for job: {} (status: {})", job.jobId(), job.status());

            // Parse workflow to get PML files
            WorkflowData workflowData = workflowFileService.parseWorkflow(job.path());
            logger.trace("Workflow parsed successfully for job: {}", job.jobId());

            // Check if this is a child job (from parallel workflow)
            if (job.parentJobId() != null) {
                logger.info("Detected child job: {} (parent: {}, status: {})", job.jobId(), job.parentJobId(), job.status());
                // Child jobs are processed as sequence workflows
                // They inherit the sequence from parent's parallel workflow
                childWorkflowHandler.processWorkflow(job, workflowData, prompts);
                return;
            }

            // Check if this is a parallel workflow
            if (workflowData.isParallelWorkflow()) {
                logger.debug("Detected parallel workflow for job: {} (status: {})", job.jobId(), job.status());
                parallelWorkflowHandler.processWorkflow(job, workflowData);
                return;
            }

            // Standard sequence workflow processing
            sequenceWorkflowHandler.processWorkflow(job, prompts, workflowData);

        } catch (Exception e) {
            logger.error("Error processing job workflow {}: {}", job.jobId(), e.getMessage(), e);
        }
    }

    /**
     * Processes a single job, handling any exceptions.
     *
     * @param job the job to process
     */
    private void processSingleJob(Job job) {
        try {
            logger.info("Processing job: {} (cursorAgentId: {}, status: {})",
                job.jobId(), job.cursorAgentId(), job.status());
            processJob(job);
        } catch (Exception e) {
            logger.error("Error processing job {}: {}", job.jobId(), e.getMessage());
            // Continue with other jobs
        }
    }
}
