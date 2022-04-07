FROM mcr.microsoft.com/windows/servercore:ltsc2019-amd64

ENV MYSQL_ROOT_PASSWORD passwordR
ENV MYSQL_DATABASE an_advanced_todo_app_db
ENV MYSQL_USER user
ENV MYSQL_PASSWORD password

SHELL ["powershell", "-Command", "$ErrorActionPreference = 'Stop'; $ProgressPreference = 'SilentlyContinue';"]

ENV chocolateyUseWindowsCompression false

RUN Set-ExecutionPolicy Bypass -Scope Process -Force; \
	[System.Net.ServicePointManager]::SecurityProtocol = [System.Net.ServicePointManager]::SecurityProtocol -bor 3072; \
	iex ((New-Object System.Net.WebClient).DownloadString('https://community.chocolatey.org/install.ps1'))
	
RUN choco feature disable --name showDownloadProgress; \
    choco feature enable --name allowGlobalConfirmation; \
    choco install -y mysql --version=8.0.28

COPY initialize-mysql.ps1 /App/initialize-mysql.ps1

RUN C:\App\initialize-mysql.ps1

EXPOSE 3306

CMD ["ping", "localhost", "-t"]