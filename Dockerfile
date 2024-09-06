FROM openjdk:21-jdk
WORKDIR /tmp
RUN apt-get update && apt-get install -y findutils
COPY . /tmp
RUN chmod +x ./gradlew && ./gradlew clean bootJar

FROM openjdk:21-jdk
WORKDIR /tmp
COPY --from=build /tmp/build/libs/potatoStudy_jwt-0.0.1-SNAPSHOT.jar /tmp/potatostudy.jar
ENTRYPOINT ["sh", "-c", "java ${JAVA_OPTS} -jar /tmp/potatostudy.jar"]
