# Introduction

In this practical, you will deploy the book-related microservice as an orchestration of Docker containers.
[Micronaut Test Resources](https://micronaut-projects.github.io/micronaut-test-resources/latest/guide/) is great for local development, but it is not designed for deployment.

You will write a [Compose file](https://docs.docker.com/reference/compose-file/) that includes your databases, your Kafka cluster, and your microservices.
Then, you will run it via [Docker Compose](https://docs.docker.com/compose/).

However, before we do that, we will write some end-to-end tests for the integration between the microservice, the database, and the Kafka cluster.
This will help us test that the container orchestration is working as intended.

## What you should already know

You should be able to:

* Generate clients from OpenAPI specifications (from Practical 4).

You should be familiar with these concepts from the Week 4 lectures:

* How to write test assertions about the expected eventual state of the system using Awaitility.

You should be familiar with these concepts from the Week 5 lectures:

* The differences between containerisation and virtualisation.
* The basic syntax of a `Dockerfile`, and how Docker images are structured in layers.
* The core elements of a Compose file, and the differences between bind mounts and volumes.

## What you will learn

* How to use clients generated from OpenAPI specifications for end-to-end testing.
* How to write a Compose file that orchestrates several Micronaut applications into a single deployable unit.
* How to test that the container orchestration is working as intended via end-to-end tests.

## What you will need

* Java 17 or newer: install from [Adoptium](https://adoptium.net/).
* An IDE with Gradle and Java support: in this worksheet, we discuss [IntelliJ IDEA](https://www.jetbrains.com/idea/).
* A local installation of [Docker Desktop](https://www.docker.com/get-started/).
  Ensure Docker Desktop is running before you start this practical.
  *  Note: if using a lab machine, you will need to boot Windows as Docker
     Desktop is not installed in the Linux image.

## What you will do

You will start from a solution of Practical 4 (either yours, or [our model solution](../../solutions/practical3.zip)) and work through the rest of the sections.
