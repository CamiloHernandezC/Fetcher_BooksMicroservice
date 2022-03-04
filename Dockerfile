FROM maven:3.6.3-openjdk-11-slim AS build 
COPY src /home/books/src
copy pom.xml /home/books
RUN mvn -f /home/books/pom.xml clean package

FROM adoptopenjdk/openjdk11:jre-11.0.6_10-alpine
COPY --from=build "home/books/target/BooksMicroservice-0.0.1-SNAPSHOT.jar" "app.jar"
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]