FROM maven:3.9.9-eclipse-temurin-21-alpine AS build

WORKDIR /app

COPY src src
COPY pom.xml pom.xml

RUN --mount=type=cache,target=/root/.m2 mvn clean package -DskipTests

FROM eclipse-temurin:21-alpine

COPY --from=build /app/target/crebito*.jar crebito.jar

EXPOSE 8080
EXPOSE 9090

CMD sh -c 'java \
    -Xms150m \
    -Xmx170m \
    -Dspring.profiles.active=prod \
    -jar crebito.jar'