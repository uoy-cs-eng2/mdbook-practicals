# Practical 5: Deploying as containers

This is the worksheet for the fifth practical in the Engineering 2 module.
This is also the last practical for Part 1 (data-intensive systems).

We will start from a solution to Practical 4, and write a [Compose file](https://docs.docker.com/reference/compose-file/) to run the microservices as an orchestration of multiple Docker containers, similarly to how we would do it in production.
Before doing that, we will write some end-to-end tests to check if the application is working as intended with the database and Kafka cluster, which we will reuse to check if our container orchestration is working as intended.

Work through every section of the practical in sequence, without missing steps. You can use the "Previous" and "Next" buttons on the left and right sides to quickly go between sections.