FROM openjdk:11

ENV MY_SQL_HOST localhost
ENV MY_SQL_PORT 3306
ENV MY_SQL_DB_NAME an_advanced_todo_app_db
ENV MY_SQL_USER user
ENV MY_SQL_PASS password

COPY .docker-util/wait-for-it.sh /app/wait-for-it.sh
COPY .docker-util/docker-cmd.sh /app/docker-cmd.sh
COPY /target/maven-javafx-dependencies/linux /lib
COPY /target/*jar-with-dependencies.jar /app/app.jar

RUN chmod +x /app/wait-for-it.sh /app/docker-cmd.sh
RUN apt-get update && apt-get install libgtk-3-0 libglu1-mesa xvfb -y && apt-get update

RUN apt-get install dos2unix -y
RUN dos2unix /app/wait-for-it.sh
RUN dos2unix /app/docker-cmd.sh

CMD ["sh", "-c", "/app/docker-cmd.sh"]