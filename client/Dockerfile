FROM openjdk:11-jre-slim
ADD ./build/distributions/client.tar /
EXPOSE 5501
EXPOSE 5502/udp
ENTRYPOINT ["/client/bin/client"]