# Introduction

In this practical, you will extend the book-related microservice by adding an integration with the [OpenLibrary APIs](https://openlibrary.org/developers/api) to fetch more information about the books that are added to the database.

## What you should already know

You should be able to:

* Implement basic Micronaut microservices that respond to HTTP requests (from Practical 1).
* Query and update a relational database within a Micronaut microservice using Micronaut Data (from Practical 2).

You should be familiar with these concepts from the lectures:

* The differences between in-process and inter-process communication.
* How the Gateway pattern helps encapsulate the details of interacting with an external service.
* The main elements in an OpenAPI specification, and its use for generating clients.

## What you will learn

* How to use the Micronaut OpenAPI Gradle plugin to generate a client from an existing specification.
* How to develop and test a Gateway for a specific external service (the OpenLibrary API).
* How to integrate the Gateway for improving the information you have about a book in the background.

## What you will need

* Java 17 or newer: install from [Adoptium](https://adoptium.net/).
* An IDE with Gradle and Java support: in this worksheet, we discuss [IntelliJ IDEA](https://www.jetbrains.com/idea/).
* A local installation of [Docker Desktop](https://www.docker.com/get-started/).
  Ensure Docker Desktop is running before you start this practical.
  *  Note: if using a lab machine, you will need to boot Windows as Docker
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

You will start from a solution of Practical 2 (either yours, or [our model solution](../../solutions/practical2.zip)) and work through the rest of the sections.
