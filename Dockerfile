FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app

COPY marketplace-common/pom.xml ./marketplace-common/pom.xml
COPY marketplace-common/src ./marketplace-common/src
RUN cd marketplace-common && mvn clean install -DskipTests

COPY marketplace-auth/pom.xml ./marketplace-auth/pom.xml
COPY marketplace-auth/src ./marketplace-auth/src
RUN cd marketplace-auth && mvn clean package -DskipTests

FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /app/marketplace-auth/target/*.jar app.jar
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "app.jar"]