FROM openjdk:8-jdk-alpine
MAINTAINER inforefiner info@inforefiner.com
EXPOSE 8004
VOLUME /baymax-dsp
ADD baymax-security-oauth-server-0.1.6-SNAPSHOT.jar baymax-security-oauth-server.jar
RUN sh -c 'touch /baymax-security-oauth-server.jar'
ENV PROFILES="default"
ENV JAVA_OPTS="-server -Xmx2G -Xms512m -Djava.awt.headless=true"
ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar -Dspring.profiles.active=$PROFILES /baymax-security-oauth-server.jar" ]