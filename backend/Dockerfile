
# ==============================
# stage 1 - build
# ==============================
FROM eclipse-temurin:25-jdk AS builder
WORKDIR /app

# wrapper + pom first: cache layer while dependencies doesnt change
COPY mvnw pom.xml ./
COPY .mvn .mvn

# for maven wrapper works correctly in linux container
RUN sed -i 's/\r$//' mvnw && chmod +x mvnw

RUN --mount=type=cache,target=/root/.m2 ./mvnw dependency:go-offline -B

COPY src ./src
RUN --mount=type=cache,target=/root/.m2 mvn clean package -DskipTests -B


# ==============================
# stage 2 - run
# ==============================
FROM eclipse-temurin:25-jre-alpine
WORKDIR /app

RUN addgroup -S spring && adduser -S spring -G spring

# copy just the .jar generated in stage 1
COPY --from=builder /app/target/*.jar app.jar

USER spring
EXPOSE 8080
ENTRYPOINT ["java", "-XX:MaxRAMPercentage=75.0", "-jar", "app.jar"]
# "-XX:MaxRAMPercentage=75.0" - jvm uses at maximum 75% of memory