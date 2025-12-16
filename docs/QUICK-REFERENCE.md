# Quick Reference Guide

One-page reference for common CI/CD tasks and commands.

## Essential Information

**Repository**: LojaSocialIPCA API  
**CI Pipeline**: `.github/workflows/ci.yml`  
**CD Pipeline**: `.github/workflows/cd.yml`  
**Documentation**: `docs/` folder  
**Docker Registry**: `ghcr.io`  
**Java Version**: 17+  
**Kotlin Version**: 1.9.22  

---

## Local Commands

### Build & Test
```bash
./gradlew build              # Full build with tests
./gradlew build -x test      # Build without tests (faster)
./gradlew test               # Run tests only
./gradlew detekt             # Code quality check
./gradlew test jacocoTestReport  # Tests + coverage
./gradlew clean              # Clean all artifacts
```

### Development
```bash
./gradlew run                # Run application (port 8080)
docker-compose up            # Start all services
docker-compose down          # Stop all services
docker-compose logs -f api   # View application logs
```

### Docker
```bash
docker build -t api:latest .                    # Build image
docker run -p 8080:8080 api:latest             # Run container
docker images | grep api                        # List images
docker ps                                       # Running containers
```

---

## Git Workflow

### Create Feature Branch
```bash
git checkout develop
git pull origin develop
git checkout -b feature/description
```

### Commit Code
```bash
git add .
git commit -m "feat: description"              # Feature
git commit -m "fix: description"               # Bug fix
git commit -m "docs: description"              # Documentation
git commit -m "test: description"              # Tests
```

### Push & Create PR
```bash
git push origin feature/description
# Then create PR on GitHub
```

### Merge After Approval
```bash
git checkout develop
git pull origin develop
git merge feature/description
git push origin develop
```

---

## Deployment

### Staging (Automatic)
```bash
# Just merge to develop branch
# CD pipeline auto-deploys
git checkout develop
git pull origin develop
git merge feature/branch
git push origin develop
```

### Production (Manual Approval)
```bash
# Create version tag
git tag -a v1.2.3 -m "Release version 1.2.3"
git push origin v1.2.3

# Then in GitHub:
# 1. Go to Actions tab
# 2. Click running workflow
# 3. Click "Review deployments"
# 4. Select "Approve and deploy"
```

---

## GitHub Actions

### View Workflow Status
1. Go to repository **Actions** tab
2. Click workflow name
3. Check job status

### View Logs
1. Click failed job
2. Expand step to see output
3. Search for error message

### Re-run Workflow
1. Go to workflow run
2. Click "Re-run jobs" or "Re-run all jobs"

---

## Troubleshooting

### Build Fails
```bash
./gradlew clean build --stacktrace
```

### Tests Fail
```bash
./gradlew test --info
./gradlew test --tests ClassName
```

### Code Quality Issues
```bash
./gradlew detekt
# Review build/reports/detekt/detekt.html
```

### Docker Issues
```bash
docker build -t api:latest . --progress=plain
docker-compose logs -f
```

### SSH Connection Failed
- Verify SSH_PRIVATE_KEY secret
- Verify SSH_KNOWN_HOSTS secret
- Check deployment servers are accessible

---

## Secrets Configuration

Required GitHub Secrets:
- `GHCR_TOKEN` - GitHub Container Registry
- `SSH_PRIVATE_KEY` - SSH key for servers
- `SSH_KNOWN_HOSTS` - Known hosts entry
- `DEPLOYMENT_USER` - SSH username
- `STAGING_HOST` - Staging server
- `PRODUCTION_HOST` - Production server
- `SLACK_WEBHOOK_URL` - Slack notifications (optional)

### Add Secret
1. Go to repository **Settings** > **Secrets and variables** > **Actions**
2. Click "New repository secret"
3. Add name and value
4. Click "Add secret"

---

## Environment Variables

Copy `.env.example` to `.env` and update:

```
JDBC_DATABASE_URL=jdbc:postgresql://localhost:5432/loja_social_ipca
JDBC_DATABASE_USER=api_user
JDBC_DATABASE_PASSWORD=secure_password
PORT=8080
JWT_SECRET=your-secret-key
```

---

## Code Quality Limits

**Enforced by Detekt**:
- Cyclomatic complexity: ≤15
- Function length: ≤70 lines
- Class size: ≤600 lines
- Function parameters: ≤6
- Line length: ≤120 characters

---

## Test Coverage

**Targets**:
- Minimum: 60%
- Target: 80%+
- Critical paths: 100%

### Check Coverage
```bash
./gradlew test jacocoTestReport
open build/reports/jacoco/test/html/index.html
```

---

## File Structure

```
.github/workflows/
  ├── ci.yml            CI pipeline
  └── cd.yml            CD pipeline

config/detekt/
  └── detekt.yml        Quality rules

docs/
  ├── 00-START-HERE.md
  ├── CI-CD-DOCUMENTATION.md
  ├── SETUP.md
  └── ... (10+ more)

src/
  ├── main/kotlin/      Source code
  └── test/kotlin/      Tests

Dockerfile              Container definition
docker-compose.yml      Local dev environment
build.gradle.kts        Build configuration
```

---

## CI Pipeline Summary

**Triggers**: Push to main/develop, all PRs  
**Duration**: 8-10 minutes  
**Jobs**: 4 parallel

```
Build (5-8m)              Tests, compile
Code Quality (2-3m)       Detekt analysis
Build Artifact (2-3m)     ZIP/TAR packages
Security Scan (3-4m)      OWASP check
```

---

## CD Pipeline Summary

**Triggers**: Main push (staging), version tags (production)  
**Duration**: 15-20 minutes  
**Jobs**: 4 sequential

```
Prepare (instant)         Version detection
Build Docker (5-10m)      Multi-stage build
Deploy Staging (5m)       Auto-deploy (develop)
Deploy Prod (5m +auth)    Approval-gated (tags)
```

---

## Versioning

**Format**: Semantic Versioning (v1.2.3)

- **v1** = Major (breaking changes)
- **2** = Minor (new features)
- **3** = Patch (bug fixes)

### Create Release
```bash
git tag -a v1.2.3 -m "Release v1.2.3"
git push origin v1.2.3
```

---

## Status Checks

### Before Merge
- ✅ Build passes
- ✅ Tests pass (80%+ coverage)
- ✅ Code quality checks pass
- ✅ Security scans pass
- ✅ Code review approved
- ✅ Branches up to date

### Before Production
- ✅ All staging tests pass
- ✅ Performance acceptable
- ✅ Team approval
- ✅ Deployment window confirmed

---

## Notifications

### Slack Channels
- `#deployments`: Deployment notifications
- `#build-status`: Build notifications

### Message Types
- ✅ Deployment successful
- ❌ Build failed
- 🔔 Staging deployed
- 🚀 Production deployed

---

## Performance Targets

**Build Time**: 8-10 minutes  
**Test Time**: 2-3 minutes  
**Docker Build**: 5-10 minutes  
**Deployment**: 5 minutes  
**Image Size**: <200MB  
**Coverage**: >80%  

---

## Common Issues

| Problem | Solution |
|---------|----------|
| Build fails locally | `./gradlew clean build --stacktrace` |
| Tests fail | `./gradlew test --info` |
| Docker fails | `docker build . --progress=plain` |
| SSH connection fails | Verify secrets in GitHub |
| Database connection fails | Check docker-compose status |
| Secret not available | Verify secret name (case-sensitive) |
| Artifact not found | Check build logs for errors |

---

## Quick Links

- **Documentation**: [docs/](docs/)
- **Setup Guide**: [docs/SETUP.md](docs/SETUP.md)
- **Full Reference**: [docs/CI-CD-DOCUMENTATION.md](docs/CI-CD-DOCUMENTATION.md)
- **Implementation**: [docs/CI-CD-IMPLEMENTATION-CHECKLIST.md](docs/CI-CD-IMPLEMENTATION-CHECKLIST.md)
- **Contributing**: [docs/CONTRIBUTING.md](docs/CONTRIBUTING.md)

---

**Last Updated**: December 15, 2025  
**Version**: 1.0  
**Print-Friendly**: Yes - Keep this nearby!
