# Background processing

In Practical 3, we changed the controller so it would immediately try to obtain the publisher while adding or updating a book.

That created *temporal coupling* between our application and the external service, as it requires OpenLibrary to be available at the same time a book is being added or updated.
This may not always be the case: OpenLibrary may be down at that time, in which case we may want to leave that until later.

There is another problem.
This synchronous blocking approach, where we wait for OpenLibrary to respond before we respond to the user, introduces latency: if OpenLibrary is busy, this could make our entire application feel sluggish as well.

To address these issues, we will move the use of the gateway to a Kafka consumer.
This means adding books will feel faster again, but users may not immediately see the publisher after creating the book.

## Adding Kafka to the project

Add Kafka support to your Practical 3 project (or start from [its model solution](../../solutions/practical3.zip)).

Remember from [Practical 2](../micronaut-data/02-libraries.md#updating-our-project) that you can use the "Diff" button in [Micronaut Launch](https://micronaut.io/launch) to tell you what needs to be changed to support a new feature (in this case, the `kafka` one).

## Creating the Kafka topic

Create a Kafka topic factory which sets up an internal `isbn-updated` topic, with 3 partitions and replication factor of 1.
Consult the lecture slides or the [relevant part of Practical 3](../micronaut-kafka/03-checkin-stats.md#automated-topic-creation) if you are unsure about how to do that.

## Creating the Kafka producer

Define a Kafka producer interface that sends records to the `isbn-updated` topic about books changing the ID.

We suggest that record keys are book IDs, and record bodies mention the old and new ISBNs.
When the book is created, the old ISBN can be `null`.

## Producing the event

Update the controller for creating and updating books, so that it produces an event when the ISBN of a book changes.

Add tests checking the producer is used as expected in those scenarios.

## Using OpenLibrary from a consumer

Create a new Kafka consumer which will handle the records arriving to the `isbn-updated` topic.

Move the controller logic that uses the gateway to populate the Publisher there.
Keep in mind that there may be a long delay between the book being created and the `isbn-updated` event being consumed, so you may want to tweak its logic somewhat.
We suggest something like this:

* Try to fetch the book by ID. If it does not exist anymore, stop processing this event.
* Check if the book already has a publisher. If it does, stop processing this event.
* Try to fetch the publisher(s) via your gateway. If the gateway fails or it does not return any results, stop processing this event. Note: if the gateway throws an exception, print it to the standard error stream with `exception.printStackTrace()`.
* Find the `Publisher` with the first name listed by the gateway, and associate it with the `Book`. Create the `Publisher` if it does not exist.
* Update the `Book`. Note that since this should always be an update, you can do `repo.update(book)` instead of `repo.save(book)` (which can sometimes be an insert, and sometimes be an update).

Update your controller tests accordingly, and write tests for your new consumer.
We recommend having one separate test for every possible situation above.

For testing, it may be easier to mock the gateway and set it up with a few ISBNs that will make it behave in specific ways (an ISBN with a known publisher, an ISBN that will cause the gateway to throw an exception, an ISBN which will return an empty response, and so on).

## Removing the old ExecuteOn annotation

Now that you no longer perform a synchronous blocking HTTP requests from your `BooksController`, you should remove the `@ExecuteOn` annotation that you added in the previous section.

You should also be able to remove the injection of the gateway into the `BooksController`.