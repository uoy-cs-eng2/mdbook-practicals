# Integrating the gateway

Now that we have the gateway and a test for it, let's integrate it into the application.

In this practical, when someone adds a book with an ISBN, we will immediately request the additional information from OpenLibrary and use it.

This simple approach has its advantages and disadvantages:

* Good: it's the simplest to implement.
* Good: the book record will be complete by the time we respond to the addition of a book.
* Bad: it adds latency to the processing of the request.
* Bad: OpenLibrary may be down while someone is trying to add a book.
  If we are not careful, users may see error messages while trying to add books just because of that.

In the next practical, we will show you how to postpone this work to a background process, to reduce latency.

## Adding ISBNs to books

Create a database migration script called `V4__add-isbn.sql`, with this content:

```sql
alter table book
add isbn varchar(13);
```

Add the `isbn` field to your entities and DTOs as needed, and update any controllers and their tests accordingly (e.g. setting the ISBN while creating and updating a `Book`).

## Using the gateway from the controller

Inject a `BookCatalogGateway` into your `BooksController`.

Update the controller so that if a publisher is not explicitly mentioned but an ISBN is provided, the controller will try to use the gateway to obtain the names of the publishers.

If one or more publishers are found, the controller should try to reuse the `Publisher` in the database whose name matches the first publisher mentioned by OpenLibrary, or create a new one if it does not.

## Testing the controller's use of the gateway

You will need to update the `BooksController` tests to cover the use of the gateway.

To avoid having the unit tests depend on an external service, use a method annotated with `@MockBean` to return a Test Double of the connection object used by the gateway (the `BooksApi`), which always provides the same response.

You can use Mockito to quickly implement that Test Double, instead of having to write a full implementation of the interface.
Remember to add Mockito to your `build.gradle` first, [as we did on Practical 3](../micronaut-kafka/06-stats-per-minute.md#testing-the-re-keying-via-mockito).

For our case, it could look like this:

```java
BooksApi mock = mock(BooksApi.class);
when(mock.readIsbnIsbnIsbnGet(any())).thenReturn(
  Map.of("publishers", Collections.singletonList("P Ublisher"))
);
```

The `any()` call uses Mockito's argument matchers to indicate that the mock will always return the same predefined response for any ISBN.

Once your tests pass again, continue.

## Trying the gateway from the Swagger UI

Start the application via the Gradle `run` task, and use the Swagger UI to try an add a book with an ISBN listed in OpenLibrary.

You may notice that it does not populate the `Publisher` as you expected.
If you look at the messages coming from your Micronaut application, you will see something like this:

```
io.micronaut.http.client.exceptions.HttpClientException:
You are trying to run a BlockingHttpClient operation on a netty event loop thread.
This is a common cause for bugs: Event loops should never be blocked.

You can either mark your controller as @ExecuteOn(TaskExecutors.BLOCKING), or use the reactive HTTP client to resolve this bug. There is also a configuration option to disable this check if you are certain a blocking operation is fine here.
```

The error is self-explanatory, but the basic idea is that the controller methods are invoked on a loop that receives every incoming HTTP request.
Anything that takes too long (like invoking an external service) could result in the event loop not being able to handle requests at a consistent pace.

As the error says, you should add `@ExecuteOn(TaskExecutors.BLOCKING)` to your controller class, and restart the application.
It should work now as expected: the event loop will dispatch the processing of each request to a separate thread pool, meaning that it will not be slowed down by a particular request taking longer than the others.
