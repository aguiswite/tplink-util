FROM openjdk:17.0.1
MAINTAINER guiswitehome.com
COPY ./tplink-util-0.0.1-SNAPSHOT.jar tplink-util-0.0.1.jar
ENTRYPOINT ["java","-jar","/tplink-util-0.0.1.jar"]