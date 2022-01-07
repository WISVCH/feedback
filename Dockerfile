FROM wisvch/openjdk:11-jdk AS builder
COPY . /src
WORKDIR /src
RUN ./gradlew build

FROM wisvch/spring-boot-base:2.1
COPY --from=builder /src/build/libs/feedback-tool.jar /srv/feedback-tool.jar
COPY ./build/libs/feedback-tool.jar /srv/feedback-tool.jar
CMD ["/srv/feedback-tool.jar"]
