FROM openjdk:11-jre-slim
ADD ./build/distributions/database.tar /
EXPOSE 6000
ENTRYPOINT ["/database/bin/database"]