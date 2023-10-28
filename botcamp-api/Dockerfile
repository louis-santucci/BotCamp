FROM openjdk:17-alpine as builder
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} botcamp-api.jar
RUN java -Djarmode=layertools -jar botcamp-api.jar extract

FROM openjdk:17-alpine
COPY --from=builder dependencies/ ./
COPY --from=builder snapshot-dependencies/ ./
COPY --from=builder spring-boot-loader/ ./
COPY --from=builder application/ ./
ENTRYPOINT ["java", "org.springframework.boot.loader.JarLauncher"]