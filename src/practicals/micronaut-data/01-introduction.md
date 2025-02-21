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
  *  Note: if using a lab machine, you will need to boot Windows as Docker
     Desktop is not installed in the Linux image.

## What you will do

You will extend a solution of Practical 1 to use a database for persisting its data.
This may be your own solution, or the model solution in the VLE.
