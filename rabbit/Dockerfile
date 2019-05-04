FROM openjdk:8-jdk-alpine
VOLUME /tmp
ADD /target/rabbit-1.0-SNAPSHOT.jar rabbit.jar
ENTRYPOINT ["java","-jar","rabbit.jar", "--spring.profiles.active=docker"]