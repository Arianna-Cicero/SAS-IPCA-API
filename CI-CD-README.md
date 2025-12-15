# LojaSocialIPCA API - CI/CD Pipeline

A comprehensive, production-ready CI/CD pipeline for automated testing, code quality checks, security scanning, and deployment.

## 🚀 Quick Start

```bash
# Build locally
./gradlew clean build

# Run with Docker
docker-compose up -d

# View documentation
# See: docs/ folder
```

## 📚 Documentation

All CI/CD documentation is in the **[`docs/`](docs/)** folder:

- **[00-START-HERE.md](docs/00-START-HERE.md)** ← Begin here (2 min)
- **[README-CI-CD.md](docs/README-CI-CD.md)** - Complete overview
- **[QUICK-REFERENCE.md](docs/QUICK-REFERENCE.md)** - Cheat sheet
- **[CI-CD-DOCUMENTATION.md](docs/CI-CD-DOCUMENTATION.md)** - Full reference
- **[GITHUB-ACTIONS-SETUP.md](docs/GITHUB-ACTIONS-SETUP.md)** - GitHub setup
- **[CONTRIBUTING.md](docs/CONTRIBUTING.md)** - Developer guide
- **[CI-CD-IMPLEMENTATION-CHECKLIST.md](docs/CI-CD-IMPLEMENTATION-CHECKLIST.md)** - Setup checklist
- **[DOCUMENTATION-INDEX.md](docs/DOCUMENTATION-INDEX.md)** - Navigation guide

## ✨ What's Included

✅ **GitHub Actions** - CI/CD automation  
✅ **Docker** - Containerization & local development  
✅ **Code Quality** - Detekt (60+ rules)  
✅ **Testing** - JUnit 5 + JaCoCo coverage  
✅ **Security** - OWASP scanning  
✅ **Deployments** - Staging & production  
✅ **Documentation** - 25,000+ words  

## 🎯 Next Steps

1. Read [docs/00-START-HERE.md](docs/00-START-HERE.md)
2. Review [docs/README-CI-CD.md](docs/README-CI-CD.md)
3. Follow [docs/CI-CD-IMPLEMENTATION-CHECKLIST.md](docs/CI-CD-IMPLEMENTATION-CHECKLIST.md)

## 💡 Key Features

- **Automated Testing** - Every push triggers tests
- **Code Quality** - Enforces standards with Detekt
- **Security Scanning** - OWASP dependency checks
- **Docker Builds** - Automatic image creation
- **Smart Deployments** - Auto staging, approval-gated production
- **Zero Cost** - Runs entirely on GitHub's free tier

## 📖 Project Structure

```
.
├── .github/workflows/          # GitHub Actions (CI/CD)
├── config/detekt/              # Code quality rules
├── docker-compose.yml          # Local dev environment
├── Dockerfile                  # Container definition
├── docs/                       # 📚 All documentation
├── scripts/                    # Build & deployment scripts
└── src/                        # Application code
```

## 🔧 Common Commands

```bash
# Build & test
./gradlew clean build

# Code quality check
./gradlew detekt

# Run tests
./gradlew test

# Local development with Docker
docker-compose up -d

# Create release (v1.0.0)
git tag -a v1.0.0 -m "Release"
git push origin v1.0.0
```

## 📊 Pipeline Overview

```
Push Code → CI Pipeline (build + test + quality + security)
         ↓
         → CD Pipeline (Docker + staging + production)
         ↓
    Deployed! ✅
```

## ⏱️ Performance

- **CI Duration**: 8-10 minutes
- **CD Duration**: 15-20 minutes  
- **Build with Cache**: 5-6 minutes
- **Monthly Cost**: $0.00 (free tier)

## 📞 Need Help?

1. Check [docs/DOCUMENTATION-INDEX.md](docs/DOCUMENTATION-INDEX.md) to navigate
2. Read [docs/QUICK-REFERENCE.md](docs/QUICK-REFERENCE.md) for commands
3. See [docs/CI-CD-DOCUMENTATION.md](docs/CI-CD-DOCUMENTATION.md) for details

---

**Status**: ✅ Ready for immediate deployment  
**Created**: December 14, 2024  
**For more info**: See [docs/](docs/) folder
