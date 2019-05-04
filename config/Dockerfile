FROM openjdk:8-jdk-alpine
VOLUME /tmp
RUN apk add curl
ADD /target/config-1.0-SNAPSHOT.jar config-1.0-SNAPSHOT.jar
ENTRYPOINT ["java", "-Xmx800m","-jar","config-1.0-SNAPSHOT.jar", "--spring.profiles.active=docker"]