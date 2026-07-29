FROM eclipse-temurin:25-jdk-alpine-3.23 AS builder
RUN mkdir /opt/app
ARG JAR_FILE=target/*-exec.jar
COPY ${JAR_FILE} /opt/app/application.jar
WORKDIR /opt/app
RUN java -Djarmode=tools -jar application.jar extract --layers --destination /opt/app/extract

FROM eclipse-temurin:25-jre-alpine-3.23
ENV JAVA_OPTS=""

WORKDIR /opt/app
COPY --from=builder /opt/app/extract/dependencies/ ./
COPY --from=builder /opt/app/extract/spring-boot-loader/ ./
COPY --from=builder /opt/app/extract/application/ ./

ENTRYPOINT ["java", "org.springframework.boot.loader.launch.JarLauncher"]
CMD ${JAVA_OPTS}
