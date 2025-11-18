package info.jab.churrera.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

/**
 * Test class for PropertyResolver utility.
 */
@DisplayName("PropertyResolver Tests")
class PropertyResolverTest {

    private PropertyResolver propertyResolver;

    @BeforeEach
    void setUp() {
        propertyResolver = new PropertyResolver();
    }

    @Nested
    @DisplayName("Successful Property Retrieval Tests")
    class SuccessfulPropertyRetrievalTests {

        @Test
        @DisplayName("Should get string property from existing resource")
        void shouldGetStringPropertyFromExistingResource() {
            // Given
            String resourcePath = "application.properties";
            String key = "model";

            // When
            Optional<String> result = propertyResolver.getProperty(resourcePath, key);

            // Then
            assertThat(result)
                .isPresent()
                .hasValueSatisfying(value -> assertThat(value)
                    .isNotNull()
                    .isNotEmpty());
        }

        @Test
        @DisplayName("Should get numeric property as string")
        void shouldGetNumericPropertyAsString() {
            // Given
            String resourcePath = "application.properties";
            String key = "delay";

            // When
            Optional<String> result = propertyResolver.getProperty(resourcePath, key);

            // Then
            assertThat(result)
                .isPresent()
                .hasValueSatisfying(value -> {
                    assertThat(value).isNotNull();
                    assertThatCode(() -> Integer.parseInt(value))
                        .doesNotThrowAnyException();
                });
        }
    }

    @Nested
    @DisplayName("Invalid Input Tests")
    class InvalidInputTests {

        @ParameterizedTest(name = "Should return empty Optional when resource path is {0}")
        @NullAndEmptySource
        @ValueSource(strings = {"   ", "\t\t", "\n\n"})
        @DisplayName("Should return empty Optional for invalid resource path")
        void shouldReturnEmptyOptionalForInvalidResourcePath(String resourcePath) {
            // Given
            String key = "model";

            // When
            Optional<String> result = propertyResolver.getProperty(resourcePath, key);

            // Then
            assertThat(result).isEmpty();
        }

        @ParameterizedTest(name = "Should return empty Optional when key is {0}")
        @NullAndEmptySource
        @ValueSource(strings = {"   ", "\t\t", "\n\n"})
        @DisplayName("Should return empty Optional for invalid key")
        void shouldReturnEmptyOptionalForInvalidKey(String key) {
            // Given
            String resourcePath = "application.properties";

            // When
            Optional<String> result = propertyResolver.getProperty(resourcePath, key);

            // Then
            assertThat(result).isEmpty();
        }

        @ParameterizedTest(name = "Should return empty Optional when resource path is '{0}' and key is '{1}'")
        @CsvSource({
            "null, model",
            "'', model",
            "'   ', model",
            "application.properties, null",
            "application.properties, ''",
            "application.properties, '   '"
        })
        @DisplayName("Should return empty Optional for invalid resource path and key combinations")
        void shouldReturnEmptyOptionalForInvalidResourcePathAndKeyCombinations(
            String resourcePath, String key) {
            // Given - Convert "null" string to actual null
            String actualResourcePath = "null".equals(resourcePath) ? null : resourcePath;
            String actualKey = "null".equals(key) ? null : key;

            // When
            Optional<String> result = propertyResolver.getProperty(actualResourcePath, actualKey);

            // Then
            assertThat(result).isEmpty();
        }
    }

    @Nested
    @DisplayName("Non-Existent Resource and Property Tests")
    class NonExistentResourceAndPropertyTests {

        @Test
        @DisplayName("Should return empty Optional for non-existent property in existing resource")
        void shouldReturnEmptyOptionalForNonExistentProperty() {
            // Given
            String resourcePath = "application.properties";
            String nonExistentKey = "nonExistent";

            // When
            Optional<String> result = propertyResolver.getProperty(resourcePath, nonExistentKey);

            // Then
            assertThat(result).isEmpty();
        }

        @Test
        @DisplayName("Should return empty Optional for non-existent resource")
        void shouldReturnEmptyOptionalForNonExistentResource() {
            // Given
            String nonExistentResource = "non-existent.properties";
            String key = "key";

            // When
            Optional<String> result = propertyResolver.getProperty(nonExistentResource, key);

            // Then
            assertThat(result).isEmpty();
        }
    }
}
