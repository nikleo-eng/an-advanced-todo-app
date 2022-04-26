$command=""
$command="${command}CREATE USER 'root'@'%' IDENTIFIED BY '${env:MYSQL_ROOT_PASSWORD}';"
$command="${command}GRANT ALL ON *.* TO 'root'@'%';"
$command="${command}CREATE USER '${env:MYSQL_USER}'@'%' IDENTIFIED BY '${env:MYSQL_PASSWORD}';"
$command="${command}CREATE DATABASE ${env:MYSQL_DATABASE};"
$command="${command}GRANT ALL ON ${env:MYSQL_DATABASE}.* TO '${env:MYSQL_USER}'@'%';"

mysql -u root -e $command