package info.jab.churrera.workflow;

import java.util.ArrayList;
import java.util.List;

/**
 * Data class containing information about a sequence within a parallel workflow.
 */
public class SequenceInfo {
    private final String model;
    private final String repository;
    private final List<PromptInfo> prompts;
    private final Long timeoutMillis;
    private final String fallbackSrc;

    public SequenceInfo(String model, String repository, List<PromptInfo> prompts, Long timeoutMillis, String fallbackSrc) {
        this.model = model;
        this.repository = repository;
        this.prompts = new ArrayList<>(prompts);
        this.timeoutMillis = timeoutMillis;
        this.fallbackSrc = fallbackSrc;
    }

    public String getModel() {
        return model;
    }

    public String getRepository() {
        return repository;
    }

    public List<PromptInfo> getPrompts() {
        return new ArrayList<>(prompts);
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

