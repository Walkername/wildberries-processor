FROM openjdk:21-jdk-oracle
WORKDIR /app
COPY target/wildberries-processor-0.0.1-SNAPSHOT.jar /app/processor.jar
EXPOSE 8082
ENTRYPOINT ["java", "-jar", "processor.jar"]