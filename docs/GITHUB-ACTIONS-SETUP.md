# GitHub Actions Setup Guide

Complete configuration and customization guide for GitHub Actions workflows.

## Prerequisites

- GitHub repository with write access
- GitHub Secrets configured
- Docker Hub or GitHub Container Registry account
- SSH keys for deployment servers
- Slack workspace (optional but recommended)

## GitHub Secrets Configuration

### Required Secrets

Navigate to **Settings > Secrets and variables > Actions** and add:

```
GHCR_TOKEN         = GitHub Personal Access Token with write:packages
SSH_PRIVATE_KEY    = Private SSH key for deployment servers
SSH_KNOWN_HOSTS    = SSH known_hosts file content
SLACK_WEBHOOK_URL  = Slack webhook for notifications
DEPLOYMENT_USER    = SSH username for deployment servers
STAGING_HOST       = Staging server hostname
PRODUCTION_HOST    = Production server hostname
```

### Creating GitHub Personal Access Token

1. Go to Settings > Developer settings > Personal access tokens > Tokens (classic)
2. Click "Generate new token"
3. Grant scopes:
   - `write:packages`
   - `read:packages`
4. Set expiration: 90 days (rotate regularly)
5. Copy token immediately - can't view again

### Creating SSH Keys

```bash
# Generate key pair (4096-bit RSA)
ssh-keygen -t rsa -b 4096 -f deployment_key -C "ci-deployment"

# Copy public key to deployment servers
ssh-copy-id -i deployment_key.pub user@server

# Get private key content for GitHub
cat deployment_key | base64 > github_secret.txt
```

### Slack Webhook Setup

1. Go to https://api.slack.com/apps
2. Create New App > From scratch
3. Name: "GitHub CI/CD"
4. Select workspace
5. In features, enable Incoming Webhooks
6. Add New Webhook to Workspace
7. Choose channel: #deployments
8. Copy webhook URL to GitHub Secrets

## CI Workflow Configuration

### File Location
`.github/workflows/ci.yml`

### Triggers

```yaml
on:
  push:
    branches: [ main, develop ]
  pull_request:
    branches: [ main, develop ]
```

**When it runs**:
- Every push to main or develop
- Every PR against main or develop

### Jobs Configuration

#### Build Job

```yaml
jobs:
  build:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v4
      
      - name: Set up Java
        uses: actions/setup-java@v3
        with:
          java-version: '17'
          distribution: 'temurin'
          cache: gradle
```

**Cache behavior**:
- Gradle cache reduces build time by 60%
- Maven cache also available
- Cache key: `${{ runner.os }}-gradle-${{ hashFiles('**/*.gradle.kts') }}`

#### Code Quality Job

```yaml
code-quality:
  runs-on: ubuntu-latest
  steps:
    - uses: actions/checkout@v4
    
    - name: Run Detekt
      run: ./gradlew detekt
    
    - name: Upload Detekt Report
      if: always()
      uses: actions/upload-artifact@v4
      with:
        name: detekt-report
        path: build/reports/detekt/
```

**Customizing Detekt rules**:
- Edit: `config/detekt/detekt.yml`
- Add rules, adjust limits, enable/disable checks
- Restart build to apply changes

#### Security Scan Job

```yaml
security-scan:
  runs-on: ubuntu-latest
  steps:
    - uses: actions/checkout@v4
    
    - name: Run OWASP Dependency Check
      run: ./gradlew dependencyCheckAnalyze
```

**Configure dependency check**:
- Suppression file: `config/owasp/suppressions.xml`
- Update frequency: Default (every scan)

## CD Workflow Configuration

### File Location
`.github/workflows/cd.yml`

### Triggers

```yaml
on:
  push:
    branches: [ main ]
    tags:
      - 'v*'
```

**When it runs**:
- Push to main branch (staging only)
- Creation of version tags (full deployment)

### Docker Setup

#### GitHub Container Registry (GHCR)

```yaml
- name: Log in to GHCR
  uses: docker/login-action@v2
  with:
    registry: ghcr.io
    username: ${{ github.actor }}
    password: ${{ secrets.GHCR_TOKEN }}

- name: Build and push Docker image
  uses: docker/build-push-action@v4
  with:
    context: .
    push: true
    tags: |
      ghcr.io/${{ github.repository }}:${{ github.sha }}
      ghcr.io/${{ github.repository }}:latest
    cache-from: type=registry,ref=ghcr.io/${{ github.repository }}:buildcache
    cache-to: type=registry,ref=ghcr.io/${{ github.repository }}:buildcache,mode=max
```

**Image tagging strategy**:
- `v1.2.3` = Release version
- `develop` = Development branch
- `sha-abc123def` = Specific commit
- `latest` = Most recent

### Deployment to Staging

```yaml
deploy-staging:
  if: github.ref == 'refs/heads/develop'
  runs-on: ubuntu-latest
  steps:
    - name: Deploy to staging
      uses: appleboy/ssh-action@master
      with:
        host: ${{ secrets.STAGING_HOST }}
        username: ${{ secrets.DEPLOYMENT_USER }}
        key: ${{ secrets.SSH_PRIVATE_KEY }}
        script: |
          docker pull ghcr.io/${{ github.repository }}:${{ github.sha }}
          docker-compose -f docker-compose.staging.yml up -d
```

**Staging deployment**:
- Automatic on develop branch
- No approval required
- Updates immediately
- Good for testing before production

### Production Deployment

```yaml
deploy-production:
  if: startsWith(github.ref, 'refs/tags/v')
  environment:
    name: production
    url: https://api.example.com
  runs-on: ubuntu-latest
  steps:
    - name: Deploy to production
      uses: appleboy/ssh-action@master
      with:
        host: ${{ secrets.PRODUCTION_HOST }}
        username: ${{ secrets.DEPLOYMENT_USER }}
        key: ${{ secrets.SSH_PRIVATE_KEY }}
        script: |
          docker pull ghcr.io/${{ github.repository }}:${{ github.sha }}
          docker-compose -f docker-compose.prod.yml up -d
```

**Production deployment**:
- Only on version tags
- Requires manual approval (environment protection)
- Cannot be bypassed
- Notifications sent to Slack

## Customizing Workflows

### Adding New Jobs

1. Open `.github/workflows/ci.yml` or `cd.yml`
2. Add job under `jobs:` section
3. Define `runs-on`, `steps`
4. Use `needs: [other-job]` for dependencies

Example:
```yaml
lint:
  runs-on: ubuntu-latest
  steps:
    - uses: actions/checkout@v4
    - name: Run linter
      run: ./gradlew lint
```

### Conditional Execution

```yaml
if: contains(github.event.head_commit.message, 'skip-ci')
if: github.event_name == 'pull_request'
if: success()
if: always()  # Run even if previous jobs failed
```

### Environment Variables

```yaml
env:
  NODE_ENV: production
  KOTLIN_VERSION: 1.9.22

steps:
  - name: Use variable
    run: echo "Environment: ${{ env.NODE_ENV }}"
```

## Notifications

### Slack Integration

```yaml
- name: Notify Slack - Success
  if: success()
  run: |
    curl -X POST ${{ secrets.SLACK_WEBHOOK_URL }} \
      -H 'Content-Type: application/json' \
      -d '{
        "text": "✅ Deployment successful!",
        "channel": "#deployments"
      }'

- name: Notify Slack - Failure
  if: failure()
  run: |
    curl -X POST ${{ secrets.SLACK_WEBHOOK_URL }} \
      -H 'Content-Type: application/json' \
      -d '{
        "text": "❌ Deployment failed!",
        "channel": "#deployments"
      }'
```

### Email Notifications

GitHub provides default notifications. Configure in:
**Settings > Notifications > Email notifications**

## Performance Optimization

### Caching

```yaml
- name: Cache Gradle packages
  uses: actions/cache@v3
  with:
    path: ~/.gradle/caches
    key: ${{ runner.os }}-gradle-${{ hashFiles('**/*.gradle.kts') }}
    restore-keys: |
      ${{ runner.os }}-gradle-
```

Benefits:
- Reduces build time by 60%
- Caches dependencies
- Caches compilation artifacts

### Parallel Jobs

```yaml
jobs:
  job1:
    runs-on: ubuntu-latest
  
  job2:
    runs-on: ubuntu-latest
  
  job3:
    needs: [job1, job2]  # Runs after job1 and job2
```

### Matrix Builds

```yaml
strategy:
  matrix:
    java-version: [17, 21]

steps:
  - uses: actions/setup-java@v3
    with:
      java-version: ${{ matrix.java-version }}
```

Runs job for each Java version in parallel.

## Debugging Workflows

### View Logs

1. Go to **Actions** tab
2. Select workflow run
3. Click failed job
4. Expand steps to see logs

### Enable Debug Logging

```yaml
- name: Enable debug logging
  run: echo "ACTIONS_STEP_DEBUG=true" >> $GITHUB_ENV
```

### Re-run Workflows

- GitHub Actions > Select workflow run
- Click "Re-run jobs" or "Re-run all jobs"

## Common Issues

### Secrets Not Available

- Check secret name matches exactly (case-sensitive)
- Verify secret is accessible to this workflow
- Check workflow file syntax

### SSH Connection Fails

- Verify SSH keys are correct
- Check known_hosts contains server
- Ensure deployment user has permissions

### Docker Build Times Out

- Reduce Dockerfile size
- Use layer caching
- Increase timeout in workflow

## Repository Settings

### Branch Protection

**Settings > Branches > Add rule**

For `main` branch:
- ✅ Require status checks to pass
- ✅ Require branches to be up to date
- ✅ Require code reviews (1+)
- ✅ Dismiss stale reviews

For `develop` branch:
- ✅ Require status checks to pass
- ☑ Require code reviews (1+)

### Environments

**Settings > Environments > Create environment**

1. **Production**
   - Add deployment branch: `main` (tags only)
   - Add reviewers: Lead developers
   - Set variables/secrets

2. **Staging**
   - Add deployment branch: `develop`
   - No approval required

---

**Last Updated**: December 15, 2025
