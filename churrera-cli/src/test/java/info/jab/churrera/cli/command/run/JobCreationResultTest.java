package info.jab.churrera.cli.command.run;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class JobCreationResultTest {

    @Test
    void shouldCreateSuccessfulResultWithEmptyErrors() {
        // When
        JobCreationResult result = JobCreationResult.success("job-123");

        // Then
        assertThat(result.isSuccess()).isTrue();
        assertThat(result.getJobId()).isEqualTo("job-123");
        assertThat(result.getErrors()).isEmpty();
    }

    @Test
    void shouldCreateFailureResultWithErrors() {
        // When
        JobCreationResult result = JobCreationResult.failure(
            java.util.List.of("first", "second")
        );

        // Then
        assertThat(result.isSuccess()).isFalse();
        assertThat(result.getJobId()).isNull();
        assertThat(result.getErrors()).containsExactly("first", "second");
    }
}

