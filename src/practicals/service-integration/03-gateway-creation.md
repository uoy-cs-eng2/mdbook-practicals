# Creating the gateway

Let's say that we want to fetch additional information about a book by its ISBN from OpenLibrary.

We want to shield most of our application from the details of talking to OpenLibrary, so we will apply the Gateway pattern that we discussed in the lecture.

## Adding the HTTP client to the regular dependencies

The `build.gradle` normally only includes the HTTP client for compilation and for testing.
We will need it for our regular implementation as well.

Remove these two lines from the `dependencies` of your `build.gradle` (they may be separate from each other):

```groovy
compileOnly("io.micronaut:micronaut-http-client")
testImplementation("io.micronaut:micronaut-http-client")
```

Add this line to the `dependencies`:

```groovy
implementation("io.micronaut:micronaut-http-client")
```

Reload all Gradle projects, since we changed the `build.gradle` file.

## Designing the Gateway interface

Most of the application only needs to know that given an ISBN, we can get some additional information.
Let's encode this in a new interface, within a new `gateways` subpackage:

```java
public interface BookCatalogGateway {
  Optional<BookCatalogInfo> findByIsbn(String isbn);
}
```

Create the `BookCatalogInfo` within the same package.
For this practical, we will keep it simple and just keep a `List<String>` of publisher names in it (with the appropriate getter/setter).

## Writing the scaffold of the Gateway implementation

Create an `OpenLibraryBookCatalogGateway` class in the `gateways` subpackage which implements the `BookCatalogGateway`.

Annotate it with `@Singleton` so Micronaut will automatically create an instance of it and inject it wherever a `BookCatalogGateway` is requested.

Inject a `SearchApi` into `OpenLibraryBookCatalogGateway`.

You would now need to implement the `findByIsbn` method, using the `searchJsonSearchJsonGet` method in `SearchApi`.
For now, just write enough for the code to compile (e.g. just return `Optional.empty()`).

## Wrapping the search method in a fluent builder

As we saw in the previous section, `searchJsonSearchJsonGet` has over 30 parameters, and we only need a few of them.
Calling it directly would require passing a long list of `null`s, and it would be very easy to pass a value in the wrong position.

Instead, we will hide the method behind a small *fluent builder*, which lets us only set the options we care about.
Create an `OpenLibrarySearch` class in the `gateways` subpackage, with this content:

```java
{{#include ../../../solutions/practical3/src/main/java/uk/ac/york/cs/eng2/books/gateways/OpenLibrarySearch.java:8:}}
```

You will need to add the relevant imports (IntelliJ can do this for you).
With this class, a search for a book by ISBN which only asks for its title would look like this:

```java
SearchResponse response = new OpenLibrarySearch(searchApi)
    .isbn("1524797162")
    .fields("title")
    .execute();
```

Note how the builder is itself an example of encapsulation: if OpenLibrary changes the parameters of its search API, we will only need to update `OpenLibrarySearch`.

## Designing a test case for the gateway

Given that the search results are only provided as raw `Map<String, Object>` objects, we'll need specific logic in our gateway to cast this down to the correct type and fill in the `BookCatalogInfo` with the appropriate information.
It's best to do this in a test-driven way.

Create an `OpenLibraryGatewayTest` test class in the appropriate package within `src/test/java`, and write a test that uses the gateway to fetch the information of some book in the Open Library by ISBN (for instance, `1524797162` is the ISBN of a [videogame book](http://openlibrary.org/isbn/1524797162)).
It should have an assertion about having "Del Rey" as its sole publisher.

Try running the test: it should fail, as we don't have a proper implementation of the gateway method yet.

## Completing the Gateway implementation

It's time to finish implementing the gateway method so our test passes.

Use `OpenLibrarySearch` to search by ISBN.
Each result in the search is a *work* (e.g. a novel), which may have multiple *editions* from different publishers.
To get the publishers of the specific edition with that ISBN, you will need to ask for these `fields`: `key`, `editions`, and `editions.publisher`.

The problem is that the `docs` of the `SearchResponse` are only raw `Map<String, Object>` objects: you'll need to use the debugger to find out what is inside them, and then cast down to the correct types to extract the desired information (the names of the publishers) and put it in the `BookCatalogInfo`.

You may find it useful to experiment with the [OpenLibrary Swagger UI](https://openlibrary.org/docs) yourself and see what the response looks like (try the `/search.json` endpoint with the above ISBN and fields).

You'll most likely need to use `instanceof` checks and cast down to `Map` and `List` where needed.
You should return `Optional.empty()` if the response is not in the format if you expect or if an exception is thrown while invoking the OpenLibrary API: you may want to print an error message in those situations (in a production website, you'd log a warning of some kind).

This downcasting is obviously somewhat fragile, as OpenLibrary may decide to change the format of their response at any time, but at least all that is encapsulated in your gateway, and you have a test to automatically detect if they have changed the API in a breaking way.
