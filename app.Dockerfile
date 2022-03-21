FROM openjdk:11-jre-slim

ENV MY_SQL_HOST localhost
ENV MY_SQL_PORT 3306
ENV MY_SQL_DB_NAME an-advanced-todo-app-db
ENV MY_SQL_USER user
ENV MY_SQL_PASS password

COPY wait-for-it.sh /app/wait-for-it.sh
# https://github.com/moby/moby/issues/37965#issuecomment-426853382
RUN true
COPY docker-cmd.sh /app/docker-cmd.sh
# https://github.com/moby/moby/issues/37965#issuecomment-426853382
RUN true
COPY /target/maven-javafx-dependencies/linux /lib
# https://github.com/moby/moby/issues/37965#issuecomment-426853382
RUN true
COPY /target/*jar-with-dependencies.jar /app/app.jar

RUN chmod +x /app/wait-for-it.sh /app/docker-cmd.sh
RUN apt-get update && apt-get install libgtk-3-0 libglu1-mesa xvfb -y && apt-get update

CMD ["sh", "-c", "/app/docker-cmd.sh"]