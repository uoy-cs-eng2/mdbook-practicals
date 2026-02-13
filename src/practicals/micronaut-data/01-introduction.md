# Introduction

In this practical, you will take the microservice from Practical 1 and change it to use a database with [Micronaut Data](https://micronaut-projects.github.io/micronaut-data/latest/guide).

## What you should already know

You should be able to:

* Implement basic Micronaut microservices that respond to HTTP requests (from Practical 1).

You should be familiar with these concepts from the lectures:

* The distinction between compute-intensive and data-intensive systems.
* The challenges related to the object/relational impedance mismatch.
* The differences between JDBC database drivers, ORM libraries, and Micronaut Data itself.
* The JPA annotations for persistent fields, one-to-many, many-to-one, and many-to-many relationships.
* The conventions followed by the methods in Micronaut Data `@Repository` interfaces.
* The use of `@Transactional` to wrap invocations of controller methods in transactions.

## What you will learn

* How to add Micronaut Data and Micronaut Flyway to an existing project.
* How to map a database schema to JPA annotations.
* How to query and update a database via Micronaut Data repositories.
* How to integrate the database into the tests of Micronaut microservices.

## What you will need

* Java 17 or newer: install from [Adoptium](https://adoptium.net/).
* An IDE with Gradle and Java support: in this worksheet, we discuss [IntelliJ IDEA](https://www.jetbrains.com/idea/).
* A local installation of [Docker Desktop](https://www.docker.com/get-started/).
  Ensure Docker Desktop is running before you start this practical.
  * Note: if using a lab machine, you will need to boot Windows as Docker
    Desktop is not installed in the Linux image.

If you have a recent installation of Docker Desktop (using Docker Engine 29 or newer), you will need to tell the Docker Java libraries to use version 1.44 of the Docker API, until [this issue in Micronaut](https://github.com/micronaut-projects/micronaut-test-resources/issues/941) is fixed.
From a Linux/Mac terminal, or from Git Bash in Windows, run this command:

```shell
echo api.version=1.44 > $HOME/.docker-java.properties
```

If you do not have Git Bash on Windows, you can run this from PowerShell instead:

```
"api.version=1.44" | set-content $HOME/.docker-java.properties -Encoding Ascii
```

## What you will do

You will extend a solution of Practical 1 to use a database for persisting its data.
This may be your own solution, or the model solution in the VLE.
