FROM openjdk:21-jdk-alpine AS builder
WORKDIR /tmp
RUN apt-get update && apt-get install -y openjdk-21-jdk findutils
COPY . /tmp
RUN chmod +x ./gradlew && ./gradlew clean bootJar

FROM openjdk:21-jdk-alpine
WORKDIR /tmp
COPY --from=build /tmp/build/libs/potatoStudy_jwt-0.0.1-SNAPSHOT.jar /tmp/potatostudy.jar
ENTRYPOINT ["sh", "-c", "java ${JAVA_OPTS} -jar /tmp/potatostudy.jar"]
