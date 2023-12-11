FROM maven:3.8.4-jdk-11-slim as build
ENV HOME /home/app
COPY src $HOME/src
COPY pom.xml $HOME
RUN --mount=type=cache,target=/root/.m2 mvn -f $HOME/pom.xml clean package

FROM amazoncorretto:11-alpine-jdk
COPY --from=build /home/app/target/*.jar document-flow.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/document-flow.jar"]