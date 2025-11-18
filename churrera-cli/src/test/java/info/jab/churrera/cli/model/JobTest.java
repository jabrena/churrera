package info.jab.churrera.cli.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.LocalDateTime;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Tests for Job record.
 */
@DisplayName("Job Model Tests")
class JobTest {

    @Test
    @DisplayName("Should create valid job")
    void shouldCreateValidJob() {
        // Given
        String jobId = "test-job-id";
        String path = "/path/to/workflow.xml";
        String cursorAgentId = "agent-123";
        String model = "gpt-4";
        String repository = "test-repo";
        AgentState status = AgentState.CREATING();
        LocalDateTime createdAt = LocalDateTime.now();
        LocalDateTime lastUpdate = LocalDateTime.now();

        // When
        Job job = new Job(jobId, path, cursorAgentId, model, repository, status, createdAt, lastUpdate, null, null, null, null, null, null, null);

        // Then
        assertThat(job.jobId()).isEqualTo(jobId);
        assertThat(job.path()).isEqualTo(path);
        assertThat(job.cursorAgentId()).isEqualTo(cursorAgentId);
        assertThat(job.model()).isEqualTo(model);
        assertThat(job.repository()).isEqualTo(repository);
        assertThat(job.status()).isEqualTo(status);
        assertThat(job.createdAt()).isEqualTo(createdAt);
        assertThat(job.lastUpdate()).isEqualTo(lastUpdate);
        assertThat(job.parentJobId()).isNull();
        assertThat(job.result()).isNull();
    }

    @Test
    @DisplayName("Should create job with null cursor agent ID")
    void shouldCreateJobWithNullCursorAgentId() {
        // Given
        String jobId = "test-job-id";
        String path = "/path/to/workflow.xml";
        String model = "gpt-4";
        String repository = "test-repo";
        AgentState status = AgentState.CREATING();
        LocalDateTime createdAt = LocalDateTime.now();
        LocalDateTime lastUpdate = LocalDateTime.now();

        // When
        Job job = new Job(jobId, path, null, model, repository, status, createdAt, lastUpdate, null, null, null, null, null, null, null);

        // Then
        assertThat(job.cursorAgentId()).isNull();
        assertThat(job.jobId()).isEqualTo(jobId);
    }

    @Test
    @DisplayName("Should throw exception when job ID is null")
    void shouldThrowExceptionWhenJobIdIsNull() {
        // Given
        String path = "/path/to/workflow.xml";
        String model = "gpt-4";
        String repository = "test-repo";
        AgentState status = AgentState.CREATING();
        LocalDateTime createdAt = LocalDateTime.now();
        LocalDateTime lastUpdate = LocalDateTime.now();

        // When & Then
        assertThatThrownBy(() -> new Job(null, path, null, model, repository, status, createdAt, lastUpdate, null, null, null, null, null, null, null))
            .isInstanceOf(NullPointerException.class)
            .hasMessage("Job ID cannot be null");
    }

    @Test
    @DisplayName("Should throw exception when path is null")
    void shouldThrowExceptionWhenPathIsNull() {
        // Given
        String jobId = "test-job-id";
        String model = "gpt-4";
        String repository = "test-repo";
        AgentState status = AgentState.CREATING();
        LocalDateTime createdAt = LocalDateTime.now();
        LocalDateTime lastUpdate = LocalDateTime.now();

        // When & Then
        assertThatThrownBy(() -> new Job(jobId, null, null, model, repository, status, createdAt, lastUpdate, null, null, null, null, null, null, null))
            .isInstanceOf(NullPointerException.class)
            .hasMessage("Path cannot be null");
    }

    @Test
    @DisplayName("Should throw exception when model is null")
    void shouldThrowExceptionWhenModelIsNull() {
        // Given
        String jobId = "test-job-id";
        String path = "/path/to/workflow.xml";
        String repository = "test-repo";
        AgentState status = AgentState.CREATING();
        LocalDateTime createdAt = LocalDateTime.now();
        LocalDateTime lastUpdate = LocalDateTime.now();

        // When & Then
        assertThatThrownBy(() -> new Job(jobId, path, null, null, repository, status, createdAt, lastUpdate, null, null, null, null, null, null, null))
            .isInstanceOf(NullPointerException.class)
            .hasMessage("Model cannot be null");
    }

    @Test
    @DisplayName("Should throw exception when repository is null")
    void shouldThrowExceptionWhenRepositoryIsNull() {
        // Given
        String jobId = "test-job-id";
        String path = "/path/to/workflow.xml";
        String model = "gpt-4";
        AgentState status = AgentState.CREATING();
        LocalDateTime createdAt = LocalDateTime.now();
        LocalDateTime lastUpdate = LocalDateTime.now();

        // When & Then
        assertThatThrownBy(() -> new Job(jobId, path, null, model, null, status, createdAt, lastUpdate, null, null, null, null, null, null, null))
            .isInstanceOf(NullPointerException.class)
            .hasMessage("Repository cannot be null");
    }

    @Test
    @DisplayName("Should throw exception when status is null")
    void shouldThrowExceptionWhenStatusIsNull() {
        // Given
        String jobId = "test-job-id";
        String path = "/path/to/workflow.xml";
        String model = "gpt-4";
        String repository = "test-repo";
        LocalDateTime createdAt = LocalDateTime.now();
        LocalDateTime lastUpdate = LocalDateTime.now();

        // When & Then
        assertThatThrownBy(() -> new Job(jobId, path, null, model, repository, null, createdAt, lastUpdate, null, null, null, null, null, null, null))
            .isInstanceOf(NullPointerException.class)
            .hasMessage("Status cannot be null");
    }

    @Test
    @DisplayName("Should throw exception when created at is null")
    void shouldThrowExceptionWhenCreatedAtIsNull() {
        // Given
        String jobId = "test-job-id";
        String path = "/path/to/workflow.xml";
        String model = "gpt-4";
        String repository = "test-repo";
        AgentState status = AgentState.CREATING();
        LocalDateTime lastUpdate = LocalDateTime.now();

        // When & Then
        assertThatThrownBy(() -> new Job(jobId, path, null, model, repository, status, null, lastUpdate, null, null, null, null, null, null, null))
            .isInstanceOf(NullPointerException.class)
            .hasMessage("Created at cannot be null");
    }

    @Test
    @DisplayName("Should throw exception when last update is null")
    void shouldThrowExceptionWhenLastUpdateIsNull() {
        // Given
        String jobId = "test-job-id";
        String path = "/path/to/workflow.xml";
        String model = "gpt-4";
        String repository = "test-repo";
        AgentState status = AgentState.CREATING();
        LocalDateTime createdAt = LocalDateTime.now();

        // When & Then
        assertThatThrownBy(() -> new Job(jobId, path, null, model, repository, status, createdAt, null, null, null, null, null, null, null, null))
            .isInstanceOf(NullPointerException.class)
            .hasMessage("Last update cannot be null");
    }

    @Test
    @DisplayName("Should create job with updated path")
    void shouldCreateJobWithUpdatedPath() {
        // Given
        LocalDateTime createdAt = LocalDateTime.now().minusHours(1);
        Job originalJob = new Job(
            "test-job-id",
            "/original/path.xml",
            "agent-123",
            "gpt-4",
            "test-repo",
            AgentState.CREATING(),
            createdAt,
            LocalDateTime.now(),
            null,
            null,
            null,
            null,
            null,
            null
        , null);
        String newPath = "/new/path.xml";

        // When
        Job updatedJob = originalJob.withPath(newPath);

        // Then
        assertThat(updatedJob.path()).isEqualTo(newPath);
        assertThat(updatedJob.jobId()).isEqualTo(originalJob.jobId());
        assertThat(updatedJob.cursorAgentId()).isEqualTo(originalJob.cursorAgentId());
        assertThat(updatedJob.model()).isEqualTo(originalJob.model());
        assertThat(updatedJob.repository()).isEqualTo(originalJob.repository());
        assertThat(updatedJob.status()).isEqualTo(originalJob.status());
        assertThat(updatedJob.createdAt()).isEqualTo(originalJob.createdAt());
        assertThat(updatedJob.lastUpdate()).isAfter(originalJob.lastUpdate());
    }

    @Test
    @DisplayName("Should create job with updated cursor agent ID")
    void shouldCreateJobWithUpdatedCursorAgentId() {
        // Given
        LocalDateTime createdAt = LocalDateTime.now().minusHours(1);
        Job originalJob = new Job(
            "test-job-id",
            "/path/to/workflow.xml",
            null,
            "gpt-4",
            "test-repo",
            AgentState.CREATING(),
            createdAt,
            LocalDateTime.now(),
            null,
            null,
            null,
            null,
            null,
            null
        , null);
        String newAgentId = "agent-456";

        // When
        Job updatedJob = originalJob.withCursorAgentId(newAgentId);

        // Then
        assertThat(updatedJob.cursorAgentId()).isEqualTo(newAgentId);
        assertThat(updatedJob.jobId()).isEqualTo(originalJob.jobId());
        assertThat(updatedJob.path()).isEqualTo(originalJob.path());
        assertThat(updatedJob.model()).isEqualTo(originalJob.model());
        assertThat(updatedJob.repository()).isEqualTo(originalJob.repository());
        assertThat(updatedJob.status()).isEqualTo(originalJob.status());
        assertThat(updatedJob.createdAt()).isEqualTo(originalJob.createdAt());
    }

    @Test
    @DisplayName("Should create job with updated status")
    void shouldCreateJobWithUpdatedStatus() {
        // Given
        LocalDateTime createdAt = LocalDateTime.now().minusHours(1);
        Job originalJob = new Job(
            "test-job-id",
            "/path/to/workflow.xml",
            "agent-123",
            "gpt-4",
            "test-repo",
            AgentState.CREATING(),
            createdAt,
            LocalDateTime.now(),
            null,
            null,
            null,
            null,
            null,
            null
        , null);
        AgentState newStatus = AgentState.RUNNING();

        // When
        Job updatedJob = originalJob.withStatus(newStatus);

        // Then
        assertThat(updatedJob.status()).isEqualTo(newStatus);
        assertThat(updatedJob.jobId()).isEqualTo(originalJob.jobId());
        assertThat(updatedJob.path()).isEqualTo(originalJob.path());
        assertThat(updatedJob.cursorAgentId()).isEqualTo(originalJob.cursorAgentId());
        assertThat(updatedJob.model()).isEqualTo(originalJob.model());
        assertThat(updatedJob.repository()).isEqualTo(originalJob.repository());
        assertThat(updatedJob.createdAt()).isEqualTo(originalJob.createdAt());
    }

    @Test
    @DisplayName("Should create job with updated model")
    void shouldCreateJobWithUpdatedModel() {
        // Given
        LocalDateTime createdAt = LocalDateTime.now().minusHours(1);
        Job originalJob = new Job(
            "test-job-id",
            "/path/to/workflow.xml",
            "agent-123",
            "gpt-4",
            "test-repo",
            AgentState.CREATING(),
            createdAt,
            LocalDateTime.now(),
            null,
            null,
            null,
            null,
            null,
            null
        , null);
        String newModel = "gpt-4-turbo";

        // When
        Job updatedJob = originalJob.withModel(newModel);

        // Then
        assertThat(updatedJob.model()).isEqualTo(newModel);
        assertThat(updatedJob.jobId()).isEqualTo(originalJob.jobId());
        assertThat(updatedJob.path()).isEqualTo(originalJob.path());
        assertThat(updatedJob.cursorAgentId()).isEqualTo(originalJob.cursorAgentId());
        assertThat(updatedJob.repository()).isEqualTo(originalJob.repository());
        assertThat(updatedJob.status()).isEqualTo(originalJob.status());
        assertThat(updatedJob.createdAt()).isEqualTo(originalJob.createdAt());
    }

    @Test
    @DisplayName("Should create job with updated repository")
    void shouldCreateJobWithUpdatedRepository() {
        // Given
        LocalDateTime createdAt = LocalDateTime.now().minusHours(1);
        Job originalJob = new Job(
            "test-job-id",
            "/path/to/workflow.xml",
            "agent-123",
            "gpt-4",
            "test-repo",
            AgentState.CREATING(),
            createdAt,
            LocalDateTime.now(),
            null,
            null,
            null,
            null,
            null,
            null
        , null);
        String newRepository = "new-repo";

        // When
        Job updatedJob = originalJob.withRepository(newRepository);

        // Then
        assertThat(updatedJob.repository()).isEqualTo(newRepository);
        assertThat(updatedJob.jobId()).isEqualTo(originalJob.jobId());
        assertThat(updatedJob.path()).isEqualTo(originalJob.path());
        assertThat(updatedJob.cursorAgentId()).isEqualTo(originalJob.cursorAgentId());
        assertThat(updatedJob.model()).isEqualTo(originalJob.model());
        assertThat(updatedJob.status()).isEqualTo(originalJob.status());
        assertThat(updatedJob.createdAt()).isEqualTo(originalJob.createdAt());
    }

    @Test
    @DisplayName("Should have proper equality")
    void shouldHaveProperEquality() {
        // Given
        LocalDateTime timestamp = LocalDateTime.now();
        Job job1 = new Job("id", "/path", "agent", "model", "repo", AgentState.CREATING(), timestamp, timestamp, null, null, null, null, null, null, null);
        Job job2 = new Job("id", "/path", "agent", "model", "repo", AgentState.CREATING(), timestamp, timestamp, null, null, null, null, null, null, null);
        Job job3 = new Job("id2", "/path", "agent", "model", "repo", AgentState.CREATING(), timestamp, timestamp, null, null, null, null, null, null, null);

        // When & Then
        assertThat(job1).isEqualTo(job2);
        assertThat(job1).isNotEqualTo(job3);
        assertThat(job1.hashCode()).isEqualTo(job2.hashCode());
    }

    @Test
    @DisplayName("Should have proper toString")
    void shouldHaveProperToString() {
        // Given
        LocalDateTime timestamp = LocalDateTime.of(2024, 10, 11, 14, 30);
        Job job = new Job(
            "test-id",
            "/path/to/file.xml",
            "agent-123",
            "gpt-4",
            "test-repo",
            AgentState.CREATING(),
            timestamp,
            timestamp,
            null,
            null,
            null,
            null,
            null,
            null
        , null);

        // When
        String toString = job.toString();

        // Then
        assertThat(toString).contains("test-id");
        assertThat(toString).contains("/path/to/file.xml");
        assertThat(toString).contains("agent-123");
        assertThat(toString).contains("gpt-4");
        assertThat(toString).contains("test-repo");
        assertThat(toString).contains("CREATING");
    }

    @ParameterizedTest(name = "Should create job with {0} state")
    @MethodSource("agentStateProvider")
    @DisplayName("Should create job with all agent states")
    void shouldTestAllAgentStates(AgentState state) {
        // Given
        LocalDateTime timestamp = LocalDateTime.now();

        // When
        Job job = new Job("id", "/path", "agent", "model", "repo", state, timestamp, timestamp, null, null, null, null, null, null, null);

        // Then
        assertThat(job.status()).isEqualTo(state);
    }

    private static Stream<Arguments> agentStateProvider() {
        return Stream.of(
            Arguments.of(AgentState.CREATING()),
            Arguments.of(AgentState.RUNNING()),
            Arguments.of(AgentState.FINISHED()),
            Arguments.of(AgentState.ERROR()),
            Arguments.of(AgentState.EXPIRED())
        );
    }
}

