FROM openjdk:11-jre-slim
ADD ./build/distributions/server.tar /
EXPOSE 5501
ENTRYPOINT ["/server/bin/server"]