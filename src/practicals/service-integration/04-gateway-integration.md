# Integrating the gateway

Now that we have the gateway and a test for it, let's integrate it into the application.
There are basically two ways to do it:

1. When someone adds a book with an ISBN, immediately request the additional information from OpenLibrary and use it.
1. When someone adds a book with an ISBN, produce a Kafka record about the addition, which a consumer will use at some point to extend the new book record.

In this practical, we will practice with both methods.
This section will walk you through the first method.
It has its advantages and disadvantages:

* Good: it's the simplest to implement.
* Good: the book record will be complete by the time we respond to the addition of a book.
* Bad: it adds latency to the processing of the request.
* Bad: OpenLibrary may be down while someone is trying to add a book.
  If we are not careful, users may see error messages while trying to add books just because of that.
  Essentially, this method introduces *temporal coupling* between our application and OpenLibrary.

