FROM mysql:8.0.28

ENV MYSQL_ROOT_PASSWORD passwordR
ENV MYSQL_DATABASE an-advanced-todo-app-db
ENV MYSQL_USER user
ENV MYSQL_PASSWORD password

EXPOSE 3306