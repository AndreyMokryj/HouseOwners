FROM openjdk:8-jdk-alpine
RUN apk add --no-cache curl wait4ports
VOLUME /tmp
ADD /target/server-0.0.1-SNAPSHOT.jar server.jar
ENTRYPOINT ["java","-jar","server.jar", "--spring.profiles.active=docker"]