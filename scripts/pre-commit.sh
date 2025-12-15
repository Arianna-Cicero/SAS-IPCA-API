#!/bin/bash
# Pre-commit hook for local validation
# Install with: cp scripts/pre-commit.sh .git/hooks/pre-commit && chmod +x .git/hooks/pre-commit

echo "Running pre-commit checks..."

# Check if gradlew exists
if [ ! -f "./gradlew" ]; then
    echo "❌ gradlew not found"
    exit 1
fi

echo "📝 Running code quality checks with Detekt..."
./gradlew detekt --quiet
if [ $? -ne 0 ]; then
    echo "❌ Detekt found issues. Run './gradlew detekt' to view details."
    exit 1
fi

echo "✅ Running tests..."
./gradlew test --quiet
if [ $? -ne 0 ]; then
    echo "❌ Tests failed. Fix before committing."
    exit 1
fi

echo "✅ All checks passed!"
exit 0
