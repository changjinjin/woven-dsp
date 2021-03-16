FROM openjdk:8-jdk-alpine
MAINTAINER inforefiner info@inforefiner.com
EXPOSE 8003
VOLUME /baymax-dsp
ADD baymax-dsp-access-consumer-0.1.6-SNAPSHOT.jar baymax-dsp-access-consumer.jar
RUN sh -c 'touch /baymax-dsp-access-consumer.jar'
ENV PROFILES="default"
ENV JAVA_OPTS="-server -Xmx2G -Xms512m -Djava.awt.headless=true"
ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar -Dspring.profiles.active=$PROFILES /baymax-dsp-access-consumer.jar" ]