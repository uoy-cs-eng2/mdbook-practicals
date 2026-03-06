# Additional exercises

Here are some ideas for things that you could try to go beyond what we cover in this practical.

## Add a healthcheck to the microservice

Micronaut provides a [built-in health endpoint](https://docs.micronaut.io/latest/guide/#healthEndpoint).
Try using it from your Compose file.
The command for it would be something like this:

```sh
curl --fail http://localhost:8080/health
```

`curl` is a very popular command-line tool for accessing HTTP endpoints, and `--fail` means "produce a non-zero status code if the HTTP request produces a 4xx or 5xx status code".

## Adding a database administration UI

The `todo-microservice` example also includes an `adminer` service which provides a convenient web-based UI to inspect your database.

Try copying it over and using it.
Keep in mind that Adminer itself is running from inside the Docker network, so it can connect to the database server through the `db` hostname.

## Advanced: Podman instead of Docker

There are other containerisation options besides Docker, which you could try on your own computer.
These options can have less restrictive licenses than Docker, which has an open-source core but many closed-source additional features.

For example, there is [Podman](https://podman.io/), which provides similar functionality while not requiring an ongoing "daemon" process like Docker (which can be a security liability).
Podman has a GUI similar to Docker Desktop (Podman Desktop), and the `podman` command [can be set up as a drop-in alias](https://developers.redhat.com/blog/2020/11/19/transitioning-from-docker-to-podman) of the `docker` command.

Keep in mind that for the assessment, we will still be using the Docker toolset for the time being, as it is the most mature option in the market at the moment.
This exercise is only for your own experimentation.

## Advanced: Kubernetes instead of Compose

You could experiment with a tool such as [Kompose](https://kompose.io/), which converts Compose files to the format required to run them within Kubernetes clusters.

Kubernetes is much more complex than Compose though, so this is not for the faint of heart!
For your first time with Kubernetes, we recommend using Minikube from a Linux OS.
That said, proceed at your own risk - we do not teach Kubernetes in this module.
