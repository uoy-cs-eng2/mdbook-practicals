# Managing books

Of course, there wouldn't be much point in always responding with the same books: we want to be able to manage them.
To do this, we will need to add endpoints in our `BooksController` for creating, updating, and deleting books:

* `POST /books` should accept a `Book` and add it to our collection.
* `GET /books/id` should return the `Book` with that `id`.
* `PUT /books/id` should accept a `Book` and update the title and author of the book with that `id`.
* `DELETE /books/id` should delete the `Book` with that `id`.

We will keep track of the books in a Java map, so they will be lost when we restart the application.
We will cover how to store the books in a database in later practicals.

## Adding books

The declaration of the controller method for `POST /books` would look like this:

```java
@Post
public void createBook(@Body Book book) {}
```

Note how the method has a `book` parameter annotated with `@Body`: this indicates to Micronaut that the endpoint will deserialise a JSON-formattted `Book` object from the HTTP request body.
All the possible ways in which you can take parts of the HTTP request and bind them to variables are listed in the [official Micronaut documentation](https://docs.micronaut.io/4.7.11/guide/#binding).

Using the Java Collections Framework, add a map from integers to `Book` objects to your `BooksController`.
If you are unfamiliar with the JCF, consult the Java Collections Framework resources in the Java knowledge map of the VLE.

Implement the `createBook` method so it updates the map, and change the `getBooks` method so it returns the values of the map.

Try restarting the application, adding a few books through `POST /books`, and listing them through `GET /books`.

## Getting a specific book

The next method will be for retrieving a specific book:

```java
@Get("/{id}")
public Book getBook(@PathVariable int id)) {}
```

`@PathVariable` is for binding a part of the path of the URL to a variable: specifically, the ID of the book that we want to fetch.
You can implement this method by simply returning the `Book` with that `id`, or `null` if we do not have it.

Try restarting the application. Try this:

* Add a book with `POST /books`.
* Check that `GET /books/{id}` with the given `id` returns it, with an HTTP 200 OK response.
* Check that `GET /books/{id}` with an `id` that we do not have produces an HTTP 404 Not Found response.
  This is because if a controller method returns `null` instead of a DTO, Micronaut will map that to a 404 response.

## Updating a book

You should be able to write the declaration of the method for `PUT /books/{id}` yourself, based on the above examples: just use `@Put` as the annotation for the method (with the appropriate string parameter).

Follow this approach:

* Get the `Book` with the given `id` in your map. If it does not exist, respond with HTTP 404: when your method does not simply return a DTO, you can instead throw a `new HttpStatusException(HttpStatus.NOT_FOUND, message)`, where the `message` is up to you.
* Update the title of the book in your map.
* Update the author of the book in your map.

Note how this HTTP endpoint does not use the `id` inside the `Book` object sent in the request, but instead uses the `id` in the URL.
In fact, for update endpoints like these we would normally use a dedicated `BookUpdateDTO` that would not list `id` as a valid field.

Try it out in the Swagger UI before moving on.

## Deleting a book

You should be able to write this method entirely yourself from the above examples, using the `@Delete` annotation on the new controller method, together with the appropriate string parameter.
