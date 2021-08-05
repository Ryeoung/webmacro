FROM openjdk:8-jdk-alpine
RUN apk update && \
    apk add --no-cache tzdata && \
	cp /usr/share/zoneinfo/Asia/Seoul /etc/localtime && \
	echo "Asia/Seoul" > /etc/timezone
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
