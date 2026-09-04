# Contributing

Thanks for taking the time to contribute!

This repository packages [Spring Cloud Config Server](https://docs.spring.io/spring-cloud-config/reference/) as a
Docker image. If your issue or idea is about Config Server's own behavior rather than this packaging, it's usually
better raised upstream at [spring-cloud/spring-cloud-config](https://github.com/spring-cloud/spring-cloud-config).

## Reporting bugs / requesting features

Please use the issue templates when opening a new issue - they ask for the information needed to reproduce
problems (image tag, platform, mounted configuration) up front.

## Making changes

1. Fork the repo and create a branch off `main`.
2. Build the image and run the test suite locally:
   ```
   ./gradlew test
   ```
   Individual backend integration tests (each spins up its own testcontainer) can be run one at a time with:
   ```
   ./gradlew test -PtestFilter=<backend>
   ```
   where `<backend>` is one of the tags used in [`ci.yml`](.github/workflows/ci.yml), e.g. `git`, `redis`, `vault`,
   `aws-s3`, `postgres`.
3. Open a pull request against `main` using the PR template. CI builds the image for both `x64` and `arm` and runs
   the full integration test matrix against every supported backend - a green run is required before merge.

## Dependency updates

Dependency bumps are managed by Renovate and open as a single grouped PR on a weekly schedule; you don't need to
bump versions by hand.
