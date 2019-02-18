FROM openjdk:11-jre-slim
ADD ./build/distributions/server.tar /
EXPOSE 5501
EXPOSE 5502/udp
ENTRYPOINT ["/server/bin/server"]