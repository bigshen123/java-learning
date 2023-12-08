FROM git.koal.com:4567/gw-cloud/docker/java-runtime:openjdk-8-kssl-1.1.1d-skywalking-8.3.0-amd64

COPY buildDockerDir /

RUN cp /application.yml /conf/application.yml && chmod +x /app.sh

ENTRYPOINT ["/app.sh"]