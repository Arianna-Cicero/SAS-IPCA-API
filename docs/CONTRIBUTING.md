# Contributing Guide

Guidelines for developers contributing to this project with emphasis on CI/CD integration.

## Code of Conduct

- Be respectful and inclusive
- Report issues constructively
- Provide helpful feedback in code reviews
- Follow Kotlin coding conventions
- Respect project decisions

## Getting Started

### Local Setup

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
```

### Verify Setup

```bash
# Check Java version
java -version  # Should be 17+

# Check Gradle
./gradlew --version

# Check Docker
docker --version
docker-compose --version

# Run full check
./gradlew clean build detekt test
```

## Development Workflow

### Branch Naming

Follow Git Flow conventions:

```
feature/description          # New features
bugfix/description           # Bug fixes
hotfix/description           # Urgent production fixes
refactor/description         # Code refactoring
docs/description             # Documentation updates
```

Examples:
- `feature/user-authentication`
- `bugfix/null-pointer-exception`
- `docs/add-api-documentation`

### Creating a Feature Branch

```bash
# Update develop branch
git checkout develop
git pull origin develop

# Create feature branch
git checkout -b feature/your-feature-name

# Make changes locally
# ...

# Commit changes
git add .
git commit -m "feat: add your feature description"

# Push to remote
git push origin feature/your-feature-name
```

### Commit Messages

Follow Conventional Commits format:

```
<type>(<scope>): <subject>

<body>

<footer>
```

**Types**:
- `feat`: New feature
- `fix`: Bug fix
- `docs`: Documentation
- `style`: Code style (whitespace, formatting)
- `refactor`: Code refactoring
- `perf`: Performance improvement
- `test`: Test addition or update
- `chore`: Build, dependency updates

**Examples**:
```
feat(auth): implement JWT token refresh

fix(routes): handle null pointer in validator

docs(readme): add setup instructions

test(services): add unit tests for UserService
```

### Creating a Pull Request

1. Push feature branch to GitHub
2. Go to repository and click "Create Pull Request"
3. Fill in PR title and description
4. Link related issues (if applicable)
5. Request reviewers from team
6. Ensure CI workflow passes

**PR Checklist**:
- [ ] Descriptive title
- [ ] Description explains what and why
- [ ] Linked to related issues
- [ ] Tests added/updated
- [ ] Code quality checks passing
- [ ] No failing tests
- [ ] Documentation updated if needed

## Code Quality Standards

### Kotlin Conventions

Follow official Kotlin style guide:
- Use `val` by default, `var` only when necessary
- Use expression bodies for simple functions
- Use data classes for POJOs
- Use destructuring in loops when applicable

### Naming Conventions

- Classes: PascalCase (AuthService)
- Functions/variables: camelCase (getUserById)
- Constants: SCREAMING_SNAKE_CASE (MAX_RETRIES)
- Private members: _prefixed (optional, use sparingly)

### Code Limits (Enforced by Detekt)

- Max cyclomatic complexity: 15
- Max function length: 70 lines
- Max class length: 600 lines
- Max parameters: 6
- Max line length: 120 characters

### Running Quality Checks

```bash
# Run all quality checks locally
./gradlew detekt

# Auto-fix style issues (if applicable)
./gradlew ktlintFormat

# Generate code coverage report
./gradlew jacocoTestReport

# View coverage report
open build/reports/jacoco/test/html/index.html
```

## Testing Requirements

### Unit Tests

- Test all public functions
- Use descriptive test names
- One assertion per test when possible
- Use MockK for mocking

```kotlin
@Test
fun `getUserById should return user when found`() {
    // Given
    val userId = 1
    val expectedUser = User(id = 1, name = "John")
    
    // When
    val result = userService.getUserById(userId)
    
    // Then
    assertEquals(expectedUser, result)
}
```

### Integration Tests

- Test component interactions
- Use test database/containers
- Use Ktor TestApplicationEngine
- Clean up resources after tests

```kotlin
@Test
fun `GET api_users endpoint should return users`() {
    testApplication {
        client.get("/api/users").apply {
            assertEquals(HttpStatusCode.OK, status)
        }
    }
}
```

### Coverage Targets

- Minimum: 60% code coverage
- Target: 80%+ code coverage
- Critical paths: 100% coverage

### Running Tests

```bash
# Run all tests
./gradlew test

# Run specific test class
./gradlew test --tests com.ipca.services.UserServiceTest

# Run with coverage report
./gradlew test jacocoTestReport

# Run in CI mode (with assertions)
./gradlew test --info
```

## Security Considerations

### Sensitive Data

- Never commit passwords, API keys, or tokens
- Use `.env` file for local configuration
- Use GitHub Secrets for CI/CD
- Use environment variables in Docker

### Dependency Updates

- Keep dependencies up to date
- Review security advisories
- Update regularly (monthly recommended)
- Run OWASP Dependency Check

```bash
./gradlew dependencyCheckAnalyze
```

### Code Review Checklist

When reviewing PRs, check:
- [ ] No hardcoded secrets
- [ ] Input validation present
- [ ] SQL injection prevention (using parameterized queries)
- [ ] XSS protection in responses
- [ ] CORS headers configured correctly
- [ ] Authentication/authorization working
- [ ] Error messages don't leak info

## Review Process

### For Contributors

1. Create PR with descriptive title
2. Reference issue if applicable
3. Request review from team lead
4. Respond to review comments
5. Make requested changes
6. Rebase if main has changed

### For Reviewers

1. Read PR description and context
2. Review code changes
3. Run locally if complex
4. Check code quality
5. Verify tests are adequate
6. Check security
7. Approve or request changes

**Review Types**:
- **Request changes**: Blocking issue found
- **Comment**: Non-blocking suggestion
- **Approve**: Ready to merge

## Deployment

### Staging Deployment

Automatic on develop branch:
1. Create/push to feature branch
2. Create PR against develop
3. After approval and merge, CI/CD triggers
4. Staging auto-deploys

### Production Deployment

Manual approval required:
1. Create version tag: `git tag -a v1.2.3 -m "Release 1.2.3"`
2. Push tag: `git push origin v1.2.3`
3. CD pipeline triggers automatically
4. Manual approval step appears in GitHub
5. After approval, production deploys

## Documentation

### Code Documentation

- Add KDoc to public functions
- Document parameters and return values
- Include examples for complex logic

```kotlin
/**
 * Retrieves a user by ID from the database.
 *
 * @param userId The ID of the user to retrieve
 * @return User object if found, null otherwise
 * @throws DatabaseException if query fails
 */
fun getUserById(userId: Int): User? {
    // ...
}
```

### API Documentation

- Document endpoints with descriptions
- Include request/response examples
- Document error codes

### Update Documentation

When adding features:
- Update relevant docs in `docs/` folder
- Update README if behavior changes
- Update API documentation
- Add/update architecture diagrams if needed

## Troubleshooting

### Local Build Issues

```bash
# Clean build (removes all cached artifacts)
./gradlew clean build

# Reset Gradle cache
./gradlew clean --no-build-cache

# Check Gradle dependencies
./gradlew dependencies

# Check for conflicts
./gradlew dependencyInsight --dependency kotlin-stdlib
```

### CI Pipeline Failures

1. Check GitHub Actions logs
2. Reproduce locally: `./gradlew build`
3. Check test output
4. Review code quality report
5. Fix issues and push again

### Database Issues

```bash
# Check if PostgreSQL is running
docker-compose ps

# View database logs
docker-compose logs postgres

# Connect to database
docker-compose exec postgres psql -U api_user -d loja_social_ipca

# Reset database
docker-compose down -v  # Remove volumes too
docker-compose up -d postgres
```

## Performance Tips

### Build Optimization

```bash
# Skip tests for faster builds during development
./gradlew build -x test

# Use parallel builds
./gradlew build --parallel

# Enable build cache
./gradlew build --build-cache
```

### Development Tips

- Run tests in IDE for faster feedback
- Use IDE inspections for code quality
- Commit frequently with atomic changes
- Pull latest main/develop regularly

## Additional Resources

- [Kotlin Documentation](https://kotlinlang.org/docs/)
- [Ktor Documentation](https://ktor.io/docs/)
- [Gradle Documentation](https://docs.gradle.org)
- [GitHub Actions Documentation](https://docs.github.com/en/actions)
- [Conventional Commits](https://www.conventionalcommits.org)

## Questions?

- Ask in team chat
- Check existing documentation
- Create GitHub issue for bugs
- Create discussion for questions

---

**Last Updated**: December 15, 2025  
**Version**: 1.0
