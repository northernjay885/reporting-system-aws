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
RUN apt-get update; apt-get install -y fontconfig libfreetype6

#Add non-free repositories to get MS fonts

RUN apt-get update
RUN apt-get install -y --no-install-recommends software-properties-common
RUN apt-add-repository contrib
RUN apt-get update

# If you want to use Microsoft fonts in reports, you must install the fonts
# Andale Mono, Arial Black, Arial, Comic Sans MS, Courier New, Georgia, Impact,
# Times New Roman, Trebuchet, Verdana,Webdings)
RUN echo "ttf-mscorefonts-installer msttcorefonts/accepted-mscorefonts-eula select true" | debconf-set-selections
RUN apt-get install -y --no-install-recommends fontconfig ttf-mscorefonts-installer
RUN fc-cache -f -v


## Add the wait script to the image
ADD https://github.com/ufoscout/docker-compose-wait/releases/download/2.8.0/wait /wait
RUN chmod +x /wait
## Launch the wait tool and then your application

ENTRYPOINT ["/bin/sh", "-c", "/wait && java -Djava.security.egd=file:/dev./urandom org.springframework.boot.loader.JarLauncher"]
