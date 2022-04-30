# Setting in current session UTF-8 charset encoding
$OutputEncoding = [System.Text.Encoding]::UTF8
[Console]::OutputEncoding = [System.Text.Encoding]::UTF8
chcp 932

if (-not([string]::IsNullOrEmpty(${env:MY_SQL_HOST})) -and -not([string]::IsNullOrEmpty(${env:MY_SQL_PORT}))) {
	java --module-path C:\Lib `
	--add-modules=javafx.base,javafx.controls,javafx.fxml,javafx.graphics,javafx.media,javafx.swing,javafx.web `
	-jar C:\App\app.jar --my-sql-host=${env:MY_SQL_HOST} `
	--my-sql-port=${env:MY_SQL_PORT} --my-sql-db-name=${env:MY_SQL_DB_NAME} `
	--my-sql-user=${env:MY_SQL_USER} --my-sql-pass=${env:MY_SQL_PASS}
} Else {
	'The env vars MY_SQL_HOST and MY_SQL_PORT are not correctly setted'
}