package info.jab.churrera.workflow;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.xml.sax.SAXParseException;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Unit tests for WorkflowValidator.ValidationErrorHandler.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("WorkflowValidator.ValidationErrorHandler Tests")
class WorkflowValidatorValidationErrorHandlerTest {

    private WorkflowValidator.ValidationErrorHandler errorHandler;

    @BeforeEach
    void setUp() {
        errorHandler = new WorkflowValidator.ValidationErrorHandler();
    }

    @Nested
    @DisplayName("Initial State Tests")
    class InitialStateTests {

        @Test
        @DisplayName("Should return false when no errors exist")
        void shouldReturnFalseWhenNoErrorsExist() {
            // When
            Boolean result = errorHandler.hasErrors();

            // Then
            assertThat(result).isFalse();
        }

        @Test
        @DisplayName("Should return empty list when no errors exist")
        void shouldReturnEmptyListWhenNoErrorsExist() {
            // When
            List<String> errors = errorHandler.getErrors();

            // Then
            assertThat(errors).isNotNull().isEmpty();
        }
    }

    @Nested
    @DisplayName("Error Handling Tests")
    class ErrorHandlingTests {

        @Test
        @DisplayName("Should add warning to errors list")
        void shouldAddWarningToErrorsList() {
            // Given
            SAXParseException exception = mock(SAXParseException.class);
            when(exception.getMessage()).thenReturn("Test warning message");
            when(exception.getLineNumber()).thenReturn(10);

            // When
            errorHandler.warning(exception);
            Boolean hasErrors = errorHandler.hasErrors();
            List<String> errors = errorHandler.getErrors();

            // Then
            assertThat(hasErrors).isTrue();
            assertThat(errors)
                .hasSize(1)
                .element(0)
                .asString()
                .contains("Warning:")
                .contains("Test warning message")
                .contains("at line 10");
        }

        @Test
        @DisplayName("Should add error to errors list")
        void shouldAddErrorToErrorsList() {
            // Given
            SAXParseException exception = mock(SAXParseException.class);
            when(exception.getMessage()).thenReturn("Test error message");
            when(exception.getLineNumber()).thenReturn(25);

            // When
            errorHandler.error(exception);
            Boolean hasErrors = errorHandler.hasErrors();
            List<String> errors = errorHandler.getErrors();

            // Then
            assertThat(hasErrors).isTrue();
            assertThat(errors)
                .hasSize(1)
                .element(0)
                .asString()
                .contains("Error:")
                .contains("Test error message")
                .contains("at line 25");
        }

        @Test
        @DisplayName("Should add fatal error to errors list")
        void shouldAddFatalErrorToErrorsList() {
            // Given
            SAXParseException exception = mock(SAXParseException.class);
            when(exception.getMessage()).thenReturn("Test fatal error message");
            when(exception.getLineNumber()).thenReturn(50);

            // When
            errorHandler.fatalError(exception);
            Boolean hasErrors = errorHandler.hasErrors();
            List<String> errors = errorHandler.getErrors();

            // Then
            assertThat(hasErrors).isTrue();
            assertThat(errors)
                .hasSize(1)
                .element(0)
                .asString()
                .contains("Fatal Error:")
                .contains("Test fatal error message")
                .contains("at line 50");
        }
    }

    @Nested
    @DisplayName("Multiple Errors Tests")
    class MultipleErrorsTests {

        @Test
        @DisplayName("Should handle multiple warnings")
        void shouldHandleMultipleWarnings() {
            // Given
            SAXParseException exception1 = mock(SAXParseException.class);
            when(exception1.getMessage()).thenReturn("Warning 1");
            when(exception1.getLineNumber()).thenReturn(5);

            SAXParseException exception2 = mock(SAXParseException.class);
            when(exception2.getMessage()).thenReturn("Warning 2");
            when(exception2.getLineNumber()).thenReturn(10);

            // When
            errorHandler.warning(exception1);
            errorHandler.warning(exception2);
            List<String> errors = errorHandler.getErrors();

            // Then
            assertThat(errors)
                .hasSize(2)
                .element(0)
                .asString()
                .contains("Warning 1")
                .contains("at line 5");
            assertThat(errors.get(1))
                .contains("Warning 2")
                .contains("at line 10");
        }

        @Test
        @DisplayName("Should handle multiple errors")
        void shouldHandleMultipleErrors() {
            // Given
            SAXParseException exception1 = mock(SAXParseException.class);
            when(exception1.getMessage()).thenReturn("Error 1");
            when(exception1.getLineNumber()).thenReturn(15);

            SAXParseException exception2 = mock(SAXParseException.class);
            when(exception2.getMessage()).thenReturn("Error 2");
            when(exception2.getLineNumber()).thenReturn(20);

            // When
            errorHandler.error(exception1);
            errorHandler.error(exception2);
            List<String> errors = errorHandler.getErrors();

            // Then
            assertThat(errors)
                .hasSize(2)
                .element(0)
                .asString()
                .contains("Error:")
                .contains("Error 1")
                .contains("at line 15");
            assertThat(errors.get(1))
                .contains("Error:")
                .contains("Error 2")
                .contains("at line 20");
        }

        @Test
        @DisplayName("Should handle mixed error types")
        void shouldHandleMixedErrorTypes() {
            // Given
            SAXParseException warning = mock(SAXParseException.class);
            when(warning.getMessage()).thenReturn("Warning message");
            when(warning.getLineNumber()).thenReturn(1);

            SAXParseException error = mock(SAXParseException.class);
            when(error.getMessage()).thenReturn("Error message");
            when(error.getLineNumber()).thenReturn(2);

            SAXParseException fatalError = mock(SAXParseException.class);
            when(fatalError.getMessage()).thenReturn("Fatal error message");
            when(fatalError.getLineNumber()).thenReturn(3);

            // When
            errorHandler.warning(warning);
            errorHandler.error(error);
            errorHandler.fatalError(fatalError);
            List<String> errors = errorHandler.getErrors();

            // Then
            assertThat(errors)
                .hasSize(3)
                .element(0)
                .asString()
                .contains("Warning:");
            assertThat(errors.get(1)).contains("Error:");
            assertThat(errors.get(2)).contains("Fatal Error:");
        }
    }

    @Nested
    @DisplayName("Edge Cases Tests")
    class EdgeCasesTests {

        @Test
        @DisplayName("Should return defensive copy of errors list")
        void shouldReturnDefensiveCopyOfErrorsList() {
            // Given
            SAXParseException exception = mock(SAXParseException.class);
            when(exception.getMessage()).thenReturn("Test message");
            when(exception.getLineNumber()).thenReturn(1);
            errorHandler.error(exception);

            // When
            List<String> errors1 = errorHandler.getErrors();
            errors1.add("Should not appear");
            List<String> errors2 = errorHandler.getErrors();

            // Then
            assertThat(errors2)
                .hasSize(1)
                .doesNotContain("Should not appear");
        }

        @Test
        @DisplayName("Should handle error with null message")
        void shouldHandleErrorWithNullMessage() {
            // Given
            SAXParseException exception = mock(SAXParseException.class);
            when(exception.getMessage()).thenReturn(null);
            when(exception.getLineNumber()).thenReturn(5);

            // When
            errorHandler.error(exception);
            List<String> errors = errorHandler.getErrors();

            // Then
            assertThat(errors)
                .hasSize(1)
                .element(0)
                .asString()
                .contains("Error:")
                .contains("at line 5");
        }

        @Test
        @DisplayName("Should handle error with negative line number")
        void shouldHandleErrorWithNegativeLineNumber() {
            // Given
            SAXParseException exception = mock(SAXParseException.class);
            when(exception.getMessage()).thenReturn("Test message");
            when(exception.getLineNumber()).thenReturn(-1);

            // When
            errorHandler.error(exception);
            List<String> errors = errorHandler.getErrors();

            // Then
            assertThat(errors)
                .hasSize(1)
                .element(0)
                .asString()
                .contains("Error:")
                .contains("at line -1");
        }

        @Test
        @DisplayName("Should handle error with zero line number")
        void shouldHandleErrorWithZeroLineNumber() {
            // Given
            SAXParseException exception = mock(SAXParseException.class);
            when(exception.getMessage()).thenReturn("Test message");
            when(exception.getLineNumber()).thenReturn(0);

            // When
            errorHandler.error(exception);
            List<String> errors = errorHandler.getErrors();

            // Then
            assertThat(errors)
                .hasSize(1)
                .element(0)
                .asString()
                .contains("Error:")
                .contains("at line 0");
        }
    }

    @Nested
    @DisplayName("Error Format Tests")
    class ErrorFormatTests {

        @Test
        @DisplayName("Should format error with message and line number")
        void shouldFormatErrorWithMessageAndLineNumber() {
            // Given
            SAXParseException exception = mock(SAXParseException.class);
            when(exception.getMessage()).thenReturn("Custom error message");
            when(exception.getLineNumber()).thenReturn(42);

            // When
            errorHandler.error(exception);
            List<String> errors = errorHandler.getErrors();

            // Then
            assertThat(errors)
                .hasSize(1)
                .element(0)
                .isEqualTo("Error: Custom error message at line 42");
        }

        @Test
        @DisplayName("Should format warning with message and line number")
        void shouldFormatWarningWithMessageAndLineNumber() {
            // Given
            SAXParseException exception = mock(SAXParseException.class);
            when(exception.getMessage()).thenReturn("Custom warning message");
            when(exception.getLineNumber()).thenReturn(100);

            // When
            errorHandler.warning(exception);
            List<String> errors = errorHandler.getErrors();

            // Then
            assertThat(errors)
                .hasSize(1)
                .element(0)
                .isEqualTo("Warning: Custom warning message at line 100");
        }

        @Test
        @DisplayName("Should format fatal error with message and line number")
        void shouldFormatFatalErrorWithMessageAndLineNumber() {
            // Given
            SAXParseException exception = mock(SAXParseException.class);
            when(exception.getMessage()).thenReturn("Custom fatal error message");
            when(exception.getLineNumber()).thenReturn(200);

            // When
            errorHandler.fatalError(exception);
            List<String> errors = errorHandler.getErrors();

            // Then
            assertThat(errors)
                .hasSize(1)
                .element(0)
                .isEqualTo("Fatal Error: Custom fatal error message at line 200");
        }

        @Test
        @DisplayName("Should handle all error types correctly")
        void shouldHandleAllErrorTypesCorrectly() {
            // Given
            SAXParseException warning = mock(SAXParseException.class);
            when(warning.getMessage()).thenReturn("Warning");
            when(warning.getLineNumber()).thenReturn(1);

            SAXParseException error = mock(SAXParseException.class);
            when(error.getMessage()).thenReturn("Error");
            when(error.getLineNumber()).thenReturn(2);

            SAXParseException fatalError = mock(SAXParseException.class);
            when(fatalError.getMessage()).thenReturn("Fatal");
            when(fatalError.getLineNumber()).thenReturn(3);

            // When
            errorHandler.warning(warning);
            errorHandler.error(error);
            errorHandler.fatalError(fatalError);
            Boolean hasErrors = errorHandler.hasErrors();
            List<String> errors = errorHandler.getErrors();

            // Then
            assertThat(hasErrors).isTrue();
            assertThat(errors)
                .hasSize(3)
                .element(0)
                .asString()
                .startsWith("Warning:");
            assertThat(errors.get(1)).startsWith("Error:");
            assertThat(errors.get(2)).startsWith("Fatal Error:");
        }
    }
}
