# Security Policy

## Reporting a Vulnerability

If you believe you've found a security vulnerability in this Docker image or its build/release pipeline, please
report it privately using [GitHub Security Advisories](../../security/advisories/new) rather than opening a public
issue.

Please include:
- The affected image tag(s)
- Steps to reproduce, or a proof of concept
- The potential impact

You should expect an initial response within a few days.

## Vulnerabilities in Spring Cloud Config Server itself

This repository only packages Spring Cloud Config Server; it doesn't patch or modify its code. If the vulnerability
is in Spring Cloud Config Server, Spring Boot, or another upstream dependency rather than in how this image is
built, please also report it to the relevant upstream project (see [Spring's security policy](https://spring.io/security-policy)).
Rebuilding this image against a patched dependency version is tracked the same way as any other dependency update
([Renovate](https://github.com/renovatebot/renovate) opens the PR automatically once a fix is released).

## Supported Versions

Only the most recently published image tags receive updates. Older tags are not patched retroactively - please
upgrade to the latest tag for your JVM/platform combination.
