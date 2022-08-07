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