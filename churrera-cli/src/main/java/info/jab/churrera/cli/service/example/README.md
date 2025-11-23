# Factory Pattern Example for Dependency Injection

This directory contains example implementations demonstrating how to refactor the codebase to use a factory pattern for dependency injection.

## Problem

The current codebase has two main issues:

1. **ChurreraCLI manually constructs dependencies** - The default constructor creates all dependencies inline, making it hard to test and maintain.

2. **JobProcessor creates services internally** - Instead of receiving services via constructor, JobProcessor creates them internally, violating dependency injection principles.

## Solution: Factory Pattern

The factory pattern provides a clean way to:
- Centralize dependency creation logic
- Make dependencies explicit and testable
- Separate object creation from business logic
- Easier migration to a DI framework later

## Files

### 1. `JobProcessorRefactored.java`
Shows how `JobProcessor` should be refactored to receive all services via constructor instead of creating them internally.

**Key Changes:**
- Constructor now accepts: `WorkflowFileService`, `SequenceWorkflowHandler`, `ParallelWorkflowHandler`, `ChildWorkflowHandler`
- Removed internal service creation logic
- All dependencies are injected

### 2. `JobProcessorFactory.java`
Factory class that creates `JobProcessor` with all dependencies properly wired.

**Features:**
- `create()` method: Creates the full dependency graph
- `createWithHandlers()` method: Allows injecting pre-created handlers (useful for testing)
- Clear dependency creation order (services → handlers → processor)

**Dependency Graph:**
```
JobProcessor
├── JobRepository
├── WorkflowFileService
│   └── WorkflowParser
├── SequenceWorkflowHandler
│   ├── JobRepository
│   ├── CLIAgent
│   ├── AgentLauncher
│   ├── PromptProcessor
│   ├── TimeoutManager
│   └── FallbackExecutor
├── ParallelWorkflowHandler
│   ├── JobRepository
│   ├── CLIAgent
│   ├── AgentLauncher
│   ├── TimeoutManager
│   ├── FallbackExecutor
│   └── ResultExtractor
└── ChildWorkflowHandler
    ├── JobRepository
    ├── CLIAgent
    ├── AgentLauncher
    ├── PromptProcessor
    ├── TimeoutManager
    └── FallbackExecutor
```

### 3. `ChurreraCLIFactory.java`
Factory class that creates `ChurreraCLI` with all dependencies properly wired.

**Features:**
- `create()` method: Creates the full dependency graph from scratch
- `createWithDependencies()` method: Allows injecting pre-created dependencies (useful for testing)
- Uses `JobProcessorFactory` to create `JobProcessor`

**Dependency Graph:**
```
ChurreraCLI
├── CursorApiKeyResolver
├── PropertyResolver
├── JobRepository
│   └── PropertyResolver
├── ApiClient
├── DefaultApi
│   └── ApiClient
├── CLIAgent
│   ├── JobRepository
│   ├── CursorAgentManagementImpl
│   ├── CursorAgentInformationImpl
│   ├── CursorAgentGeneralEndpointsImpl
│   └── PmlConverter
├── WorkflowParser
├── JobProcessor (via JobProcessorFactory)
│   └── [see JobProcessorFactory dependency graph]
├── WorkflowValidator
└── PmlValidator
```

## Usage Example

### Before (Current Implementation)
```java
// ChurreraCLI constructor creates everything manually
ChurreraCLI cli = new ChurreraCLI();

// JobProcessor constructor creates services internally
JobProcessor processor = new JobProcessor(jobRepository, cliAgent, workflowParser);
```

### After (Factory Pattern)
```java
// Using factory to create ChurreraCLI
ChurreraCLI cli = ChurreraCLIFactory.create();

// Or create JobProcessor separately
JobProcessor processor = JobProcessorFactory.create(jobRepository, cliAgent, workflowParser);
```

### Testing Example
```java
// Easy to inject mocks for testing
JobRepository mockRepository = mock(JobRepository.class);
CLIAgent mockAgent = mock(CLIAgent.class);
WorkflowParser mockParser = mock(WorkflowParser.class);

JobProcessor processor = JobProcessorFactory.create(
    mockRepository, 
    mockAgent, 
    mockParser
);
```

## Benefits

1. **Testability**: Easy to inject mocks and test individual components
2. **Maintainability**: Dependency creation logic is centralized
3. **Clarity**: Dependency relationships are explicit
4. **Flexibility**: Can easily swap implementations
5. **Migration Path**: Easy to migrate to a DI framework (Dagger, Guice) later

## Migration Steps

1. Refactor `JobProcessor` to accept services via constructor (see `JobProcessorRefactored.java`)
2. Create `JobProcessorFactory` to wire dependencies
3. Refactor `ChurreraCLI` to use `ChurreraCLIFactory` or accept dependencies via constructor
4. Update all call sites to use factories
5. (Optional) Later migrate to a DI framework like Dagger or Guice

## Next Steps

If you want to migrate to a full DI framework:

- **Dagger**: Annotation-based, compile-time DI
- **Guice**: Runtime DI with bindings
- **Spring**: Full framework with many features

The factory pattern provides a good stepping stone and can coexist with DI frameworks.
