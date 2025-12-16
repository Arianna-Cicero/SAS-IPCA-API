# CI/CD Implementation Checklist

Complete step-by-step guide with verification points for implementing the CI/CD pipeline.

## Phase 1: Prerequisites & Setup (30 minutes)

### Tools Installation
- [ ] Install Java 17+
  - Verification: `java -version` shows 17+
- [ ] Install Docker & Docker Compose
  - Verification: `docker --version` and `docker-compose --version`
- [ ] Install Git
  - Verification: `git --version`
- [ ] Setup GitHub CLI (optional but recommended)
  - Verification: `gh --version`

### Repository Access
- [ ] Clone repository
  ```bash
  git clone <repository-url>
  cd api
  ```
- [ ] Verify write permissions to repository
- [ ] Verify access to GitHub Secrets settings
- [ ] Verify Docker registry access

### Environment Files
- [ ] Copy `.env.example` to `.env`
  ```bash
  cp .env.example .env
  ```
- [ ] Update `.env` with actual values
  - Database credentials
  - API keys
  - Configuration values
- [ ] Verify file has correct permissions

---

## Phase 2: Local Build & Test (45 minutes)

### Build Verification
- [ ] Clean build succeeds locally
  ```bash
  ./gradlew clean build
  ```
  - Expected: BUILD SUCCESSFUL
- [ ] Test suite passes
  ```bash
  ./gradlew test
  ```
  - Expected: All tests pass, >80% coverage
- [ ] Code quality checks pass
  ```bash
  ./gradlew detekt
  ```
  - Expected: No errors, only warnings acceptable

### Database Setup
- [ ] PostgreSQL running locally
  ```bash
  docker-compose up -d postgres
  ```
- [ ] Database migrations applied
  ```bash
  ./gradlew migrate
  ```
- [ ] Sample data loaded (if applicable)

### Application Startup
- [ ] Application runs locally
  ```bash
  ./gradlew run
  ```
  - Expected: Starts on port 8080
- [ ] Endpoints respond correctly
  ```bash
  curl http://localhost:8080/health
  ```
- [ ] Logs show no errors

---

## Phase 3: Docker Configuration (30 minutes)

### Dockerfile Verification
- [ ] Dockerfile exists and is valid
- [ ] Multi-stage build is configured
- [ ] Final image uses Alpine base
- [ ] Non-root user is configured
- [ ] Health checks are defined

### Docker Build
- [ ] Build Docker image locally
  ```bash
  docker build -t loja-social-ipca-api:latest .
  ```
  - Expected: Build completes in 2-3 minutes
- [ ] Image size is acceptable (<500MB)
  ```bash
  docker images | grep loja-social-ipca-api
  ```
- [ ] Image runs successfully
  ```bash
  docker run -p 8080:8080 loja-social-ipca-api:latest
  ```
- [ ] Health check responds
  ```bash
  curl http://localhost:8080/health
  ```

### Docker Compose Verification
- [ ] docker-compose.yml is valid
  ```bash
  docker-compose config
  ```
- [ ] All services start successfully
  ```bash
  docker-compose up
  ```
- [ ] Services communicate correctly
- [ ] PostgreSQL is accessible from API container
- [ ] Data persists between restarts

---

## Phase 4: GitHub Secrets Configuration (20 minutes)

### Create Personal Access Token
- [ ] Navigate to GitHub Settings > Developer settings
- [ ] Create new Personal Access Token (classic)
- [ ] Grant `write:packages` and `read:packages` scopes
- [ ] Copy token (will not be visible again)

### Add GitHub Secrets
- [ ] Go to repository Settings > Secrets and variables > Actions
- [ ] Add `GHCR_TOKEN`
  - Value: Personal Access Token created above
  - Verification: Type 'token' and see recent token in autocomplete
- [ ] Add `SSH_PRIVATE_KEY`
  - Value: Private SSH key for deployment servers
  - Verification: Starts with `-----BEGIN RSA PRIVATE KEY-----`
- [ ] Add `SSH_KNOWN_HOSTS`
  - Value: Known hosts entry (get via `ssh-keyscan -H server.com`)
  - Verification: Contains server fingerprint
- [ ] Add `DEPLOYMENT_USER`
  - Value: SSH username
- [ ] Add `STAGING_HOST`
  - Value: Staging server hostname
- [ ] Add `PRODUCTION_HOST`
  - Value: Production server hostname

### Slack Integration (Optional)
- [ ] Create Slack webhook in workspace
- [ ] Add `SLACK_WEBHOOK_URL` secret
  - Value: Webhook URL starting with `https://hooks.slack.com`

---

## Phase 5: GitHub Actions Workflows (30 minutes)

### CI Workflow File
- [ ] `.github/workflows/ci.yml` exists
- [ ] File validates correctly
  ```bash
  gh workflow view ci.yml
  ```
- [ ] Triggers are configured
  - [ ] Triggers on push to main
  - [ ] Triggers on push to develop
  - [ ] Triggers on all PRs
- [ ] All 4 jobs are defined
  - [ ] Build job
  - [ ] Code quality job
  - [ ] Build artifact job
  - [ ] Security scan job
- [ ] Caching is enabled for Gradle

### CD Workflow File
- [ ] `.github/workflows/cd.yml` exists
- [ ] File validates correctly
- [ ] Triggers are configured
  - [ ] Triggers on main branch pushes (staging)
  - [ ] Triggers on version tags (production)
- [ ] All 4 jobs are defined
  - [ ] Deploy preparation
  - [ ] Build and push Docker image
  - [ ] Deploy to staging
  - [ ] Deploy to production
- [ ] Production job requires approval
- [ ] Environment protection rule is set

### First Workflow Run
- [ ] Commit and push to main branch
  ```bash
  git add .
  git commit -m "feat: enable CI/CD pipeline"
  git push origin main
  ```
- [ ] Go to repository **Actions** tab
- [ ] CI workflow starts automatically
- [ ] Monitor workflow progress
  - [ ] Build job completes
  - [ ] Code quality job completes
  - [ ] Security scan job completes
  - [ ] Artifact job completes
- [ ] All jobs show ✅ green checkmark
- [ ] Verify artifacts are uploaded
  - [ ] Test reports
  - [ ] Detekt reports
  - [ ] Distribution packages

---

## Phase 6: Deployment Testing (45 minutes)

### Staging Deployment
- [ ] Push to develop branch
  ```bash
  git checkout develop
  git push origin develop
  ```
- [ ] CD workflow triggers automatically
- [ ] Docker image builds successfully
- [ ] Docker image pushes to GHCR
- [ ] Staging deployment begins
- [ ] Check deployment progress in Actions tab
- [ ] Verify deployed application is accessible
- [ ] Run smoke tests against staging

### Production Deployment
- [ ] Create version tag
  ```bash
  git tag -a v1.0.0 -m "Release version 1.0.0"
  git push origin v1.0.0
  ```
- [ ] CD workflow triggers
- [ ] Docker image builds and pushes
- [ ] Staging deployment completes automatically
- [ ] **Manual approval required** - Go to Actions tab
  - [ ] Click "Production deployment" job
  - [ ] Click "Review deployments"
  - [ ] Select "Approve and deploy"
- [ ] Production deployment begins
- [ ] Verify GitHub Release is created
  - [ ] Auto-generated release notes
  - [ ] Artifacts attached
- [ ] Verify deployed application is live

### Notifications
- [ ] Slack notifications received on deployment
  - [ ] Success notification has ✅ emoji
  - [ ] Shows deployment environment
  - [ ] Shows git commit/tag

---

## Phase 7: Monitoring & Verification (30 minutes)

### Workflow Dashboard
- [ ] Go to repository **Actions** tab
- [ ] Verify both workflows are visible
  - [ ] CI workflow shows recent runs
  - [ ] CD workflow shows recent runs
- [ ] Check workflow status indicators
  - [ ] Green checkmarks for successful runs
  - [ ] Red X for failed runs
  - [ ] Orange clock for in-progress runs

### Branch Protection Rules
- [ ] Go to repository **Settings** > **Branches**
- [ ] Add rule for `main` branch
  - [ ] Enable "Require status checks to pass"
  - [ ] Select CI workflow checks
  - [ ] Enable "Require code reviews"
  - [ ] Set to 1 reviewer
- [ ] Add rule for `develop` branch
  - [ ] Enable "Require status checks to pass"
  - [ ] Disable code review requirement (for quick merges)

### Test PR Workflow
- [ ] Create feature branch
  ```bash
  git checkout -b feature/test-pr
  ```
- [ ] Make minor change and commit
- [ ] Create pull request
- [ ] Verify CI workflow triggers
- [ ] Verify branch protection check appears
  - [ ] Check shows as pending
  - [ ] Check updates as CI completes
  - [ ] Check shows as passed
- [ ] Merge PR only after check passes
- [ ] Verify develop deployment triggers

---

## Phase 8: Code Quality Configuration (20 minutes)

### Detekt Setup
- [ ] `config/detekt/detekt.yml` exists
- [ ] Detekt rules are configured
- [ ] Run Detekt locally
  ```bash
  ./gradlew detekt
  ```
- [ ] View Detekt report
  ```bash
  open build/reports/detekt/detekt.html
  ```
- [ ] Address any code quality violations
- [ ] Run Detekt in CI workflow successfully

### Code Coverage
- [ ] Generate coverage report locally
  ```bash
  ./gradlew test jacocoTestReport
  ```
- [ ] Verify coverage is >80%
- [ ] View coverage report
  ```bash
  open build/reports/jacoco/test/html/index.html
  ```
- [ ] Review low-coverage areas
- [ ] Add tests for critical paths

### Dependency Checking
- [ ] Run OWASP Dependency Check
  ```bash
  ./gradlew dependencyCheckAnalyze
  ```
- [ ] Review any vulnerabilities found
- [ ] Create suppression file if needed
  ```bash
  mkdir -p config/owasp
  # Add suppressions.xml if needed
  ```
- [ ] Verify dependency check runs in CI

---

## Phase 9: Documentation & Handoff (30 minutes)

### Documentation
- [ ] All CI/CD docs are in `docs/` folder
- [ ] Team has access to documentation
- [ ] Documentation is up to date
- [ ] Architecture diagram is current
- [ ] Troubleshooting guide is comprehensive

### Team Training
- [ ] Team members have GitHub access
- [ ] Team members understand git workflow
- [ ] Team members can read CI/CD logs
- [ ] Team members know how to debug failures
- [ ] Team members understand deployment process

### Handoff
- [ ] All configurations are version controlled
- [ ] No credentials in repository
- [ ] Secrets are securely managed
- [ ] Deployment procedures are documented
- [ ] Emergency contact info is available

---

## Verification Checklist

### Pre-Production Sign-Off

- [ ] All 9 phases completed
- [ ] All workflow runs successful
- [ ] All tests passing (>80% coverage)
- [ ] All code quality checks passing
- [ ] All security scans passing
- [ ] Staging environment verified
- [ ] Production deployment tested
- [ ] Documentation complete
- [ ] Team trained and ready
- [ ] Monitoring and alerts configured

### Ready for Production ✅

Once all items are checked:
- [ ] Schedule production deployment
- [ ] Send team notification
- [ ] Monitor deployment progress
- [ ] Verify application health
- [ ] Record deployment time and team members
- [ ] Create post-deployment summary

---

## Common Issues & Solutions

### Workflow Not Triggering
- **Problem**: Changes pushed but workflow doesn't start
- **Solution**: Check branch names match workflow triggers
- **Solution**: Verify GitHub Secrets are added (required for CD)

### Build Fails on Gradle Cache
- **Problem**: Random build failures with cache
- **Solution**: Delete Gradle cache and retry
- **Solution**: Use `--no-build-cache` flag

### Docker Push Fails
- **Problem**: Cannot push to GHCR
- **Solution**: Verify GHCR_TOKEN secret is correct
- **Solution**: Check token hasn't expired

### SSH Deployment Fails
- **Problem**: Cannot connect to deployment server
- **Solution**: Verify SSH_PRIVATE_KEY secret is correct
- **Solution**: Ensure public key is on server
- **Solution**: Check SSH_KNOWN_HOSTS is configured

---

**Status**: ✅ Ready to Use  
**Last Updated**: December 15, 2025
