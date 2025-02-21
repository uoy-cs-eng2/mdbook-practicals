# Crashed desks

We have endpoints for listing the out-of-order desks and those that have a customer stuck on them.
In this exercise, you will add an endpoint that will list all the "crashed" desks.
We will say that a desk has crashed if it has not sent status updates in the last 12 seconds.

This is mostly an exercise to check that you understood everything so far:
we will not be introducing any new concepts. 

## Adding the database migration

We need a new column to reflect the last time we received a status update from a desk.

Create a new `V3__add-last-status.sql` database migration script, with this content:

```sql
alter table check_in_desk
add last_status_at timestamp(3);
```

## Updating the entity

Update the `CheckInDesk` entity to reflect this new column.

## Creating the consumer

Create the `StatusUpdateConsumer` that will consume the `CheckinTopics.TOPIC_STATUS` records,
and update the `lastStatusAt` field in `CheckInDesk` to the timestamp of the given Kafka record.

## Testing the consumer

As usual, write an automated unit test to ensure that the consumer works as it should.

## Extend the controller

Add a `GET /desks/crashed` endpoint to list the desks whose last status update was from before 12 seconds ago.

You will need to write a custom query in your repository that lists the `CheckInDesk` whose `lastStatusAt` was before a given `Instant`.

Note that you can compute the `Instant` from 12 seconds ago with:

```java
Instant.now().minusSeconds(12)
```

## Trying everything together

Try running the simulator and your consumers, and checking that crashed desks start appearing once the system has been running for a minute or so.

Once you're happy with the results, stop the simulator and your consumers, and move on to the final exercise.
