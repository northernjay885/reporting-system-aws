FROM openjdk:11.0.4-jre-slim as builder
WORKDIR application
ADD maven/${project.build.finalName}.jar ${project.build.finalName}.jar
RUN java -Djarmode=layertools -jar ${project.build.finalName}.jar extract

FROM openjdk:11.0.4-jre-slim
LABEL PROJECT_NAME=${project.artifactId} \
      PROJECT=${project.id}

EXPOSE 8080

WORKDIR application
COPY --from=builder application/dependencies/ ./
COPY --from=builder application/spring-boot-loader/ ./
COPY --from=builder application/snapshot-dependencies/ ./
COPY --from=builder application/application/ ./
## Add the wait script to the image
ADD https://github.com/ufoscout/docker-compose-wait/releases/download/2.8.0/wait /wait
RUN chmod +x /wait
## Launch the wait tool and then your application

ENTRYPOINT ["/bin/sh", "-c", "/wait && java -Djava.security.egd=file:/dev./urandom org.springframework.boot.loader.JarLauncher"]
