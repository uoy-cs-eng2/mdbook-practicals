# Additional exercises

Congratulations! You have completed this first practical.

If you would like to gain more practice with the basics of building REST APIs with Micronaut, here are some things you could try.

## Enforcing ID uniqueness when adding books

`POST`ing a new book does not enforce that the ID is not already in use.
Add this logic, making it so trying to `POST` a `Book` whose ID is already in use produces an HTTP 400 Bad Request response.

## Managing authors

We only track books so far, but not their authors.
Create a new `Author` DTO and its own `AuthorsController`, plus the appropriate tests.

## Relating authors and books

Extend the `Book` and `Author` DTOs so they relate to each other.
Try adding endpoints to fetch the author of a book, or the books of an author.
For instance:

* `GET /books/{id}/author`
* `GET /authors/{id}/books`

Note that if you have methods such as `Author.getBooks()` or `Book.getAuthor()`, you may need to annotate one of them with `@JsonIgnore` so they are not represented in the JSON serialisation.
This is to avoid an endless loop where serialising a book requires in serialising the author, which requires serialising their books, and so on until the server runs out of memory.

## Improving the generated OpenAPI specification

Micronaut automatically produces an OpenAPI description of your microservice, in YAML format.
To improve its quality (making it easier to understand for humans and for things like [ChatGPT function calling](https://cookbook.openai.com/examples/function_calling_with_an_openapi_spec)), you can do things like:

* Touch up the `@OpenAPIDefinition` annotation in your `Application`, as [documented by micronaut-openapi](https://micronaut-projects.github.io/micronaut-openapi/snapshot/guide/#openApiDefinition).
* Write better [Javadoc comments](https://micronaut-projects.github.io/micronaut-openapi/snapshot/guide/#controllers) for your controller methods.