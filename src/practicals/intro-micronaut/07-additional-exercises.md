# Additional exercises

Congratulations! You have completed this first practical.

If you would like to gain more practice with the basics of building REST APIs with Micronaut, here are some things you could try.

For the harder exercises, we have an [expanded model solution](../../solutions/practical1-additional.zip) that covers some of them, in case you get stuck.

## Enforcing ID uniqueness when adding books (easy)

`POST`ing a new book does not enforce that the ID is not already in use.
Add this logic, making it so trying to `POST` a `Book` whose ID is already in use produces an HTTP 400 Bad Request response.

## Creating a DTO specific for updating (easy)

We use the same `Book` DTO for updating books, which allows for specifying an `id` that is not used.

Change the updating of books to use a dedicated `BookUpdateDTO` which only allows for `title` and `author` to be used.
This will make our expectations clearer to clients that use the Swagger UI or our generated OpenAPI definitions.

## Managing authors (easy)

We only track books so far, but not their authors.
Create a new `Author` DTO and its own `AuthorsController`, plus the appropriate tests.

## Relating authors and books (hard)

Extend `Book` and `Author` so they relate to each other: specifically, let's assume that "a Book has at most one Author" and "an Author has zero or more Books".
You will usually want to add a `Set<Book> books` field to `Author`, and an `Author author` field to `Book`.

Add endpoints to fetch the author of a book and the books of an author, and update existing endpoints to maintain their relationships.
We suggest these:

* `GET /books/{id}/author`: gets the author of the given book.
* `POST /books`: if the book specifies an author, make sure the author has been previously created, and add the book to the author's set.
* `PUT /books/{id}`: if the updated book specifies an author, make sure the author has been previously created, and add the book to the author's list. In addition, if the book had a different author before, remove the book from the old author's set.
* `GET /authors/{id}/books`: gets the books of the given author.

You will need to annotate the `books` of an `Author` with `@JsonIgnore`, so they are not represented in the JSON version of an `Author`.
This is to avoid an endless loop where serialising an author serialises their books, which then serialise their author again.

You will need to refactor your code so the `BooksController` can check for the authors created so far.
There are several options:

* Use public static fields for the maps that hold the authors and the books. These are essentially mutable global variables now (which make testing and debugging harder).
* Add an `@Inject AuthorsController authorsController` field to `BooksController`, so it can ask the other controller for authors.
* Move both maps to a new `ApplicationState` class annotated as a `@Singleton`, and then `@Inject` it into both controllers.

You may notice that we have limited updating the Author-Book relationship to the Book side: this is to keep things simpler.
In Practical 2, we will see this is a common practice when using databases as well: we would say that Book is the "owning" side of the Author-Book relationship as that is the side we make changes from.

## Improving the generated OpenAPI specification (easy)

Micronaut automatically produces an OpenAPI description of your microservice, in YAML format.
To improve its quality (making it easier to understand for humans and for things like [ChatGPT function calling](https://cookbook.openai.com/examples/function_calling_with_an_openapi_spec)), you can do things like:

* Touch up the `@OpenAPIDefinition` annotation in your `Application`, as [documented by micronaut-openapi](https://micronaut-projects.github.io/micronaut-openapi/snapshot/guide/#openApiDefinition).
* Write better [Javadoc comments](https://micronaut-projects.github.io/micronaut-openapi/snapshot/guide/#controllers) for your controller methods.
