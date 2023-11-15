FROM openjdk:17-alpine as builder
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} gmail-gateway-api.jar
RUN java -Djarmode=layertools -jar gmail-gateway-api.jar extract
COPY ./startup.sh .

ENV WAIT_VERSION 2.7.2
ADD https://github.com/ufoscout/docker-compose-wait/releases/download/$WAIT_VERSION/wait /wait
RUN chmod +x /wait

FROM openjdk:17-alpine
COPY --from=builder dependencies/ ./
COPY --from=builder snapshot-dependencies/ ./
COPY --from=builder spring-boot-loader/ ./
COPY --from=builder application/ ./
COPY --from=builder ./startup.sh ./
COPY --from=builder ./wait ./

RUN chmod +x startup.sh

ENTRYPOINT ["./startup.sh"]