# Software Distribution DSL queries

Write EOL queries that print:
- The number of components that have no dependencies
- The filenames of JARs that are not used by any component 
- The names of components that are not used in any bundle 
- The filenames of JARs that are not used by any bundle
- The names of the components involved in cyclic dependencies
    - Tip: search the [EOL documentation](https://eclipse.dev/epsilon/doc/eol) for `closure`

Create a sample model that conforms to the software distribution DSL and run the queries against the sample model.

TODO: Point to the metamodel + an existing model?