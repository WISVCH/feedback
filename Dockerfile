FROM wisvch/spring-boot-base:2.1
COPY ./build/libs/feedback-tool.jar /srv/feedback-tool.jar
CMD ["/srv/feedback-tool.jar"]
