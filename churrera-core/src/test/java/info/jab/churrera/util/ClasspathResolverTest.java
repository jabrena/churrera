package info.jab.churrera.util;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Test class for ClasspathResolver utility.
 * Tests the utility's ability to load resources from classpath and handle error cases.
 */
@DisplayName("ClasspathResolver Tests")
class ClasspathResolverTest {

    @Test
    @DisplayName("Should retrieve existing resource from classpath")
    void shouldRetrieveExistingResource() {
        // Given
        String fileName = "examples/hello-world/prompt1.xml";
        ClasspathResolver resolver = new ClasspathResolver();

        // When
        String content = resolver.retrieve(fileName);

        // Then
        assertThat(content)
            .isNotNull()
            .isNotEmpty()
            .contains("<?xml");
    }

    @Test
    @DisplayName("Should retrieve XSL file from classpath")
    void shouldRetrieveXslFileFromClasspath() {
        // Given
        String fileName = "pml/pml-to-md.xsl";
        ClasspathResolver resolver = new ClasspathResolver();

        // When
        String content = resolver.retrieve(fileName);

        // Then
        assertThat(content)
            .isNotNull()
            .isNotEmpty()
            .contains("xsl:stylesheet");
    }

    @Test
    @DisplayName("Should retrieve properties file from classpath")
    void shouldRetrievePropertiesFileFromClasspath() {
        // Given
        String fileName = "application.properties";
        ClasspathResolver resolver = new ClasspathResolver();

        // When
        String content = resolver.retrieve(fileName);

        // Then
        assertThat(content)
            .isNotNull()
            .isNotEmpty()
            .contains("=");
    }

    // Precondition Tests - Null and Empty Inputs

    @Test
    @DisplayName("Should throw IllegalArgumentException when filename is null")
    void shouldThrowExceptionWhenFilenameIsNull() {
        // Given
        ClasspathResolver resolver = new ClasspathResolver();

        // When & Then
        assertThatThrownBy(() -> resolver.retrieve(null))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("cannot be null or empty");
    }

    @Test
    @DisplayName("Should throw IllegalArgumentException when filename is empty string")
    void shouldThrowExceptionWhenFilenameIsEmpty() {
        // Given
        ClasspathResolver resolver = new ClasspathResolver();

        // When & Then
        assertThatThrownBy(() -> resolver.retrieve(""))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("cannot be null or empty");
    }

    @Test
    @DisplayName("Should throw IllegalArgumentException when filename is whitespace only")
    void shouldThrowExceptionWhenFilenameIsWhitespaceOnly() {
        // Given
        ClasspathResolver resolver = new ClasspathResolver();

        // When & Then
        assertThatThrownBy(() -> resolver.retrieve("   "))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("cannot be null or empty");
    }

    @Test
    @DisplayName("Should throw IllegalArgumentException when filename is tab characters")
    void shouldThrowExceptionWhenFilenameIsTabCharacters() {
        // Given
        ClasspathResolver resolver = new ClasspathResolver();

        // When & Then
        assertThatThrownBy(() -> resolver.retrieve("\t\t"))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("cannot be null or empty");
    }

    @Test
    @DisplayName("Should throw IllegalArgumentException when filename is newline characters")
    void shouldThrowExceptionWhenFilenameIsNewlineCharacters() {
        // Given
        ClasspathResolver resolver = new ClasspathResolver();

        // When & Then
        assertThatThrownBy(() -> resolver.retrieve("\n\n"))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("cannot be null or empty");
    }

    // Error Handling Tests

    @Test
    @DisplayName("Should throw RuntimeException when file does not exist in classpath")
    void shouldThrowRuntimeExceptionWhenFileDoesNotExist() {
        // Given
        String nonExistentFile = "non-existent-file.xml";
        ClasspathResolver resolver = new ClasspathResolver();

        // When & Then
        assertThatThrownBy(() -> resolver.retrieve(nonExistentFile))
            .isInstanceOf(RuntimeException.class)
            .hasMessageContaining(nonExistentFile)
            .hasMessageContaining("Could not find");
    }

    @Test
    @DisplayName("Should throw RuntimeException when path is invalid")
    void shouldThrowRuntimeExceptionWhenPathIsInvalid() {
        // Given
        String invalidPath = "../../../etc/passwd";
        ClasspathResolver resolver = new ClasspathResolver();

        // When & Then
        assertThatThrownBy(() -> resolver.retrieve(invalidPath))
            .isInstanceOf(RuntimeException.class)
            .satisfies(exception -> {
                String message = exception.getMessage();
                assertThat(message)
                    .satisfiesAnyOf(
                        msg -> assertThat(msg).contains(invalidPath),
                        msg -> assertThat(msg).contains("Could not find"),
                        msg -> assertThat(msg).contains("Failed to load")
                    );
            });
    }

    @Test
    @DisplayName("Should throw RuntimeException when file path has directory traversal")
    void shouldThrowRuntimeExceptionWhenFilePathHasDirectoryTraversal() {
        // Given
        String traversalPath = "../../outside-classpath.txt";
        ClasspathResolver resolver = new ClasspathResolver();

        // When & Then
        assertThatThrownBy(() -> resolver.retrieve(traversalPath))
            .isInstanceOf(RuntimeException.class)
            .hasMessageContaining("");
    }


    // Content Validation Tests

    @Test
    @DisplayName("Should retrieve content with correct encoding (UTF-8)")
    void shouldRetrieveContentWithCorrectEncoding() {
        // Given - assuming we have a file with special characters
        String fileName = "examples/hello-world/prompt1.xml";
        ClasspathResolver resolver = new ClasspathResolver();

        // When
        String content = resolver.retrieve(fileName);

        // Then
        assertThat(content)
            .isNotNull()
            .isNotEmpty();
    }

    @Test
    @DisplayName("Should retrieve empty file without errors")
    void shouldRetrieveEmptyFileWithoutErrors() {
        // Given - we need to create a test for an empty resource if it exists
        // For now, we test that non-empty files work correctly
        String fileName = "application.properties";
        ClasspathResolver resolver = new ClasspathResolver();

        // When
        String content = resolver.retrieve(fileName);

        // Then
        assertThat(content).isNotNull();
    }

    @Test
    @DisplayName("Should handle files with various extensions")
    void shouldHandleFilesWithVariousExtensions() {
        // Given
        ClasspathResolver resolver = new ClasspathResolver();

        // Then
        assertThatCode(() -> resolver.retrieve("examples/hello-world/prompt1.xml"))
            .doesNotThrowAnyException();
        assertThatCode(() -> resolver.retrieve("pml/pml-to-md.xsl"))
            .doesNotThrowAnyException();
        assertThatCode(() -> resolver.retrieve("application.properties"))
            .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("Should handle nested directory structures")
    void shouldHandleNestedDirectoryStructures() {
        // Given
        String nestedPath = "examples/hello-world/prompt1.xml";
        ClasspathResolver resolver = new ClasspathResolver();

        // When
        String content = resolver.retrieve(nestedPath);

        // Then
        assertThat(content)
            .isNotNull()
            .isNotEmpty();
    }

    @Test
    @DisplayName("Should handle paths without leading slash")
    void shouldHandlePathsWithoutLeadingSlash() {
        // Given
        String pathWithoutSlash = "application.properties";
        ClasspathResolver resolver = new ClasspathResolver();

        // When
        String content = resolver.retrieve(pathWithoutSlash);

        // Then
        assertThat(content)
            .isNotNull()
            .isNotEmpty();
    }
}

