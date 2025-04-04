# Research Project DSL queries

Write EOL queries that print:

- The titles of all deliverables in the project, in chronological order
- The names of partners that have allocated effort to a work-package but don't contribute to any of its tasks
- Labels for all tasks in the following form: `T<Work-package-index>.<Task-index>`
    - The label of the second task of the third work-package is `T2.3`
- The titles of deliverables that are due before the start / after the end of the work-package in which they are contained
    - The start/end month of a work-package can be computed through the work-package's tasks
