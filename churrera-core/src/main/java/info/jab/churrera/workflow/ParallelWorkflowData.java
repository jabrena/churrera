package info.jab.churrera.workflow;

import java.util.ArrayList;
import java.util.List;

/**
 * Data class containing information about a parallel workflow.
 */
public class ParallelWorkflowData {
    private final PromptInfo parallelPrompt;
    private final String bindResultType;
    private final List<SequenceInfo> sequences;
    private final Long timeoutMillis;
    private final String fallbackSrc;

    public ParallelWorkflowData(PromptInfo parallelPrompt, String bindResultType, List<SequenceInfo> sequences, Long timeoutMillis, String fallbackSrc) {
        this.parallelPrompt = parallelPrompt;
        this.bindResultType = bindResultType;
        this.sequences = new ArrayList<>(sequences);
        this.timeoutMillis = timeoutMillis;
        this.fallbackSrc = fallbackSrc;
    }

    public PromptInfo getParallelPrompt() {
        return parallelPrompt;
    }

    public String getBindResultType() {
        return bindResultType;
    }

    public List<SequenceInfo> getSequences() {
        return new ArrayList<>(sequences);
    }

    public boolean hasBindResultType() {
        return bindResultType != null && !bindResultType.trim().isEmpty();
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

