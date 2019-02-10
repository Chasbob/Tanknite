FROM openjdk:11-jre-slim
ADD ./build/distributions/client.tar /
EXPOSE 5501
ENTRYPOINT ["/client/bin/client"]