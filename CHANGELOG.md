# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

[0.2.0] - 2025-11-23

### Added

- PML validator to validate workflow files before execution (#40)
- New `run` command option for executing workflows with flags: `--workflow`, `--delete-on-completion`, `--delete-on-success-completion`, `--retrieve-models`, `--retrieve-repositories`, `--show-logs`, `--polling-interval` (#51)
- Timeout support for workflows with configurable timeout values (#53)
- Fallback support for workflows to handle timeout scenarios (#53)
- New churrera recipe for continuous diagrams workflow (#103)
- Sonar configuration iteration 2 (#148)

### Changed

- Updated to PML 0.3.0 specification (#40)
- Client behavior: PR creation is now conditional and not automatic in all cases (#43)
- UML diagrams updated for clarity and accuracy (#81, #156)

[0.2.0]: https://github.com/jabrena/churrera/compare/0.1.0...0.2.0
