#FROM adoptopenjdk:8-jre-openj9
FROM adoptopenjdk/openjdk8
COPY target/Smart2GoAdmin.jar /app/Smart2GoAdmin.jar
CMD ["java", "-jar", "/app/Smart2GoAdmin.jar"]
EXPOSE 9090