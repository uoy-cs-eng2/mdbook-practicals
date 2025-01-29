# Sending Book objects

Rather than plain strings, we would like to send and receive all the information about a book in one go.
To do this, we will create a Data Transfer Object (DTO) class, which will be automatically turned to JSON by Micronaut so long as we annotate it as `@Serdeable` and follow certain conventions.

## Writing the Book DTO

*Note*: the name `Serdeable` comes from "serialisable + deserialisable".
Serialisation is the process of turning an in-memory object into a stream of bytes that you can send over the network or save into a file (e.g. by representing it as JSON).
Deserialisation is the reverse process of reading a stream of bytes and turning it into an in-memory object.

Create a new `dto` subpackage inside `uk.ac.york.cs.eng2.lab1.books`, and create a `Book` class inside it.
Annotate it with `@Serdeable`, to indicate to Micronaut that you want to serialise and deserialise it to/from JSON:

```java
@Serdeable
class Book {
}
```

For this example, let's track the title and the full name of the author, and an integer `id`.
We will need to define them as *properties* to be serialised and deserialised.
There are several ways to do it (see the [docs](https://micronaut-projects.github.io/micronaut-serialization/latest/guide/)).
For this practical, we will use a pair of getter and setter methods.

For instance, to track the `title`:

* Add a `private String title` field to `Book`.
* Add a `getTitle()` method that returns the value of the `title` field.
* Add a `setTitle(String newTitle)` method that changes the value of the `title` field.

Do the same for the `author` and the `id`.

*Note*: in IntelliJ, you can usually write the two fields yourself, and then use "Code - Generate" to have it producer the getter and setter methods.

## Using the Book DTO

Go back to our `BooksController`.
Rename the `getTitles()` method to `getBooks()`, change the `@Get("/titles")` to just `@Get`, and have it return a `List<Book>`.
Change the code so that it creates a few `Book` objects instead of just using strings.

Once you're done with the changes, run the application as before, and try out the endpoint through the [Swagger UI](http://localhost:8080/swagger-ui).
If you expand `GET /books` and scroll down to the "Example Value", you'll notice that it now shows a JSON object:

```json
[
  {
    "id": 0,
    "title": "string",
    "author": "string"
  }
]
```

Click on "Try it out" and then "Execute" to run your endpoint and check that it works as intended.
You should get an HTTP 200 OK response like this one:

```json
[
  {
    "id": 1,
    "title": "Title 1",
    "author": "Author 1"
  },
  {
    "id": 2,
    "title": "Title 2",
    "author": "Author 2"
  }
]```
