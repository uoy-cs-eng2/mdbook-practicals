# Conference DSL queries

Write five EOL queries (Q1-Q5) that print:
- **Q1**: The number of days the conference lasts
- **Q2**: A set of unique affiliations of participants of the conference
    - i.e. if multiple persons have the same affiliation, the affiliation should appear only once in this set
- **Q3**: The total duration of all talks in the conference
- **Q4**: A set of all the names of the rooms in which breaks take place
- **Q5**: The number of tracks that start before noon
    - Reminder: `Slot.start` and `Slot.end` are strings that use the `HH:MM` format (e.g. 15:30).

Instructions for running your EOL queries against a conference model are provided [below](#running-queries-q1-q5).

## Solutions

```eol
{{#include ../../../solutions/practical8/conference-dsl/queries1to4.eol}}
```

For Q5, we will need to define a few helper operations for working with `HH:MM`-formatted times first. For now, we assume that string values under `Slot.start` and `Slot.end` conform to the `HH:MM` format. Later on, we will encode (and check) this assumption using a validation constraint.

```eol
{{#include ../../../solutions/practical8/conference-dsl/query5.eol}}
```

<div class="warning">
<b>Statements after operations are ignored</b>

The EOL execution engines ignores any statements that appear after the definition of the first operation in a `.eol` file. Therefore, if you were to move the first statement in the example above (`Track.all.select(t|t.start.isBefore("12:00")).size().println();`) to the end of the file, it would not get executed.
</div>

## Running queries Q1-Q5

- To run these queries you will need
    - The Ecore metamodel of the Conference DSL;
    - A sample model that conforms to it;
    - An EOL file that contains your queries.
- You should already have `conference.ecore` (Ecore metamodel) and `conference.model` (sample model) in your workspace after following [the instructions at the start of this practical](./index.md#before-you-start).
- Create an empty text file named `conference-queries.eol` within your `conference-dsl` project.
- Right-click on `conference.ecore`.

![Step 5](images/step-5.png)

- Click on the `Register EPackages` menu item. This will register the metamodel with EMF so that it can load models (such as `conference.model`) that conform to this metamodel.

![Step 6](images/step-6.png)

- Open your `conference-queries.eol` file and type/paste your queries in it.

![Step 7](images/step-7.png)

- Create a new `EOL Program` run configuration to run your EOL queries.

![Step 8](images/step-8.png)

![Step 9](images/step-9.png)

- Double-check that the path of your EOL file appears in the `Source` field of the `Source` tab of the run configuration dialog.

![Step 10](images/step-10.png)

- Add the Conference model (`conference.model`) to the run configuration through the `Models` tab of the dialog.

![Step 11](images/step-11.png)

![Step 12](images/step-12.png)

- Set the name of the model to `M`

![Step 13](images/step-13.png)

- Select `conference.model` as the model file. If the file selection dialog is empty, start typing the name of the file or `**` to display all workspace files. 

<div class="warning">
<b>Have you selected the model file?</b>

Double-check that you have selected the model file (`conference.model`) and **not** the metamodel file (`conference.ecore`) in this step. This is a common mistake to make.
</div>

![Step 14](images/step-14.png)

- As soon as you select `conference.model`, the URI of its metamodel (`conference`) should be added to the `Metamodels` list of the dialog. If it doesn't, it means that you have either not selected the correct model file, or that you have not registered your metamodel (`conference.ecore`) with EMF (see earlier steps).

![Step 15](images/step-15.png)

- Click the `Run` button.

![Step 16](images/step-16.png)

- The output of your queries should appear in the `Console` view.

![Step 17](images/step-17.png)

