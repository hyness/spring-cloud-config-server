---
name: Bug report
about: Report a problem with the Docker image or its configuration
title: ''
labels: bug
assignees: ''

---

**Describe the bug**
A clear and concise description of what's wrong.

**To Reproduce**
The `docker run` / `docker-compose` command and any mounted configuration used to reproduce it:
```
docker run -it -p 8888:8888 \
      -v /path/to/config:/config \
      hyness/spring-cloud-config-server:<tag>
```

**Expected behavior**
A clear and concise description of what you expected to happen.

**Logs**
Relevant output from `docker logs` (please redact secrets/credentials).

**Environment**
 - Image tag: [e.g. 5.0.4-jre17]
 - Platform: [e.g. linux/amd64, linux/arm64]
 - Docker version: [e.g. output of `docker version`]
 - Config format: [e.g. application.yml, environment variables, Git-backed]

**Additional context**
Add any other context about the problem here.
