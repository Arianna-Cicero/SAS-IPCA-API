# Quick Commands Cheat Sheet

Copy-paste these for common tasks.

## Local Development

```bash
# Build everything
./gradlew clean build

# Run tests only
./gradlew test

# Check code quality
./gradlew detekt

# Start application
./gradlew run

# Generate test coverage report
./gradlew jacocoTestReport
```

## Docker

```bash
# Start local environment
docker-compose up -d

# View logs
docker-compose logs -f api

# Stop everything
docker-compose down

# Remove all data
docker-compose down -v

# Build Docker image
docker build -t api:latest .

# Run Docker container
docker run -p 8080:8080 api:latest
```

## Git & GitHub

```bash
# Create feature branch
git checkout -b feature/my-feature

# Add and commit
git add .
git commit -m "feat: add new feature"

# Push to GitHub
git push origin feature/my-feature

# Create version tag
git tag -a v1.0.0 -m "Release v1.0.0"
git push origin v1.0.0

# View logs
git log --oneline
```

## GitHub Actions

| Action | How |
|--------|-----|
| **Trigger CI** | Push to main/develop or create PR |
| **View results** | Click "Actions" tab in GitHub |
| **Check logs** | Click workflow run → failed job |
| **Re-run** | Click "Re-run jobs" button |
| **Cancel** | Click workflow run → "Cancel" |

## Troubleshooting Commands

```bash
# Clear Gradle cache
./gradlew clean

# Rebuild everything fresh
./gradlew clean build --no-build-cache

# Run with debug info
./gradlew build --stacktrace --debug

# Reset Docker
docker system prune -a

# Remove containers
docker-compose down -v

# Check status
docker-compose ps

# View logs
docker-compose logs app

# SSH into container
docker-compose exec api sh
```

## Environment Setup

```bash
# Copy template
cp .env.example .env

# Edit configuration
# (update JDBC_DATABASE_* values)

# Verify it works
./gradlew build
```

## Deployment

```bash
# Staging (automatic on develop)
git push origin develop
# → Deploys automatically in ~5 minutes

# Production (manual with approval)
git tag -a v1.0.0 -m "Release"
git push origin v1.0.0
# → Builds, deploys to staging, awaits approval
# → You approve in GitHub
# → Auto-deploys to production
```

## Common Workflows

### Feature Development
```bash
git checkout -b feature/user-auth
# ... make changes ...
./gradlew test
./gradlew detekt
git push origin feature/user-auth
# Create PR on GitHub
# Wait for CI to pass
# Get code review
git merge
```

### Deploy to Staging
```bash
git push origin develop
# Automatically deployed in ~15 min
# Check: https://staging-api.example.com
```

### Deploy to Production
```bash
git tag -a v1.2.0 -m "Release v1.2.0"
git push origin v1.2.0
# Awaits approval in GitHub Actions
# Click "Approve" button
# Auto-deployed in ~20 min
```

## Help & Documentation

| Need | Resource |
|------|----------|
| Setup guide | [SETUP.md](SETUP.md) |
| What's included | [OVERVIEW.md](OVERVIEW.md) |
| GitHub setup | [GITHUB-SETUP.md](GITHUB-SETUP.md) |
| Contributing | [CONTRIBUTING.md](CONTRIBUTING.md) |
| Issues | [TROUBLESHOOTING.md](TROUBLESHOOTING.md) |
| Technical details | [REFERENCE.md](REFERENCE.md) |

---

**Tip**: Bookmark this page for quick reference!  
**More help**: [INDEX.md](INDEX.md)
