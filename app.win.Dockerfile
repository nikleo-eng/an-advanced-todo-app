FROM winamd64/openjdk:11

ENV MY_SQL_HOST localhost
ENV MY_SQL_PORT 3306
ENV MY_SQL_DB_NAME an_advanced_todo_app_db
ENV MY_SQL_USER user
ENV MY_SQL_PASS password

SHELL ["powershell", "-Command", "$ErrorActionPreference = 'Stop'; $ProgressPreference = 'SilentlyContinue';"]

COPY .docker-util/docker-cmd.win.ps1 /App/docker-cmd.win.ps1
COPY /target/maven-javafx-dependencies/windows /Lib
COPY /target/*win-jar-with-dependencies.jar /App/app.jar

CMD C:\App\docker-cmd.win.ps1