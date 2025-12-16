# Multi-stage build for optimal image size
FROM eclipse-temurin:17-jdk-alpine AS builder

WORKDIR /app

# Copy gradle files
COPY gradle/ gradle/
COPY gradlew gradlew.bat build.gradle.kts settings.gradle.kts gradle.properties ./
RUN chmod +x gradlew

# Download dependencies
RUN ./gradlew dependencies --no-daemon

# Copy source code
COPY src/ src/

# Build application
RUN ./gradlew build -x test --no-daemon

# Runtime stage
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

# Install curl for health checks
RUN apk add --no-cache curl

# Copy built application from builder
COPY --from=builder /app/build/distributions/LojaSocialIPCA_API.tar ./app.tar
RUN tar -xf app.tar && rm app.tar && mv LojaSocialIPCA_API-* api

# Create non-root user
RUN addgroup -g 1001 -S appuser && adduser -u 1001 -S appuser -G appuser
USER appuser

# Expose port
EXPOSE 8080

# Health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=10s --retries=3 \
  CMD curl -f http://localhost:8080/ || exit 1

# Run application
CMD ["sh", "-c", "exec api/bin/LojaSocialIPCA_API"]
