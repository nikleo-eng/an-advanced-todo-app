# An Advanced ToDo App
A project for the Advanced Programming Techniques course

[![An-Advanced-ToDo-App CI/CD in MacOS](https://github.com/nikleo-eng/an-advanced-todo-app/actions/workflows/mac-os.yml/badge.svg)](https://github.com/nikleo-eng/an-advanced-todo-app/actions/workflows/mac-os.yml)
[![An-Advanced-ToDo-App CI/CD in Ubuntu](https://github.com/nikleo-eng/an-advanced-todo-app/actions/workflows/ubuntu.yml/badge.svg)](https://github.com/nikleo-eng/an-advanced-todo-app/actions/workflows/ubuntu.yml)
[![An-Advanced-ToDo-App CI/CD in Windows](https://github.com/nikleo-eng/an-advanced-todo-app/actions/workflows/windows.yml/badge.svg)](https://github.com/nikleo-eng/an-advanced-todo-app/actions/workflows/windows.yml)

[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=nikleo-eng_an-advanced-todo-app&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=nikleo-eng_an-advanced-todo-app)
[![Technical Debt](https://sonarcloud.io/api/project_badges/measure?project=nikleo-eng_an-advanced-todo-app&metric=sqale_index)](https://sonarcloud.io/summary/new_code?id=nikleo-eng_an-advanced-todo-app)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=nikleo-eng_an-advanced-todo-app&metric=coverage)](https://sonarcloud.io/summary/new_code?id=nikleo-eng_an-advanced-todo-app)
[![Code Smells](https://sonarcloud.io/api/project_badges/measure?project=nikleo-eng_an-advanced-todo-app&metric=code_smells)](https://sonarcloud.io/summary/new_code?id=nikleo-eng_an-advanced-todo-app)
[![Maintainability Rating](https://sonarcloud.io/api/project_badges/measure?project=nikleo-eng_an-advanced-todo-app&metric=sqale_rating)](https://sonarcloud.io/summary/new_code?id=nikleo-eng_an-advanced-todo-app)
[![Security Rating](https://sonarcloud.io/api/project_badges/measure?project=nikleo-eng_an-advanced-todo-app&metric=security_rating)](https://sonarcloud.io/summary/new_code?id=nikleo-eng_an-advanced-todo-app)
[![Bugs](https://sonarcloud.io/api/project_badges/measure?project=nikleo-eng_an-advanced-todo-app&metric=bugs)](https://sonarcloud.io/summary/new_code?id=nikleo-eng_an-advanced-todo-app)
[![Vulnerabilities](https://sonarcloud.io/api/project_badges/measure?project=nikleo-eng_an-advanced-todo-app&metric=vulnerabilities)](https://sonarcloud.io/summary/new_code?id=nikleo-eng_an-advanced-todo-app)
[![Duplicated Lines (%)](https://sonarcloud.io/api/project_badges/measure?project=nikleo-eng_an-advanced-todo-app&metric=duplicated_lines_density)](https://sonarcloud.io/summary/new_code?id=nikleo-eng_an-advanced-todo-app)
[![Reliability Rating](https://sonarcloud.io/api/project_badges/measure?project=nikleo-eng_an-advanced-todo-app&metric=reliability_rating)](https://sonarcloud.io/summary/new_code?id=nikleo-eng_an-advanced-todo-app)

## Description
An example of an *Eclipse MVC Maven* project; the project is built with *GitHub Actions* (on Ubuntu, MacOS and Windows); the *Repository components* use *MySQL* as target DBMS (an RDBMS): the project can be extended to connect to another RDBMS or even to a NoSQL DBMS, like MongoDB; *JavaFX* is at the base of the *UI components*: the project can also use an other GUI framework, like Swing, or even a CLI; the project contains *Unit, UI, IT, E2E and BDD Tests* (with *TestFX* and *Cucumber*); the project uses *Guice* as *DI framework* ; the project is analyzable with *SonarQube* and it is analyzed with *SonarCloud* from GitHub Actions; for the project's tests' execution, an empty *dockerized image of a MySQL instance* is provided (both for Linux and Windows containers); for academic purposes, also a *Docker Compose* example (with two services and a network) is provided (both for Linux and Windows containers): the example can be used as an E2E test for assuring that the builded, dockerized and running Java application can connect to a dockerized MySQL instance; the code coverage is calculated with *JaCoCo*, while the reliability of the *Controller components* is guaranteed with *Mutation Tests* (with *PIT*); finally, a *Maven wrapper* has been added.

## Features
An easy way to manage series of todos:
* the todos are grouped by list
* the lists are grouped by user

### Users
The application allows:
* the registration of a new user
* the login with an existing user
* the retrieve of the last login infos
* the logout of the current user

### Lists
Logged in with an existing user, the application allows:
* the retrieve of its lists
* the creation of a new list
* the deletion of an existing list
* the modification of an existing list

### Todos
Selected an existing list, the application allows:
* the retrieve of its todos
* the creation of a new todo
* the deletion of an existing todo
* the modification of an existing todo
* the setting as done/undone a todo

## Required Dependencies
In order to functionally test or run in Production mode this project, only a *JRE of JavaSE-11* is needed (make sure that *JAVA_HOME* is setted between the environment variables).

In order to contribute to or use in Development mode this project, the JRE is not enough, a complete *JDK of JavaSE-11* has to be available; also an installation of *Eclipse IDE for Enterprise Java and Web Developers* (with *EclEmma Java Code Coverage*, *Cucumber Eclipse Plugin*, *Pitclipse*, *Eclipse Docker Tooling*, *ShellWax* and *Yaml Editor* as recommended tools/add-ins and the JDK setted as Installed JRE in the preferences) and *Docker Desktop App* (with *mysql:8.0.28*, *openjdk:11* and *sonarqube:latest* as recommended available containers) are necessary; it is recommended to have the sources of a version of *Apache Maven* (3.8.4 is the guaranteed worked version) - and the reference to its binaries's directory between the environment variables - even if an alternative *Apache Maven Wrapper* is provided within the project.

## Installation
For functional tests or Production, you have to download the asset of the target operating system from the [latest release](https://github.com/nikleo-eng/an-advanced-todo-app/releases/latest) - for example for Windows the ZIP file *an-advanced-todo-app-{version}-win.zip* (where *{version}* is the target app version) has to be downloaded.

For contributing or setting up the Development environment, it is sufficient to fork-clone this Github repo or to download the [ZIP file](https://github.com/nikleo-eng/an-advanced-todo-app/archive/refs/heads/master.zip) of the updated master branch of this project.

## Usage
To run the application, extract in a directory the contents of the asset downloaded, open a command line prompt, change working directory to the directory with extracted contents and execute this command (the command shown is for Windows environments so it has to be adjusted for Linux or MacOS environments):

```powershell
java --module-path .\maven-javafx-dependencies\{operating system} --add-modules=javafx.base,javafx.controls,javafx.fxml,javafx.graphics,javafx.media,javafx.swing,javafx.web -jar .\an-advanced-todo-app-{version}-{operating_system}-jar-with-dependencies.jar --my-sql-host={my_sql_host} --my-sql-port={my_sql_port} --my-sql-db-name={my_sql_db_name} --my-sql-user={my_sql_user} --my-sql-pass={my_sql_pass}
```

where (some substitutions have to be made)
* *{operating system}* is the working operating system: are supported the values *win*, *linux*, *mac*;
* *{version}* is the current app version: for example *0.0.1-SNAPSHOT*;
* *{my_sql_host}* is the host of an instance of working MySql DBMS: for example *localhost*;
* *{my_sql_port}* is the port of the target MySql instance: for example *3306*;
* *{my_sql_db_name}* is the name of a database of the target MySql instance: for example *an_advanced_todo_app_db*;
* *{my_sql_user}* is an allowed user (for executing DDL and DML instructions) of the target database: for example *user*;
* *{my_sql_pass}* is the password of the target user: for example *password*.

It is recommended to create and then use in the command above some environments variables for storing and sharing the values of the MySql instance informations, like users and relative passwords.

In a Development environment with the Docker Engine up, to build the application (verifying that meets all the Unit, UI, Integration, E2E and BDD Tests and the quality criteria of PITest and Jacoco) from a command line prompt, with the root directory of the cloned or downloaded Eclipse project as working directory, execute the next command (as above, it is a command for Windows environments):

```powershell
mvn clean verify -P jacoco,mutation-testing,{docker_profile} sonar:sonar "-Dsonar.host.url={sonar_host_url}"
```

where (some substitutions have to be made)
* *{docker_profile}* is the configured Maven profile for using Docker Engine and its containers from the *package* to *verify* phase: the values allowed are *docker*, if the Docker Engine is configured to use the Linux Containers, and *docker-win*, if the Docker Engine is configured to use the alternative Windows Containers (usable only in Windows environments);
* *{sonar_host_url}* is the url of a working instance of SonarQube that doesn't require the authentication to push a project, or its updates, to its workspace: for example, if in the local machine, thanks to Docker Engine, is up a SonarQube containerized instance with 9000 as mapped port, the url is *http://localhost:9000*.

Always in the same Development environment, to execute only the Unit and UI Tests of the application, having the same workind directory, execute the next simple command:

```powershell
mvn clean test
```

where no substitution is required.

In the commands above that use Maven, if no local source of Maven is present, substitute *mvn* with *.\mvnw* for using the Maven Wrapper of the project (this is the substitution for Windows environments, so it has to be adjusted for different environments).

In the Linux distributions, like Ubuntu, for executing all the commands above, it is necessary to open a virtual display: it is enough to prepend *xvfb-run* to the command; for example, to run the application the right command is:

```sh
xvfb-run java --module-path ./maven-javafx-dependencies/linux --add-modules=javafx.base,javafx.controls,javafx.fxml,javafx.graphics,javafx.media,javafx.swing,javafx.web -jar ./an-advanced-todo-app-{version}-linux-jar-with-dependencies.jar --my-sql-host={my_sql_host} --my-sql-port={my_sql_port} --my-sql-db-name={my_sql_db_name} --my-sql-user={my_sql_user} --my-sql-pass={my_sql_pass}
```

In the Linux and MacOS environments, it is possible to build and test the application in the Headless mode, without using a virtual display and visualizing the running tests, that include checks on the JavaFX module; to do so, add the next Java Options to the relative command: *-Djava.awt.headless=true -Dtestfx.robot=glass -Dtestfx.headless=true -Dprism.order=sw*; for example the build command becomes:

```sh
mvn clean verify -P jacoco,mutation-testing,docker sonar:sonar -Dsonar.host.url={sonar_host_url} -Djava.awt.headless=true -Dtestfx.robot=glass -Dtestfx.headless=true -Dprism.order=sw
```