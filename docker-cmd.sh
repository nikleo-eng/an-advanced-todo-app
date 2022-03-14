#!/bin/sh

# Abort on any error (including if wait-for-it fails).
set -e

# Wait for the dbms to be up, if we know where it is.
if [ -n "${MY_SQL_HOST}" ] && [ -n "${MY_SQL_PORT}" ]; then
  /app/wait-for-it.sh ${MY_SQL_HOST}:${MY_SQL_PORT} --strict --timeout=30 \
  -- xvfb-run java -Dprism.verbose=true --module-path /lib \
  --add-modules=javafx.base,javafx.controls,javafx.fxml,javafx.graphics,javafx.media,javafx.swing,javafx.web \
  -jar /app/app.jar --my-sql-host=${MY_SQL_HOST} \
  --my-sql-port=${MY_SQL_PORT} --my-sql-db-name=${MY_SQL_DB_NAME} \
  --my-sql-user=${MY_SQL_USER} --my-sql-pass=${MY_SQL_PASS}
else
  echo "The env vars MY_SQL_HOST and MY_SQL_PORT are not correctly setted"
fi

# Continue the main container command.
exec "$@"