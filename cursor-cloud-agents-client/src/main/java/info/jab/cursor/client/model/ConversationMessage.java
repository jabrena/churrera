package info.jab.cursor.client.model;

import java.util.Objects;

/**
 * Domain model for a conversation message.
 */
public record ConversationMessage(
    String id,
    String type,
    String text
) {
    public ConversationMessage {
        Objects.requireNonNull(id, "ID cannot be null");
        Objects.requireNonNull(type, "Type cannot be null");
        // text can be null
    }

    /**
     * Factory method to create ConversationMessage from generated OpenAPI model.
     *
     * @param generated the generated OpenAPI model
     * @return domain model instance, or null if input is null
     */
    public static ConversationMessage from(info.jab.cursor.generated.client.model.ConversationMessage generated) {
        if (generated == null) {
            return null;
        }
        String type = null;
        if (generated.getType() != null) {
            // ConversationMessage type is a String, not an enum
            type = generated.getType().toString();
        }
        return new ConversationMessage(
            generated.getId(),
            type,
            generated.getText()
        );
    }
}

