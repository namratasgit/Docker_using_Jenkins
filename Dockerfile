FROM openjdk:11
EXPOSE 8080
ADD target/calculator_app.jar calculator_app.jar
ENTRYPOINT ["java", "-jar", "/calculator_app.jar"]