# ===== Build stage =====
FROM maven:3.9-eclipse-temurin-21 AS builder
WORKDIR /app
COPY pom.xml .
COPY wzoto-domain/pom.xml wzoto-domain/
COPY wzoto-infrastructure/pom.xml wzoto-infrastructure/
COPY wzoto-application/pom.xml wzoto-application/
COPY wzoto-interfaces/pom.xml wzoto-interfaces/
RUN mvn dependency:go-offline -B 2>/dev/null || true
COPY . .
RUN mvn clean package -DskipTests -B

# ===== Run stage =====
FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=builder /app/wzoto-interfaces/target/wzoto-interfaces-1.0.0-SNAPSHOT.jar app.jar
EXPOSE 8080
ENV JAVA_OPTS="-Xmx512m -Xms256m"
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar --server.port=8080"]
