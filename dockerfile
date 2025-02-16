FROM maven:3.8.6-eclipse-temurin-17 AS build
RUN mkdir -p /workspace
WORKDIR /workspace
COPY pom.xml /workspace
COPY src /workspace/src
RUN mvn -f pom.xml clean package -Dmaven.test.skip

FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=build /workspace/target/Web_Shop_Api-*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]