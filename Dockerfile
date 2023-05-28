FROM gradle:8.1.1-jdk AS gradle

RUN mkdir -p /opt/src/app

COPY . /opt/src/app

WORKDIR /opt/src/app

RUN gradle build --no-daemon

FROM eclipse-temurin:17-jdk-jammy

RUN mkdir -p /opt/app

COPY --from=gradle /opt/src/app/build/libs/*.jar /opt/app/

WORKDIR /opt/app

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/opt/app/wordcloud-worker-0.0.1-SNAPSHOT.jar"]