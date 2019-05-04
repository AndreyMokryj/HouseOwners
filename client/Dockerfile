FROM openjdk:8-jdk-alpine
RUN apk add --no-cache curl wait4ports
VOLUME /tmp
ADD /target/client-1.0-SNAPSHOT.jar client.jar
ENTRYPOINT ["java","-jar","client.jar", "--spring.profiles.active=docker"]