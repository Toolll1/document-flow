FROM maven:3.8.4-jdk-11-slim as build
COPY src /home/app/src
COPY pom.xml /home/app
RUN mvn -f /home/app/pom.xml clean package

FROM amazoncorretto:11-alpine-jdk
COPY --from=build /home/app/target/*.jar document-flow.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/document-flow.jar", "--service.file=minio"]