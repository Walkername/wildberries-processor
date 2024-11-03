FROM openjdk:21-jdk-oracle
WORKDIR /app
COPY target/wb-processor.jar /app/processor.jar
EXPOSE 8082
ENTRYPOINT ["java", "-jar", "processor.jar"]