FROM maven:3.8.4-jdk-11-slim
ENV HOME /home/app
COPY src $HOME/src
COPY pom.xml $HOME
RUN --mount=type=cache,target=/root/.m2 mvn -f $HOME/pom.xml clean compile && cp -r /root/.m2 /home
WORKDIR /home/app
RUN echo "if [ ! -d "/root/.m2" ]; then mv /home/.m2 /root/; fi &&  \
    mvn clean test -Dtarget.url=\$TARGET_URL" > /home/app/entrypoint.sh
ENTRYPOINT ["sh", "/home/app/entrypoint.sh"]