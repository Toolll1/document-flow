FROM amazoncorretto:11-alpine-jdk
COPY target/*.jar document-flow.jar
ENTRYPOINT ["java","-jar","/document-flow.jar"]