FROM openjdk:17-jdk-buster AS builder
COPY . /src
WORKDIR /src
RUN ./gradlew build

FROM wisvch/spring-boot-base:2.5.5
COPY --from=builder /src/build/libs/feedback.jar /srv/feedback.jar
CMD ["/srv/feedback.jar"]
