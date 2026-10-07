FROM openjdk:8-jre-slim
RUN mkdir /log /config /app
ADD target/generate-document-v2.0.3.jar /app/generate-document-v2.0.3.jar
ADD config/ /config
ENV DEFAULT_OPTIONS="-Duser.timezone=America/Guayaquil -Djava.net.preferIPv4Stack=true -Djava.security.egd=file:/dev/./urandom"
ENV JAVA_OPTS="-Xms512m -Xmx800m"
WORKDIR /app
ENTRYPOINT java ${DEFAULT_OPTIONS} ${JAVA_OPTS} -jar /app/generate-document-v2.0.3.jar --spring.config.location=file:/config/application.properties
