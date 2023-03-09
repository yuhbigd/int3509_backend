FROM openjdk:18.0-jdk-slim
COPY target/nhatrotot.jar /home/nhatrotot.jar
CMD ["java","-jar","/home/nhatrotot.jar"]