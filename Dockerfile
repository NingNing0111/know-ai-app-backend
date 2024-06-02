FROM arm64v8/openjdk:17
ENV BASE_KEY=""
ENV BASE_URL=""
ARG JAR_FILE=target/*.jar
COPY ./target/know-hub-ai-app-1.0-SNAPSHOT.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]

