# Stuck check-ins

In the previous exercise, the consumer maintained overall counts of various types of events.
In this exercise, we will instead maintain an updated view of the current state of every check-in desk,
and provide these endpoints:

* `GET /desks`: lists all the desks and their current state.
* `GET /desks/outOfOrder`: lists all the desks which are currently out of order.
* `GET /desks/stuck`: lists all the desks which have gone out of order *after* a check-in process started on them. These desks have a customer *stuck* on them.

## Adding the database migration

Add a migration script called `V2__create-checkin-desk.sql` with this content:

```sql
create table check_in_desk (
    -- surrogate key (autoincremented)
    id bigint primary key not null,
    -- natural key (from Kafka)
    desk_id bigint unique not null,
    -- if not null, a check-in is currently undergoing in this desk
    checkin_started_at timestamp(3),
    -- true iff out of order
    out_of_order bool not null default false
);
```

This creates a new table which will hold the known status of each desk.

Note: `timestamp(3)` is a MariaDB type which means "timestamps at the millisecond level" (i.e. 10^-3 seconds).
The plain `timestamp` type only works at the level of seconds.

## Creating the entity and the repository

You should be able to create the JPA entity and the repository yourself for the most part.

The only detail is that the `timestamp(3)` column should be mapped to a `java.time.Instant`.
It is currently one of the most convenient types in Java for representing a given moment in time.

## Creating the consumer

Create a `CheckinDeskConsumer` consumer class.
You will need to give it a `groupId`, and you will want to use multiple threads and start from the beginning of the topics involved when the consumer group is created.

In terms of actual behaviour, you should do this:

* When a check-in is started, create or update the `CheckInDesk` with the relevant `desk_id`:
  * Update `checkin_started_at` to the timestamp of the Kafka record.
  * Set it as not being out of order.
* When a check-in is cancelled or completed, create or update the `CheckInDesk` with the relevant `desk_id`:
  * Reset `checkin_started_at` to `null`.
  * Set it as not being out of order.
* When a desk goes out of order, create or update the `CheckInDesk` with the relevant `desk_id`:
  * Set it as being out of order.

Note that the consumer methods will need these parameters:

* `@KafkaKey long deskId`: the key of the topics is the desk ID.
* `TerminalInfo tInfo`: the unannotated parameter after the `@KafkaKey` is bound to the record body.
* `long timestamp`: this parameter is bound by Micronaut to the timestamp of the record.
  This is because we want to use the time in the record rather than the current time in the consumer:
  we may be catching up with old events.

## Testing the consumer

You should write tests for the above consumer.

The approach will be the same as in the previous section:

* Clear the table before each test.
* Invoke the relevant consumer methods.
* Check that they had the expected effects on the database.

## Writing the controller

You can now write the controller that will handle the requests mentioned at the top of this section.

You will want to define two custom queries in your repository for `CheckInDesk`s:

* Finding the `List<CheckInDesk>` by a given `out_of_order` value.
* Finding the `List<CheckInDesk>` by a given `out_of_order` value whose `checkin_started_at` is not `null`.
  (This can be done as a single custom query: revisit the Micronaut Data documentation on how to specify multiple conditions.)

## Trying it all together

Same as before: start the simulator, then start your application, and try invoking your endpoints while the simulation advances.

You should be able to see all your desks very soon.
As the simulation advances, some of them will start showing up as out of order.
Later, some of the out-of-order desks will involve stuck customers as well.

In other words, you should see that the out-of-order desks are a subset of all the desks, and that the stuck desks are a subset of the out-of-order ones.

Once you're happy with your consumer, stop the simulator and your consumers, and move on to the next section.