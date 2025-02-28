# Introduction

In this practical, you will extend the book-related microservice by adding an integration with the [OpenLibrary APIs](https://openlibrary.org/developers/api) to fetch more information about the books that are added to the database.

This fetching will take place in the background, to avoid increasing the latency of requests to add books.
To do so, we will use Kafka consumers and producers to decouple the processing of book requests from the use of the external API.

## What you should already know

You should be able to:

* Implement basic Micronaut microservices that respond to HTTP requests (from Practical 1).
* Query and update a relational database within a Micronaut microservice using Micronaut Data (from Practical 2).
* Write Kafka consumers and producers (from Practical 3).
* Use interaction-based testing (e.g. Mockito) (from Practical 3).

You should be familiar with these concepts from the Week 4 lectures:

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

## What you will do

You will start from a solution of Practical 2 (either yours, or [our model solution](../../solutions/practical2.zip)) and work through the rest of the sections.
