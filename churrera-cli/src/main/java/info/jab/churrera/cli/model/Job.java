package info.jab.churrera.cli.model;

import info.jab.churrera.agent.AgentState;
import info.jab.churrera.workflow.WorkflowType;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Represents a job in the churrera system.
 * Each job has a unique ID, path, timestamps, and cursor agent management.
 */
public record Job(
    String jobId,
    String path,
    String cursorAgentId,
    String model,
    String repository,
    AgentState status,
    LocalDateTime createdAt,
    LocalDateTime lastUpdate,
    String parentJobId,
    String result,
    WorkflowType type,
    Long timeoutMillis,
    LocalDateTime workflowStartTime,
    String fallbackSrc,
    Boolean fallbackExecuted
) {

    /**
     * Creates a new job with the given parameters.
     *
     * @param jobId the unique job identifier (UUID)
     * @param path the file path for the job
     * @param cursorAgentId the Cursor agent ID (can be null if not launched yet)
     * @param model the AI model identifier
     * @param repository the repository name/path
     * @param status the current status of the job
     * @param createdAt when the job was created
     * @param lastUpdate when the job was last updated
     * @param parentJobId the parent job ID (null for top-level jobs)
     * @param result the result from parallel jobs (null for non-parallel jobs)
     * @param type the workflow type (SEQUENCE or PARALLEL, can be null for legacy jobs)
     * @param timeoutMillis the timeout in milliseconds (null if not specified)
     * @param workflowStartTime when the workflow execution started (null if timeout not specified)
     * @param fallbackSrc the fallback source file path (null if not specified)
     * @param fallbackExecuted whether the fallback prompt has already been executed (null or false if not executed)
     */
    public Job {
        Objects.requireNonNull(jobId, "Job ID cannot be null");
        Objects.requireNonNull(path, "Path cannot be null");
        Objects.requireNonNull(model, "Model cannot be null");
        Objects.requireNonNull(repository, "Repository cannot be null");
        Objects.requireNonNull(status, "Status cannot be null");
        Objects.requireNonNull(createdAt, "Created at cannot be null");
        Objects.requireNonNull(lastUpdate, "Last update cannot be null");
        // parentJobId, result, type, timeoutMillis, workflowStartTime, fallbackSrc, and fallbackExecuted can be null
    }

    /**
     * Creates a new job with updated path and lastUpdate timestamp.
     *
     * @param newPath the new path
     * @return a new Job instance with updated path and timestamp
     */
    public Job withPath(String newPath) {
        return new Job(jobId, newPath, cursorAgentId, model, repository, status, createdAt, LocalDateTime.now(), parentJobId, result, type, timeoutMillis, workflowStartTime, fallbackSrc, fallbackExecuted);
    }

    /**
     * Creates a new job with updated cursor agent ID and lastUpdate timestamp.
     *
     * @param newCursorAgentId the new Cursor agent ID
     * @return a new Job instance with updated cursor agent ID and timestamp
     */
    public Job withCursorAgentId(String newCursorAgentId) {
        return new Job(jobId, path, newCursorAgentId, model, repository, status, createdAt, LocalDateTime.now(), parentJobId, result, type, timeoutMillis, workflowStartTime, fallbackSrc, fallbackExecuted);
    }

    /**
     * Creates a new job with updated status and lastUpdate timestamp.
     *
     * @param newStatus the new status
     * @return a new Job instance with updated status and timestamp
     */
    public Job withStatus(AgentState newStatus) {
        return new Job(jobId, path, cursorAgentId, model, repository, newStatus, createdAt, LocalDateTime.now(), parentJobId, result, type, timeoutMillis, workflowStartTime, fallbackSrc, fallbackExecuted);
    }

    /**
     * Creates a new job with updated model and lastUpdate timestamp.
     *
     * @param newModel the new model
     * @return a new Job instance with updated model and timestamp
     */
    public Job withModel(String newModel) {
        return new Job(jobId, path, cursorAgentId, newModel, repository, status, createdAt, LocalDateTime.now(), parentJobId, result, type, timeoutMillis, workflowStartTime, fallbackSrc, fallbackExecuted);
    }

    /**
     * Creates a new job with updated repository and lastUpdate timestamp.
     *
     * @param newRepository the new repository
     * @return a new Job instance with updated repository and timestamp
     */
    public Job withRepository(String newRepository) {
        return new Job(jobId, path, cursorAgentId, model, newRepository, status, createdAt, LocalDateTime.now(), parentJobId, result, type, timeoutMillis, workflowStartTime, fallbackSrc, fallbackExecuted);
    }

    /**
     * Creates a new job with updated parent job ID and lastUpdate timestamp.
     *
     * @param newParentJobId the new parent job ID
     * @return a new Job instance with updated parent job ID and timestamp
     */
    public Job withParentJobId(String newParentJobId) {
        return new Job(jobId, path, cursorAgentId, model, repository, status, createdAt, LocalDateTime.now(), newParentJobId, result, type, timeoutMillis, workflowStartTime, fallbackSrc, fallbackExecuted);
    }

    /**
     * Creates a new job with updated result and lastUpdate timestamp.
     *
     * @param newResult the new result
     * @return a new Job instance with updated result and timestamp
     */
    public Job withResult(String newResult) {
        return new Job(jobId, path, cursorAgentId, model, repository, status, createdAt, LocalDateTime.now(), parentJobId, newResult, type, timeoutMillis, workflowStartTime, fallbackSrc, fallbackExecuted);
    }

    /**
     * Creates a new job with updated type and lastUpdate timestamp.
     *
     * @param newType the new workflow type
     * @return a new Job instance with updated type and timestamp
     */
    public Job withType(WorkflowType newType) {
        return new Job(jobId, path, cursorAgentId, model, repository, status, createdAt, LocalDateTime.now(), parentJobId, result, newType, timeoutMillis, workflowStartTime, fallbackSrc, fallbackExecuted);
    }

    /**
     * Creates a new job with updated timeoutMillis and lastUpdate timestamp.
     *
     * @param newTimeoutMillis the new timeout in milliseconds
     * @return a new Job instance with updated timeoutMillis and timestamp
     */
    public Job withTimeoutMillis(Long newTimeoutMillis) {
        return new Job(jobId, path, cursorAgentId, model, repository, status, createdAt, LocalDateTime.now(), parentJobId, result, type, newTimeoutMillis, workflowStartTime, fallbackSrc, fallbackExecuted);
    }

    /**
     * Creates a new job with updated workflowStartTime and lastUpdate timestamp.
     *
     * @param newWorkflowStartTime the new workflow start time
     * @return a new Job instance with updated workflowStartTime and timestamp
     */
    public Job withWorkflowStartTime(LocalDateTime newWorkflowStartTime) {
        return new Job(jobId, path, cursorAgentId, model, repository, status, createdAt, LocalDateTime.now(), parentJobId, result, type, timeoutMillis, newWorkflowStartTime, fallbackSrc, fallbackExecuted);
    }

    /**
     * Creates a new job with updated fallbackSrc and lastUpdate timestamp.
     *
     * @param newFallbackSrc the new fallback source file path
     * @return a new Job instance with updated fallbackSrc and timestamp
     */
    public Job withFallbackSrc(String newFallbackSrc) {
        return new Job(jobId, path, cursorAgentId, model, repository, status, createdAt, LocalDateTime.now(), parentJobId, result, type, timeoutMillis, workflowStartTime, newFallbackSrc, fallbackExecuted);
    }

    /**
     * Creates a new job with updated fallbackExecuted flag and lastUpdate timestamp.
     *
     * @param newFallbackExecuted whether the fallback has been executed
     * @return a new Job instance with updated fallbackExecuted flag and timestamp
     */
    public Job withFallbackExecuted(Boolean newFallbackExecuted) {
        return new Job(jobId, path, cursorAgentId, model, repository, status, createdAt, LocalDateTime.now(), parentJobId, result, type, timeoutMillis, workflowStartTime, fallbackSrc, newFallbackExecuted);
    }
}
