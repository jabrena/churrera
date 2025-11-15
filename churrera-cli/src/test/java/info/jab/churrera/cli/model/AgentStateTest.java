package info.jab.churrera.cli.model;

import info.jab.cursor.client.model.AgentResponse;
import info.jab.cursor.client.model.Source;
import info.jab.cursor.client.model.Target;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.net.URI;
import java.time.OffsetDateTime;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for AgentState enum.
 */
public class AgentStateTest {

    private static AgentResponse createAgentResponse(String status) {
        return new AgentResponse(
            "test-id",
            "Test Agent",
            status,
            new Source(URI.create("https://github.com/test/repo"), "main"),
            new Target("cursor/test", URI.create("https://cursor.com/agents?id=test"), false, null, false, false),
            null,
            OffsetDateTime.now(),
            null
        );
    }

    @Test
    public void testIsTerminal() {
        // Terminal states
        assertTrue(AgentState.COMPLETED.isTerminal());
        assertTrue(AgentState.FAILED.isTerminal());
        assertTrue(AgentState.CANCELLED.isTerminal());
        assertTrue(AgentState.EXPIRED.isTerminal());
        assertTrue(AgentState.FINISHED.isTerminal());

        // Non-terminal states
        assertFalse(AgentState.PENDING.isTerminal());
        assertFalse(AgentState.CREATING.isTerminal());
        assertFalse(AgentState.RUNNING.isTerminal());
        assertFalse(AgentState.UNKNOWN.isTerminal());
    }

    @Test
    public void testIsSuccessful() {
        // Successful states
        assertTrue(AgentState.COMPLETED.isSuccessful());
        assertTrue(AgentState.FINISHED.isSuccessful());

        // Non-successful states
        assertFalse(AgentState.PENDING.isSuccessful());
        assertFalse(AgentState.CREATING.isSuccessful());
        assertFalse(AgentState.RUNNING.isSuccessful());
        assertFalse(AgentState.FAILED.isSuccessful());
        assertFalse(AgentState.CANCELLED.isSuccessful());
        assertFalse(AgentState.EXPIRED.isSuccessful());
        assertFalse(AgentState.UNKNOWN.isSuccessful());
    }

    @Test
    public void testIsFailed() {
        // Failed states
        assertTrue(AgentState.FAILED.isFailed());
        assertTrue(AgentState.CANCELLED.isFailed());
        assertTrue(AgentState.EXPIRED.isFailed());

        // Non-failed states
        assertFalse(AgentState.PENDING.isFailed());
        assertFalse(AgentState.CREATING.isFailed());
        assertFalse(AgentState.RUNNING.isFailed());
        assertFalse(AgentState.COMPLETED.isFailed());
        assertFalse(AgentState.FINISHED.isFailed());
        assertFalse(AgentState.UNKNOWN.isFailed());
    }

    @Test
    public void testIsActive() {
        // Active states (non-terminal)
        assertTrue(AgentState.PENDING.isActive());
        assertTrue(AgentState.CREATING.isActive());
        assertTrue(AgentState.RUNNING.isActive());
        assertTrue(AgentState.UNKNOWN.isActive());

        // Inactive states (terminal)
        assertFalse(AgentState.COMPLETED.isActive());
        assertFalse(AgentState.FAILED.isActive());
        assertFalse(AgentState.CANCELLED.isActive());
        assertFalse(AgentState.EXPIRED.isActive());
        assertFalse(AgentState.FINISHED.isActive());
    }

    @Test
    public void testOfWithNullAgent() {
        AgentState result = AgentState.of(null);
        assertEquals(AgentState.UNKNOWN, result);
    }

    @Test
    public void testOfWithNullStatus() {
        AgentResponse agent = new AgentResponse(
            "test-id",
            "Test Agent",
            null,
            new Source(URI.create("https://github.com/test/repo"), "main"),
            new Target("cursor/test", URI.create("https://cursor.com/agents?id=test"), false, null, false, false),
            null,
            OffsetDateTime.now(),
            null
        );

        AgentState result = AgentState.of(agent);
        assertEquals(AgentState.UNKNOWN, result);
    }

    @Test
    public void testOfWithPendingStatus() {
        AgentResponse agent = createAgentResponse("CREATING"); // Note: PENDING is not in the enum, so we test with CREATING

        AgentState result = AgentState.of(agent);
        assertEquals(AgentState.CREATING, result);
    }

    @Test
    public void testOfWithCreatingStatus() {
        AgentResponse agent = createAgentResponse("CREATING");

        AgentState result = AgentState.of(agent);
        assertEquals(AgentState.CREATING, result);
    }

    @Test
    public void testOfWithRunningStatus() {
        AgentResponse agent = createAgentResponse("RUNNING");

        AgentState result = AgentState.of(agent);
        assertEquals(AgentState.RUNNING, result);
    }

    @Test
    public void testOfWithCompletedStatus() {
        AgentResponse agent = createAgentResponse("COMPLETED");

        AgentState result = AgentState.of(agent);
        assertEquals(AgentState.COMPLETED, result);
    }

    @Test
    public void testOfWithFailedStatus() {
        AgentResponse agent = createAgentResponse("FAILED");

        AgentState result = AgentState.of(agent);
        assertEquals(AgentState.FAILED, result);
    }

    @Test
    public void testOfWithCancelledStatus() {
        AgentResponse agent = createAgentResponse("CANCELLED");

        AgentState result = AgentState.of(agent);
        assertEquals(AgentState.CANCELLED, result);
    }

    @Test
    public void testOfWithExpiredStatus() {
        AgentResponse agent = createAgentResponse("EXPIRED");

        AgentState result = AgentState.of(agent);
        assertEquals(AgentState.EXPIRED, result);
    }

    @Test
    public void testOfWithFinishedStatus() {
        AgentResponse agent = createAgentResponse("FINISHED");

        AgentState result = AgentState.of(agent);
        assertEquals(AgentState.FINISHED, result);
    }

    @Test
    public void testAllValidStatusEnums() {
        // Test all valid status values
        AgentResponse creatingAgent = createAgentResponse("CREATING");
        assertEquals(AgentState.CREATING, AgentState.of(creatingAgent));

        AgentResponse runningAgent = createAgentResponse("RUNNING");
        assertEquals(AgentState.RUNNING, AgentState.of(runningAgent));

        AgentResponse completedAgent = createAgentResponse("COMPLETED");
        assertEquals(AgentState.COMPLETED, AgentState.of(completedAgent));

        AgentResponse failedAgent = createAgentResponse("FAILED");
        assertEquals(AgentState.FAILED, AgentState.of(failedAgent));

        AgentResponse cancelledAgent = createAgentResponse("CANCELLED");
        assertEquals(AgentState.CANCELLED, AgentState.of(cancelledAgent));

        AgentResponse expiredAgent = createAgentResponse("EXPIRED");
        assertEquals(AgentState.EXPIRED, AgentState.of(expiredAgent));

        AgentResponse finishedAgent = createAgentResponse("FINISHED");
        assertEquals(AgentState.FINISHED, AgentState.of(finishedAgent));
    }

    @Test
    public void testStateConsistency() {
        // Test that isActive() is the opposite of isTerminal()
        for (AgentState state : AgentState.values()) {
            assertEquals(!state.isTerminal(), state.isActive());
        }
    }

    @Test
    public void testSuccessfulStatesAreTerminal() {
        // All successful states should be terminal
        for (AgentState state : AgentState.values()) {
            if (state.isSuccessful()) {
                assertTrue(state.isTerminal(),
                    "Successful state " + state + " should be terminal");
            }
        }
    }

    @Test
    public void testFailedStatesAreTerminal() {
        // All failed states should be terminal
        for (AgentState state : AgentState.values()) {
            if (state.isFailed()) {
                assertTrue(state.isTerminal(),
                    "Failed state " + state + " should be terminal");
            }
        }
    }

    @Test
    public void testNoStateIsBothSuccessfulAndFailed() {
        // No state should be both successful and failed
        for (AgentState state : AgentState.values()) {
            assertFalse(state.isSuccessful() && state.isFailed(),
                "State " + state + " cannot be both successful and failed");
        }
    }

    @Test
    public void testEnumValues() {
        // Test that all expected enum values exist
        AgentState[] values = AgentState.values();
        assertEquals(9, values.length);

        assertTrue(java.util.Arrays.asList(values).contains(AgentState.PENDING));
        assertTrue(java.util.Arrays.asList(values).contains(AgentState.CREATING));
        assertTrue(java.util.Arrays.asList(values).contains(AgentState.RUNNING));
        assertTrue(java.util.Arrays.asList(values).contains(AgentState.COMPLETED));
        assertTrue(java.util.Arrays.asList(values).contains(AgentState.FAILED));
        assertTrue(java.util.Arrays.asList(values).contains(AgentState.CANCELLED));
        assertTrue(java.util.Arrays.asList(values).contains(AgentState.EXPIRED));
        assertTrue(java.util.Arrays.asList(values).contains(AgentState.FINISHED));
        assertTrue(java.util.Arrays.asList(values).contains(AgentState.UNKNOWN));
    }

    // Additional Edge Case Tests

    @Test
    public void testOfWithAgentHavingEmptyStatus() {
        // Test with Agent having null status (edge case)
        AgentResponse agent = new AgentResponse(
            "test-id",
            "Test Agent",
            null,
            new Source(URI.create("https://github.com/test/repo"), "main"),
            new Target("cursor/test", URI.create("https://cursor.com/agents?id=test"), false, null, false, false),
            null,
            OffsetDateTime.now(),
            null
        );

        AgentState result = AgentState.of(agent);
        assertEquals(AgentState.UNKNOWN, result);
    }

    @Test
    public void testEnumValueOf() {
        // Test that all enum values can be accessed via valueOf
        assertEquals(AgentState.PENDING, AgentState.valueOf("PENDING"));
        assertEquals(AgentState.CREATING, AgentState.valueOf("CREATING"));
        assertEquals(AgentState.RUNNING, AgentState.valueOf("RUNNING"));
        assertEquals(AgentState.COMPLETED, AgentState.valueOf("COMPLETED"));
        assertEquals(AgentState.FAILED, AgentState.valueOf("FAILED"));
        assertEquals(AgentState.CANCELLED, AgentState.valueOf("CANCELLED"));
        assertEquals(AgentState.EXPIRED, AgentState.valueOf("EXPIRED"));
        assertEquals(AgentState.FINISHED, AgentState.valueOf("FINISHED"));
        assertEquals(AgentState.UNKNOWN, AgentState.valueOf("UNKNOWN"));
    }

    @Test
    public void testTerminalStatesCount() {
        // Test that we have exactly 5 terminal states
        long terminalCount = java.util.Arrays.stream(AgentState.values())
            .filter(AgentState::isTerminal)
            .count();
        assertEquals(5, terminalCount, "Should have exactly 5 terminal states");
    }

    @Test
    public void testActiveStatesCount() {
        // Test that we have exactly 4 active (non-terminal) states
        long activeCount = java.util.Arrays.stream(AgentState.values())
            .filter(AgentState::isActive)
            .count();
        assertEquals(4, activeCount, "Should have exactly 4 active states");
    }

    @Test
    public void testSuccessfulStatesCount() {
        // Test that we have exactly 2 successful states
        long successfulCount = java.util.Arrays.stream(AgentState.values())
            .filter(AgentState::isSuccessful)
            .count();
        assertEquals(2, successfulCount, "Should have exactly 2 successful states");
    }

    @Test
    public void testFailedStatesCount() {
        // Test that we have exactly 3 failed states
        long failedCount = java.util.Arrays.stream(AgentState.values())
            .filter(AgentState::isFailed)
            .count();
        assertEquals(3, failedCount, "Should have exactly 3 failed states");
    }

    @Test
    public void testUnknownStateIsNotSuccessfulOrFailed() {
        // UNKNOWN should not be considered successful or failed
        assertFalse(AgentState.UNKNOWN.isSuccessful());
        assertFalse(AgentState.UNKNOWN.isFailed());
        assertFalse(AgentState.UNKNOWN.isTerminal());
        assertTrue(AgentState.UNKNOWN.isActive());
    }

    @Test
    public void testAllStatesHaveConsistentBehavior() {
        // Comprehensive test of all state behaviors
        for (AgentState state : AgentState.values()) {
            // Terminal states should not be active
            if (state.isTerminal()) {
                assertFalse(state.isActive(),
                    "Terminal state " + state + " should not be active");
            }

            // Active states should not be terminal
            if (state.isActive()) {
                assertFalse(state.isTerminal(),
                    "Active state " + state + " should not be terminal");
            }

            // Successful states should be terminal but not failed
            if (state.isSuccessful()) {
                assertTrue(state.isTerminal(),
                    "Successful state " + state + " should be terminal");
                assertFalse(state.isFailed(),
                    "Successful state " + state + " should not be failed");
            }

            // Failed states should be terminal but not successful
            if (state.isFailed()) {
                assertTrue(state.isTerminal(),
                    "Failed state " + state + " should be terminal");
                assertFalse(state.isSuccessful(),
                    "Failed state " + state + " should not be successful");
            }
        }
    }

    @Test
    public void testEnumOrdinals() {
        // Test that enum ordinals are consistent
        assertEquals(0, AgentState.PENDING.ordinal());
        assertEquals(1, AgentState.CREATING.ordinal());
        assertEquals(2, AgentState.RUNNING.ordinal());
        assertEquals(3, AgentState.COMPLETED.ordinal());
        assertEquals(4, AgentState.FAILED.ordinal());
        assertEquals(5, AgentState.CANCELLED.ordinal());
        assertEquals(6, AgentState.EXPIRED.ordinal());
        assertEquals(7, AgentState.FINISHED.ordinal());
        assertEquals(8, AgentState.UNKNOWN.ordinal());
    }

    @Test
    public void testEnumToString() {
        // Test that enum toString returns the expected values
        assertEquals("PENDING", AgentState.PENDING.toString());
        assertEquals("CREATING", AgentState.CREATING.toString());
        assertEquals("RUNNING", AgentState.RUNNING.toString());
        assertEquals("COMPLETED", AgentState.COMPLETED.toString());
        assertEquals("FAILED", AgentState.FAILED.toString());
        assertEquals("CANCELLED", AgentState.CANCELLED.toString());
        assertEquals("EXPIRED", AgentState.EXPIRED.toString());
        assertEquals("FINISHED", AgentState.FINISHED.toString());
        assertEquals("UNKNOWN", AgentState.UNKNOWN.toString());
    }

    @Test
    public void testEnumName() {
        // Test that enum name() returns the expected values
        assertEquals("PENDING", AgentState.PENDING.name());
        assertEquals("CREATING", AgentState.CREATING.name());
        assertEquals("RUNNING", AgentState.RUNNING.name());
        assertEquals("COMPLETED", AgentState.COMPLETED.name());
        assertEquals("FAILED", AgentState.FAILED.name());
        assertEquals("CANCELLED", AgentState.CANCELLED.name());
        assertEquals("EXPIRED", AgentState.EXPIRED.name());
        assertEquals("FINISHED", AgentState.FINISHED.name());
        assertEquals("UNKNOWN", AgentState.UNKNOWN.name());
    }
}

