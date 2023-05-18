FROM adoptopenjdk/openjdk11
ADD target/bloomberg-fx-deals-0.0.1-SNAPSHOT.jar bloomberg-fx-deals-0.0.1-SNAPSHOT.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "bloomberg-fx-deals-0.0.1-SNAPSHOT.jar"]