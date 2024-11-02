FROM openjdk:21-jdk-oracle
WORKDIR /app
COPY target/*.jar /app/processor.jar
ENTRYPOINT ["java", "-jar", "processor.jar"]