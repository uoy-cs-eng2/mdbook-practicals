# Additional exercises

Congratulations on completing this practical!

We hope that you found it interesting to use the Gateway pattern and use it both from a controller, and from a background job via Kafka.

Here are some ideas on things you could try to expand upon what we have covered in this practical.

## Enable caching in the gateway

To reduce the load imposed on OpenLibrary from our application and improve its responsiveness, it may be good to adopt [Micronaut Cache](https://micronaut-projects.github.io/micronaut-cache/5.1.0/guide/) to automatically cache the results from our gateway.

For inspiration, you may want to check the example mentioned in the lecture slides for service integration.
It should be a matter of adding the library, adding some configuration to your `application.properties`, and adding the appropriate `@Cacheable` annotation to the right method in your gateway implementation.

## Use the Circuit Breaker pattern

You may have noticed that the example in the service integration lecture also uses the [Micronaut Retry](https://docs.micronaut.io/snapshot/guide/index.html#clientRetry) library to implement the Circuit Breaker pattern.

This pattern would be useful if OpenLibrary had intermittent problems.
The `@CircuitBreaker` annotation implies a number of changes in behaviour:

* There will be a few retries when making the original request, instead of a single one.
* If several errors are observed in a given period of time, the "circuit breaker" will go into a "closed" state and all requests will immediately fail. This has several benefits:
  * The application will be more responsive, as it won't be waiting every time for a failing service.
  * The failing service will see a reduction in its load, which will make it easier for its administrators to bring it back up.
* After some time, the "circuit breaker" will go to a half-open state, in which it will attempt to make a real request again. If that works, it will go back to an "open" state where requests go to OpenLibrary as usual. Otherwise, it will go back to the above "closed" state and wait for some time before trying again.

Since you cannot directly change the generated OpenAPI client (`BooksApi`), you may need to apply the `@CircuitBreaker` interface to a new subinterface of `BooksApi` that you would create yourself.

## Use more information from OpenLibrary

You could try to pull in more information from OpenLibrary.
For instance, you could get the title as well, or the authors.
