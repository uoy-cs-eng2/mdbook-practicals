# ENG2 practicals

This repository hosts the [mdBook](https://github.com/rust-lang/mdBook) sources of the worksheets for the practicals of the ENG2 module at the University of York.

The built worksheets are available [online](https://uoy-cs-eng2.github.io/mdbook-practicals/), and the [solutions](./solutions) are available from this repository - but please only check them if stuck!

## Building the book

To build this book locally, install mdBook using the [official instructions](https://rust-lang.github.io/mdBook/guide/installation.html).
You can then serve it locally with:

```shell
./zip-solutions.sh && mdbook serve --open
```

Alternatively, you can build a static HTML version in the `book` folder with:

```shell
./zip-solutions.sh && mdbook build
```

## Checking links

Our CI uses [lychee](https://github.com/lycheeverse/lychee) to check the internal and external links of the book.

To check links locally, build the book, install lychee, and run this command:

```shell
lychee --exclude-loopback book
```
