FROM maven:3.9-eclipse-temurin-25 AS build
WORKDIR /workspace
COPY pom.xml pom.xml
COPY src src
RUN mvn --batch-mode -DskipTests package \
    && cp target/call-service-1.0.0.jar /app.jar

FROM eclipse-temurin:25-jre
WORKDIR /app
COPY --from=build --chown=10001:10001 /app.jar app.jar
USER 10001:10001
EXPOSE 8082
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
