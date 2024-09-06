FROM openjdk:21-jdk AS build
WORKDIR /tmp
COPY . /tmp
RUN apt update && apt install -y findutils && \
    chmod +x ./gradlew && ./gradlew clean bootJar

FROM openjdk:21-jdk
WORKDIR /tmp
COPY --from=build /tmp/build/libs/potatoStudy_jwt-0.0.1-SNAPSHOT.jar /tmp/potatostudy.jar
ENTRYPOINT ["sh", "-c", "java ${JAVA_OPTS} -jar /tmp/potatostudy.jar"]
