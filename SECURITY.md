# Security Policy

## Supported Versions

The following versions of this project are currently supported with security updates:

| Version | Supported          |
| ------- | ------------------ |
| 5.0.x   | :white_check_mark: |
| 4.3.x   | :white_check_mark: |
| < 4.3   | :x:                |

## Reporting a Vulnerability

We take the security of this project seriously. If you discover a security vulnerability, please follow these steps:

1. **DO NOT** open a public GitHub issue
2. Email the maintainer directly at the email associated with their GitHub profile
3. Include the following information:
   - Description of the vulnerability
   - Steps to reproduce the issue
   - Potential impact of the vulnerability
   - Suggested fix (if you have one)

## What to Expect

- **Acknowledgment**: You will receive a response within 48 hours acknowledging your report
- **Updates**: You will be kept informed of progress toward a fix
- **Credit**: You will be credited in the release notes (unless you prefer to remain anonymous)

## Disclosure Policy

- The maintainer will confirm receipt of your vulnerability report
- The maintainer will investigate and validate the vulnerability
- A fix will be developed and tested
- A new release will be published with the fix
- Public disclosure will occur after the fix is available

## Security Best Practices for Users

When running this Docker image in production:

- Always use the latest stable version
- Enable Spring Security profile when exposing the server
- Use strong passwords for any sensitive configurations
- Keep your Docker host and dependencies up to date
- Follow the principle of least privilege when configuring access
- Use TLS/SSL for all communications
- Regularly rotate credentials and secrets
