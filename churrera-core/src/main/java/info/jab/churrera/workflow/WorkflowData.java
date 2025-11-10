package info.jab.churrera.workflow;

import java.util.ArrayList;
import java.util.List;

/**
 * Data class containing parsed workflow information.
 */
public class WorkflowData {
    private final PromptInfo launchPrompt;
    private final String model;
    private final String repository;
    private final List<PromptInfo> updatePrompts;
    private final ParallelWorkflowData parallelWorkflowData;
    private final Long timeoutMillis;
    private final String fallbackSrc;

    public WorkflowData(PromptInfo launchPrompt, String model, String repository, List<PromptInfo> updatePrompts, ParallelWorkflowData parallelWorkflowData, Long timeoutMillis, String fallbackSrc) {
        this.launchPrompt = launchPrompt;
        this.model = model;
        this.repository = repository;
        this.updatePrompts = new ArrayList<>(updatePrompts);
        this.parallelWorkflowData = parallelWorkflowData;
        this.timeoutMillis = timeoutMillis;
        this.fallbackSrc = fallbackSrc;
    }

    /**
     * Returns the launch prompt information.
     *
     * @return the launch prompt info
     */
    public PromptInfo getLaunchPrompt() {
        return launchPrompt;
    }

    /**
     * Returns the model for the sequence.
     *
     * @return the model string (may be null or empty if not specified)
     */
    public String getModel() {
        return model;
    }

    /**
     * Returns the repository for the sequence.
     *
     * @return the repository string (may be null or empty if not specified)
     */
    public String getRepository() {
        return repository;
    }

    /**
     * Returns the list of update prompts.
     *
     * @return list of update prompt info
     */
    public List<PromptInfo> getUpdatePrompts() {
        return new ArrayList<>(updatePrompts);
    }

    /**
     * Returns the parallel workflow data if this is a parallel workflow.
     *
     * @return parallel workflow data, or null if this is not a parallel workflow
     */
    public ParallelWorkflowData getParallelWorkflowData() {
        return parallelWorkflowData;
    }

    /**
     * Returns true if this is a parallel workflow.
     *
     * @return true if parallel workflow, false otherwise
     */
    public boolean isParallelWorkflow() {
        return parallelWorkflowData != null;
    }

    /**
     * Returns the total number of update prompts in the workflow.
     *
     * @return number of update prompts
     */
    public int getUpdateAgentCount() {
        return updatePrompts.size();
    }

    /**
     * Returns true if the workflow has any update prompts.
     *
     * @return true if there are update prompts, false otherwise
     */
    public boolean hasUpdateAgents() {
        return !updatePrompts.isEmpty();
    }

    /**
     * Returns the timeout in milliseconds, or null if not specified.
     *
     * @return timeout in milliseconds, or null
     */
    public Long getTimeoutMillis() {
        return timeoutMillis;
    }

    /**
     * Returns the fallback source file path, or null if not specified.
     *
     * @return fallback source file path, or null
     */
    public String getFallbackSrc() {
        return fallbackSrc;
    }
}

