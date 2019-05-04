FROM openjdk:8-jdk-alpine
RUN apk add --no-cache curl wait4ports
VOLUME /tmp
ADD /target/discovery-1.0-SNAPSHOT.jar discovery.jar
ENTRYPOINT ["java","-jar","discovery.jar", "--spring.profiles.active=docker"]