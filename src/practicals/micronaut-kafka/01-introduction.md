# Introduction

In this practical, you will write a microservice that will consume the events from a simulated airport check-in area, and produce various reports about the check-in desks and the events so far.

To do so, you will use [Micronaut Kafka](https://micronaut-projects.github.io/micronaut-kafka/latest/guide/).

You will also revisit the solution from Practical 3 and rework it to fetch information in the background, to reduce the latency of requests to add books.
To do so, we will use Kafka consumers and producers to decouple the processing of book requests from the use of the external API.

## What you should already know

You should be able to:

* Implement basic Micronaut microservices that respond to HTTP requests (from Practical 1).
* Query and update a relational database within a Micronaut microservice using Micronaut Data (from Practical 2).
* Calling other microservices from your own microservice (from Practical 3).

You should be familiar with these concepts from the lectures:

* The main components in an event-driven architecture.
* The core concepts in Apache Kafka: cluster, broker, topic, partition, record, and the structure of a record (key, message body, and a timestamp).
* The challenges around achieving durable and scalable stateful event processing, and common strategies (partitioned state, re-keying).
* The use of interaction-based testing (Mockito) for checking the correct use of Kafka producers.

## What you will learn

* How to create a project that uses Micronaut Kafka.
* How to automatically create topics while starting a Micronaut microservice.
* How to implement Kafka producers and consumers using Micronaut Kafka.
* How to test Kafka producers and consumers using JUnit.

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

You will create a new Micronaut project that will consume the events from [this simulator](../../solutions/practical4-simulator.zip) (also written as a Micronaut application).

You will also revisit your solution to Practical 3 (or its [model solution](../../solutions/practical3.zip)) to move the invocations to OpenLibrary to a background process.