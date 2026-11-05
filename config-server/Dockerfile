FROM maven:3.9-eclipse-temurin-21 AS build

WORKDIR /app

COPY pom.xml .

COPY src ./src

RUN mvn -B clean package


FROM eclipse-temurin:21-jre

WORKDIR /app

COPY --from=build /app/target/config-server-1.0-SNAPSHOT.jar app.jar

COPY config-repo ./config-repo

EXPOSE 8888

ENTRYPOINT ["java", "-jar", "app.jar"]