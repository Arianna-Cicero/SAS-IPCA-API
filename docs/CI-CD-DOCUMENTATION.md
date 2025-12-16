# CI/CD Documentation - Complete Reference

Comprehensive guide to the CI/CD pipeline architecture, configuration, and operations.

## Table of Contents

1. [Overview](#overview)
2. [CI Pipeline](#ci-pipeline)
3. [CD Pipeline](#cd-pipeline)
4. [Getting Started](#getting-started)
5. [Docker](#docker)
6. [Build Details](#build-details)
7. [Testing](#testing)
8. [Code Quality](#code-quality)
9. [Security](#security)
10. [Branch Strategy](#branch-strategy)
11. [Versioning](#versioning)
12. [Monitoring](#monitoring)
13. [Troubleshooting](#troubleshooting)
14. [Performance](#performance)
15. [Maintenance](#maintenance)

---

## Overview

This project uses **GitHub Actions** for continuous integration and continuous deployment. The pipeline ensures code quality, security, and reliability through automated testing, code analysis, and deployment stages.

### Pipeline Architecture

**CI Pipeline** (`ci.yml`)
- Runs on every push to main/develop and all PRs
- 4 parallel jobs: build, code-quality, build-artifact, security-scan
- Total time: 8-10 minutes

**CD Pipeline** (`cd.yml`)
- Runs on pushes to main branch and version tags
- 4 sequential jobs: prepare, docker-build, deploy-staging, deploy-production
- Total time: 15-20 minutes

---

## CI Pipeline

### Build & Test Job
**Duration**: 5-8 minutes

Steps:
1. Checkout code with full git history
2. Setup Java 17 (Temurin distribution)
3. Validate Gradle wrapper
4. Build project (excluding tests for speed)
5. Run unit tests (JUnit 5)
6. Generate test reports
7. Upload test artifacts (30 days)

**Outputs**:
- Compiled JAR
- Test reports
- Build logs (on failure)

### Code Quality Job
**Duration**: 2-3 minutes

Steps:
1. Run Detekt static analysis
2. Check code style
3. Enforce naming conventions
4. Upload quality reports

**Tools**:
- Detekt v1.23.5
- 60+ code quality rules
- Custom configuration: `config/detekt/detekt.yml`

**Rules Include**:
- Complexity limits
- Naming conventions
- Style consistency
- Security patterns
- Performance issues

### Build Artifact Job
**Duration**: 2-3 minutes

Creates distribution packages:
- ZIP archive
- TAR archive
- 90-day retention

### Security Scan Job
**Duration**: 3-4 minutes

Steps:
1. OWASP Dependency Check
2. Detect known vulnerabilities
3. Generate security reports
4. Block on high-severity issues

---

## CD Pipeline

### Deploy Preparation Job
**Duration**: Instant

- Determines version from git tags or git describe
- Selects environment (staging for develop, production for tags)

### Build & Push Docker Image Job
**Duration**: 5-10 minutes

Steps:
1. Multi-stage Docker build
2. Push to GitHub Container Registry (GHCR)
3. Implements layer caching
4. Automatic tagging

**Docker Image**:
- Registry: `ghcr.io/<owner>/<repo>`
- Tags: semantic versions, branch names, SHAs

### Deploy to Staging Job
**Duration**: 5 minutes

- Triggers on develop branch or manual trigger
- Auto-deploys to staging environment
- Runs smoke tests
- Sends Slack notifications

### Deploy to Production Job
**Duration**: 5 minutes

- Triggers on version tags only
- Requires manual approval
- Deploys to production
- Runs integration tests
- Creates GitHub release with auto-generated notes
- Sends Slack notifications

---

## Getting Started

### Prerequisites

1. **Java 17+** installed locally
2. **Gradle** (included as wrapper)
3. **Docker & Docker Compose** for local testing
4. **Git** configured
5. **GitHub** account

### Local Development Setup

```bash
# Clone repository
git clone <repository-url>
cd api

# Copy environment file
cp .env.example .env

# Setup database
docker-compose up -d postgres

# Build project
./gradlew build

# Run tests
./gradlew test

# Run code quality checks
./gradlew detekt

# Run application
./gradlew run
```

### Running with Docker Compose

```bash
# Start all services
docker-compose up

# Stop services
docker-compose down

# View logs
docker-compose logs -f api

# Execute command in container
docker-compose exec api sh
```

---

## Docker

### Dockerfile Structure

**Stage 1: Builder**
- Alpine JDK 17
- Downloads dependencies
- Compiles Kotlin to bytecode

**Stage 2: Runtime**
- Alpine JRE 17 (smaller image)
- Copies compiled artifact
- Non-root user for security
- Health checks configured

### Building Locally

```bash
# Build image
docker build -t loja-social-ipca-api:latest .

# Run standalone
docker run -p 8080:8080 \
  -e JDBC_DATABASE_URL=jdbc:postgresql://host:5432/db \
  -e JDBC_DATABASE_USER=user \
  -e JDBC_DATABASE_PASSWORD=pass \
  loja-social-ipca-api:latest
```

### Docker Compose

```yaml
services:
  postgres:
    image: postgres:16-alpine
    environment: 
      POSTGRES_USER: api_user
      POSTGRES_PASSWORD: secure_password
      POSTGRES_DB: loja_social_ipca
  
  api:
    build: .
    environment:
      JDBC_DATABASE_URL: jdbc:postgresql://postgres:5432/loja_social_ipca
    ports:
      - "8080:8080"
    depends_on:
      postgres:
        condition: service_healthy
```

---

## Build Details

### Build Artifacts

**Location**: `build/distributions/`

- `LojaSocialIPCA_API-<version>.tar`
- `LojaSocialIPCA_API-<version>.zip`

**Contents**:
- Compiled JAR
- Startup scripts (bin/)
- Configuration files
- Dependencies

### Test Reports

**Location**: `build/reports/tests/test/index.html`

Run locally:
```bash
./gradlew test
```

### Code Quality Reports

**Detekt Report**: `build/reports/detekt/detekt.html`

Run locally:
```bash
./gradlew detekt
```

---

## Testing

### Test Framework

- **JUnit 5** for unit testing
- **MockK** for mocking
- **Ktor test host** for integration tests

### Running Tests

```bash
# All tests
./gradlew test

# Specific test class
./gradlew test --tests com.ipca.routes.AuthRoutesTest

# With coverage report
./gradlew test jacocoTestReport
```

### Code Coverage

**Tool**: JaCoCo

**Report**: `build/reports/jacoco/test/html/index.html`

**Goal**: 80%+ coverage

---

## Code Quality

### Detekt Configuration

**Location**: `config/detekt/detekt.yml`

**60+ Rules** covering:
- Complexity (cyclomatic, cognitive)
- Naming conventions
- Code style
- Performance issues
- Security patterns
- Potential bugs

### Limits

- Max cyclomatic complexity: 15
- Max method length: 70 lines
- Max class size: 600 lines
- Max function parameters: 6
- Max line length: 120 characters

---

## Security

### Secrets Management

All secrets stored in **GitHub Secrets**:
- Never committed to repository
- Encrypted at rest
- Only exposed to authorized workflows
- Rotated regularly

### Dependency Scanning

**Tools**:
- OWASP Dependency Check
- Gradle wrapper validation

**Process**:
- Scans on every build
- Detects known vulnerabilities
- Blocks on high-severity issues
- Reports included in artifacts

### Container Security

- Non-root user execution
- Alpine base images (minimal surface area)
- Health checks enabled
- Regular image scanning

---

## Branch Strategy

### Main Branch (`main`)
- Production-ready code
- Protected branch - requires PR reviews
- Triggers: Build, Quality, Security, Docker
- Deployable to production via tags

### Develop Branch (`develop`)
- Integration branch for features
- Pre-production testing
- Triggers: Build, Quality, Security, Staging deployment
- Auto-deploys to staging

### Feature Branches (`feature/*`)
- Individual feature development
- Triggers: Build, Quality, Security
- Must pass all checks before PR merge

---

## Versioning

### Version Numbering

Follows **Semantic Versioning** (SemVer):
- Format: `v<MAJOR>.<MINOR>.<PATCH>`
- Example: `v1.2.3`

### Creating Releases

```bash
# Create version tag
git tag -a v1.2.3 -m "Release version 1.2.3"

# Push tag to trigger CD pipeline
git push origin v1.2.3
```

This automatically:
1. Builds Docker image
2. Pushes to registry
3. Deploys to staging
4. Awaits approval
5. Deploys to production
6. Creates GitHub Release

---

## Monitoring

### Slack Integration

Notifications on:
- ✅ Successful deployments
- ❌ Failed builds
- 🔔 Staging deployments
- 🚀 Production deployments

### GitHub Integration

- PR comments with test results
- Branch protection checks
- Release auto-generation
- Deployment status tracking

---

## Troubleshooting

### Build Failures

1. **Check logs**: GitHub Actions > Workflows > Click failed job
2. **Local reproduction**: 
   ```bash
   ./gradlew clean build --stacktrace
   ```
3. **Gradle cache issues**:
   ```bash
   ./gradlew clean --no-build-cache
   ```

### Docker Build Issues

```bash
# Check Docker daemon
docker ps

# Build with verbose output
docker build -t api:latest . --progress=plain
```

### Database Connection Issues

```bash
# Check PostgreSQL
docker-compose ps

# View logs
docker-compose logs postgres

# Connect to database
docker-compose exec postgres psql -U api_user -d loja_social_ipca
```

---

## Performance

### Gradle Caching

The pipeline uses GitHub Actions cache:
- Maven/Gradle dependencies
- Kotlin compilation cache

This reduces build time by ~60% on subsequent runs.

### Docker Layer Caching

- Leverages GHCR build cache
- Reduces image build time
- Preserves layers between builds

---

## Maintenance

### Updating Dependencies

1. Update versions in `build.gradle.kts`
2. Run `./gradlew dependencies --update-locks`
3. Commit changes
4. PR will trigger full test suite

### Updating Actions

Check for new versions regularly and update in `.github/workflows/` files.

### Rotating Secrets

1. Generate new secret/key
2. Update in GitHub Settings > Secrets
3. Deploy to servers
4. Verify connectivity
5. Remove old secret

---

## Additional Resources

- [GitHub Actions Documentation](https://docs.github.com/en/actions)
- [Gradle Documentation](https://docs.gradle.org)
- [Kotlin/Detekt](https://detekt.dev)
- [Docker Best Practices](https://docs.docker.com/develop/dev-best-practices/)
- [Semantic Versioning](https://semver.org)

---

**Status**: ✅ Complete and Ready  
**Last Updated**: December 15, 2025
