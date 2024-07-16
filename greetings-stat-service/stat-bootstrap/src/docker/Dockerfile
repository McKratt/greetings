FROM eclipse-temurin:22-jre-alpine AS builder
RUN mkdir /opt/app
ARG JAR_FILE=target/*-exec.jar
COPY ${JAR_FILE} /opt/app/application.jar
WORKDIR /opt/app
RUN java -Djarmode=layertools -jar application.jar extract

FROM eclipse-temurin:22-jre-alpine
ENV JAVA_OPTS=""

WORKDIR /opt/app
COPY --from=builder /opt/app/dependencies/ ./
COPY --from=builder /opt/app/spring-boot-loader/ ./
COPY --from=builder /opt/app/application/ ./
ENTRYPOINT ["java", "org.springframework.boot.loader.launch.JarLauncher"]
CMD ${JAVA_OPTS}
