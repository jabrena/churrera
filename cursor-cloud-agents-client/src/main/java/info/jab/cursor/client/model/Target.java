package info.jab.cursor.client.model;

import java.net.URI;
import java.util.Objects;

/**
 * Domain model for agent target configuration.
 */
public record Target(
    String branchName,
    URI url,
    boolean autoCreatePr,
    URI prUrl,
    boolean openAsCursorGithubApp,
    boolean skipReviewerRequest
) {
    public Target {
        Objects.requireNonNull(branchName, "Branch name cannot be null");
        Objects.requireNonNull(url, "URL cannot be null");
    }

    /**
     * Factory method to create Target from generated OpenAPI model.
     *
     * @param generated the generated OpenAPI model
     * @return domain model instance, or null if input is null
     */
    public static Target from(info.jab.cursor.generated.client.model.Target generated) {
        if (generated == null) {
            return null;
        }
        return new Target(
            generated.getBranchName(),
            generated.getUrl(),
            generated.getAutoCreatePr() != null ? generated.getAutoCreatePr() : false,
            generated.getPrUrl(),
            generated.getOpenAsCursorGithubApp() != null ? generated.getOpenAsCursorGithubApp() : false,
            generated.getSkipReviewerRequest() != null ? generated.getSkipReviewerRequest() : false
        );
    }
}

