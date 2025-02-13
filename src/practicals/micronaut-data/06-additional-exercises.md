# Additional exercises

Congratulations for getting this far!
This has been a long practical, as we've had to touch upon a wide range of tasks around implementing an API to manage a set of entities in a database.

If you would like to go beyond this practical, here are some ideas you could try out.

## Adding creation or update timestamps

Micronaut Data supports the `@DateCreated` and `@DateUpdated` annotations to automatically record the timestamps of when an entity was created or last updated.

Consult the [Micronaut documentation](https://micronaut-projects.github.io/micronaut-data/latest/guide/#timestamps) and give this a try.

## Adding new entity types and relationships

Consider adding another one-to-many or many-to-many relationship around your entities.

For instance:

* `Book` has multiple `Genre`s, which are also associated to multiple `Book`s
* You can organise some `Book`s into `Series` (e.g. the Lord of the Rings books, or the Harry Potter books).
  You would have a one-to-many relationship from `Series` to `Book`.
  How would you record the order of a given `Book` in a `Series`?

## Adding by-name endpoints

You could add endpoints to find authors or books by name, such as:

* `GET /books/byName/{name}`
* `GET /authors/byName/{name}`

This will require creating custom repository methods, e.g. `findByName`.

## Adding paging

In a real application, you would never return all the records in one go.
This is because you could overwhelm both the client and the server if you tried to send thousands of records in one go.
Instead, a common strategy is to use pagination.

Have a look at the [relevant section of the Micronaut Data documentation](https://micronaut-projects.github.io/micronaut-data/latest/guide/#pagination) on how to do paginated queries, and consider changing your listing endpoints so they can accept an optional page number, like this:

```java
@Get("/{?page}")
Page<Author> list(@QueryValue(defaultValue = "0") int page) { ... }
```

Note that page numbers start at zero for `Pageable`.
If you'd prefer page numbers to start at one in your API, feel free to make any necessary adjustments.
