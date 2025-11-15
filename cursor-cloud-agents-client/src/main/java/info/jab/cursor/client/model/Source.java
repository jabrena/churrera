package info.jab.cursor.client.model;

import java.net.URI;
import java.util.Objects;

/**
 * Domain model for agent source configuration.
 */
public record Source(
    URI repository,
    String ref
) {
    public Source {
        Objects.requireNonNull(repository, "Repository cannot be null");
    }

    /**
     * Factory method to create Source from generated OpenAPI model.
     *
     * @param generated the generated OpenAPI model
     * @return domain model instance, or null if input is null
     */
    public static Source from(info.jab.cursor.generated.client.model.Source generated) {
        if (generated == null) {
            return null;
        }
        return new Source(
            generated.getRepository(),
            generated.getRef()
        );
    }
}

