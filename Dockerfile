FROM openjdk:12-alpine

MAINTAINER register-demo

ENV SPRING_OUTPUT_ANSI_ENABLED=ALWAYS \
    JAVA_OPTS="" \
    PROFILES="default"

ADD /target/*.jar /register-demo.jar

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar /register-demo.jar --spring.profiles.active=$PROFILES"]

EXPOSE 8080