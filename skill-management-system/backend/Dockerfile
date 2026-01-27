## Backend production Dockerfile (multi-stage)
## Builds Spring Boot jar with Maven (Java 25) and runs on a slim JRE.

FROM maven:3-eclipse-temurin-25 AS builder
WORKDIR /workspace

# Copy Maven wrapper and project files
COPY mvnw mvnw.cmd ./
COPY .mvn/ .mvn/
COPY pom.xml ./

# Make mvnw executable
RUN chmod +x ./mvnw

COPY src/ src/

# Build with tests to ensure quality; use --no-transfer-progress for cleaner logs
RUN ./mvnw -B --no-transfer-progress clean package


FROM eclipse-temurin:25-jre
WORKDIR /app

# Create non-root user
RUN addgroup --system appgroup && adduser --system --ingroup appgroup appuser

# Copy built jar (Spring Boot repackage produces a single fat jar)
COPY --from=builder /workspace/target/*.jar /app/app.jar

# Change ownership to appuser
RUN chown appuser:appgroup /app/app.jar

EXPOSE 8080

ENV JAVA_TOOL_OPTIONS="-XX:MaxRAMPercentage=75"

USER appuser
ENTRYPOINT ["java","-jar","/app/app.jar"]
