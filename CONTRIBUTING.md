# Contributing to Spring Cloud Config Server

Thank you for your interest in contributing to this project! All contributions are welcome, whether it's a bug report, feature request, documentation improvement, or code change.

## Table of Contents

- [Code of Conduct](#code-of-conduct)
- [How Can I Contribute?](#how-can-i-contribute)
  - [Reporting Bugs](#reporting-bugs)
  - [Suggesting Enhancements](#suggesting-enhancements)
  - [Pull Requests](#pull-requests)
- [Development Setup](#development-setup)
- [Coding Guidelines](#coding-guidelines)
- [Testing](#testing)
- [Commit Messages](#commit-messages)
- [Release Process](#release-process)

## Code of Conduct

This project follows a [Code of Conduct](CODE_OF_CONDUCT.md). By participating, you are expected to uphold this code. Please report unacceptable behavior to the project maintainer.

## How Can I Contribute?

### Reporting Bugs

Before creating bug reports, please check existing issues to avoid duplicates. When creating a bug report, include as much detail as possible:

- **Use a clear and descriptive title**
- **Describe the exact steps to reproduce the problem**
- **Provide specific examples** (commands, configuration, docker-compose files)
- **Describe the behavior you observed and what you expected**
- **Include Docker image version/tag you're using**
- **Include any relevant logs or error messages**
- **Include your environment details** (Docker version, OS, etc.)

### Suggesting Enhancements

Enhancement suggestions are tracked as GitHub issues. When creating an enhancement suggestion:

- **Use a clear and descriptive title**
- **Provide a step-by-step description of the suggested enhancement**
- **Explain why this enhancement would be useful** to most users
- **List some examples of how this enhancement would be used**

### Pull Requests

1. Fork the repository and create your branch from `main`
2. Ensure your changes follow the [coding guidelines](#coding-guidelines)
3. Add or update tests as necessary
4. Ensure the test suite passes locally (see [Testing](#testing))
5. Update documentation if you're changing functionality
6. Submit your pull request with a clear description of the changes

## Development Setup

### Prerequisites

- JDK 17 or 21 (matching the `jdkVersion` in `gradle.properties`)
- Docker (for building and testing images)
- Git

### Building the Project

```bash
# Build the application
./gradlew build

# Build the Docker image
./gradlew bootBuildImage
```

### Project Structure

- `src/main/kotlin` - Main application source code
- `src/test/kotlin` - Integration tests using Testcontainers
- `examples/` - Docker Compose examples for different backends
- `.github/workflows/` - CI/CD pipeline definitions

## Coding Guidelines

- Follow the [Kotlin Coding Conventions](https://kotlinlang.org/docs/coding-conventions.html)
- Use meaningful variable and function names
- Keep functions small and focused
- Write comments only when the intent isn't obvious from the code
- Match the existing code style in the repository

## Testing

Tests use JUnit 5 with Testcontainers for integration testing against various backends.

```bash
# Run all tests
./gradlew test

# Run tests for a specific backend
./gradlew test -PtestFilter=git
./gradlew test -PtestFilter=vault
./gradlew test -PtestFilter=redis
```

Available backend filters:
- `aws-param-store`, `aws-s3`, `aws-secrets-manager`
- `cloud-bus-kafka`, `cloud-bus-rabbitmq`
- `git`, `native`, `vault`, `redis`, `mongodb`
- `postgres`, `mariadb`, `security`, `prometheus`

## Commit Messages

- Use the present tense ("Add feature" not "Added feature")
- Use the imperative mood ("Move cursor to..." not "Moves cursor to...")
- Limit the first line to 72 characters or less
- Reference issues and pull requests liberally after the first line

## Release Process

Releases are managed through GitHub Actions. The version is determined by the Spring Cloud Config version defined in `gradle/libs.versions.toml`. Docker images are automatically built and pushed with appropriate tags on release.

---

Thanks again for contributing!
