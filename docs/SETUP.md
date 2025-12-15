# 🚀 Quick Setup Guide

Get your CI/CD pipeline running in 30 minutes.

## Prerequisites

- Java 17+ installed
- Git configured
- Docker & Docker Compose (for local testing)
- GitHub account

## 5-Step Setup

### Step 1: Verify Locally (5 min)

```bash
# Navigate to project
cd c:\Users\arian\Desktop\ipca\3\pdm\api

# Copy environment file
cp .env.example .env

# Build project
./gradlew clean build
```

✅ Build should complete successfully

### Step 2: Test with Docker (5 min)

```bash
# Start services
docker-compose up -d

# Check logs
docker-compose logs api

# Stop services
docker-compose down
```

✅ Services should start and stop cleanly

### Step 3: Verify Code Quality (5 min)

```bash
# Run code quality checks
./gradlew detekt

# Run tests
./gradlew test
```

✅ Should pass with no violations

### Step 4: Push to GitHub (5 min)

```bash
# Add files
git add .

# Commit
git commit -m "feat: add CI/CD pipeline"

# Push
git push origin main
```

✅ GitHub Actions should trigger automatically

### Step 5: Configure GitHub (10 min)

1. **Go to Settings > Secrets and variables > Actions**
2. **Add 4 secrets** (get values from your infrastructure):
   - `STAGING_DEPLOY_KEY` - Base64 encoded SSH private key
   - `STAGING_DEPLOY_HOST` - Staging server IP/hostname
   - `PROD_DEPLOY_KEY` - Base64 encoded SSH private key
   - `PROD_DEPLOY_HOST` - Production server IP/hostname

3. **Go to Settings > Branches > Add rule**
4. **Protect branches**:
   - Protect `main` branch
   - Require PR before merge
   - Require 1 code review
   - Require status checks pass

✅ **Done!** Your pipeline is now active.

## Verify It Works

1. **Create a test branch**:
   ```bash
   git checkout -b feature/test
   echo "# Test" >> README.md
   git push origin feature/test
   ```

2. **Create a Pull Request** on GitHub

3. **Watch GitHub Actions** run:
   - Build & test should run automatically
   - Code quality checks should run
   - Results appear in your PR

4. **Merge when checks pass**

5. **Watch automatic staging deployment**

## Next Steps

- Read [OVERVIEW.md](OVERVIEW.md) to understand what's included
- Read [GITHUB-SETUP.md](GITHUB-SETUP.md) for detailed GitHub configuration
- Read [CONTRIBUTING.md](CONTRIBUTING.md) for development workflow
- See [QUICK-COMMANDS.md](QUICK-COMMANDS.md) for everyday commands

## Troubleshooting

**Build fails locally?**
```bash
./gradlew clean build --stacktrace
```

**Docker issues?**
```bash
docker-compose down -v
docker system prune -a
docker-compose build --no-cache
```

**Check GitHub Actions logs**:
1. Go to GitHub > Actions tab
2. Click the failed workflow
3. Click the failed job
4. Scroll to see error details

See [TROUBLESHOOTING.md](TROUBLESHOOTING.md) for more help.

---

**Time**: 30 minutes  
**Difficulty**: Easy  
**Next**: [OVERVIEW.md](OVERVIEW.md)
