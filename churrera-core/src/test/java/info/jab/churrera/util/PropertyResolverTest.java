package info.jab.churrera.util;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

/**
 * Test class for PropertyResolver utility.
 */
@DisplayName("PropertyResolver Tests")
class PropertyResolverTest {

    @Nested
    @DisplayName("getProperty Tests")
    class GetPropertyTests {

        @Test
        @DisplayName("Should get string property")
        void shouldGetStringProperty() {
            // Given
            PropertyResolver propertyResolver = new PropertyResolver();

            // When
            Optional<String> modelProperty =
                propertyResolver.getProperty("application.properties", "model");

            // Then
            assertThat(modelProperty).isPresent();
            assertThat(modelProperty.get()).isNotNull().isNotEmpty();
        }

        @Test
        @DisplayName("Should get numeric property as string")
        void shouldGetNumericPropertyAsString() {
            // Given
            PropertyResolver propertyResolver = new PropertyResolver();

            // When
            Optional<String> delayProperty =
                propertyResolver.getProperty("application.properties", "delay");

            // Then
            assertThat(delayProperty).isPresent();
            assertThat(delayProperty.get()).isNotNull();
            assertThatCode(() -> Integer.parseInt(delayProperty.get())).doesNotThrowAnyException();
        }

        @Test
        @DisplayName("Should return empty Optional for non-existent property")
        void shouldReturnEmptyOptionalForNonExistentProperty() {
            // Given
            PropertyResolver propertyResolver = new PropertyResolver();

            // When
            Optional<String> nonExistentProperty =
                propertyResolver.getProperty("application.properties", "nonExistent");

            // Then
            assertThat(nonExistentProperty).isEmpty();
        }

        @Test
        @DisplayName("Should return empty Optional for null resource path")
        void shouldReturnEmptyOptionalForNullResourcePath() {
            // Given
            PropertyResolver propertyResolver = new PropertyResolver();

            // When
            Optional<String> result =
                propertyResolver.getProperty(null, "model");

            // Then
            assertThat(result).isEmpty();
        }

        @Test
        @DisplayName("Should return empty Optional for null key")
        void shouldReturnEmptyOptionalForNullKey() {
            // Given
            PropertyResolver propertyResolver = new PropertyResolver();

            // When
            Optional<String> result =
                propertyResolver.getProperty("application.properties", null);

            // Then
            assertThat(result).isEmpty();
        }

        @Test
        @DisplayName("Should return empty Optional for empty resource path")
        void shouldReturnEmptyOptionalForEmptyResourcePath() {
            // Given
            PropertyResolver propertyResolver = new PropertyResolver();

            // When
            Optional<String> result =
                propertyResolver.getProperty("", "model");

            // Then
            assertThat(result).isEmpty();
        }

        @Test
        @DisplayName("Should return empty Optional for empty key")
        void shouldReturnEmptyOptionalForEmptyKey() {
            // Given
            PropertyResolver propertyResolver = new PropertyResolver();

            // When
            Optional<String> result =
                propertyResolver.getProperty("application.properties", "");

            // Then
            assertThat(result).isEmpty();
        }

        @Test
        @DisplayName("Should return empty Optional for non-existent resource")
        void shouldReturnEmptyOptionalForNonExistentResource() {
            // Given
            PropertyResolver propertyResolver = new PropertyResolver();

            // When
            Optional<String> result =
                propertyResolver.getProperty("non-existent.properties", "key");

            // Then
            assertThat(result).isEmpty();
        }
    }
}
