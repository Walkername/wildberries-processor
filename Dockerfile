FROM openjdk:21-jdk-oracle
WORKDIR /app
COPY /out/artifacts/wildberries_processor_jar/wildberries-processor.jar /app/wb-processor.jar
ENTRYPOINT ["java", "-jar", "wb-processor.jar"]