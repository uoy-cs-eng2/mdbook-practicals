# Introduction

In this practical, you will work through the basics of creating a microservice using [Micronaut](https://micronaut.io/).

## What you should already know

You should be able to:

* Create your own classes in the Java programming language.
* Be familiar with inheritance and implementing interfaces.
* Add [annotations](https://dev.java/learn/annotations/) to classes, methods, parameters, and variables (e.g. `@Override` on a method).
* Use [generics](https://dev.java/learn/generics/) to parameterize types (e.g. `List<Integer>`).
* Use lists and maps from the [Java Collections Framework](https://dev.java/learn/api/collections-framework/).
* Write unit tests using [JUnit](https://junit.org/junit5/docs/current/user-guide/#writing-tests).

If you need to read up on these concepts, consult the links in the Part 1 Java knowledge map in the VLE, and check the [Learn Java](https://dev.java/learn/) section of the Dev.java website.

You should be familiar with these concepts from the lectures:

* The definition of software architecture as structure + architectural characteristics + decisions + design principles.
* The microservices architectural pattern.
* The REST principles and the 4 levels of the Richardson Maturity Model.

## What you will learn

* How to create a new Micronaut project from scratch.
* How to import the project into IntelliJ.
* How to write controllers that handle HTTP requests in [JSON](https://www.json.org/) format.
* How to produce a web-based interface to try out the controllers.
* How to write unit tests for the controllers.

## What you will need

* Java 17 or newer: install from [Adoptium](https://adoptium.net/).
* An IDE with Gradle and Java support: in this worksheet, we discuss [IntelliJ IDEA](https://www.jetbrains.com/idea/).

## What you will do

You will implement and test a minimal version of a microservice which manages a collection of books.
The microservice will be able to create, retrieve, update, and delete books.