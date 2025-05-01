# Ant workflows

## Running the EGL example via Ant

Download a copy of the [Generate Task Lists EGL example](https://eclipse.dev/epsilon/playground/?egx) from the Epsilon Playground.

Make sure to choose the "Ant (Eclipse)" option from the Download dialog:

!["Download" dialog in the Epsilon Playground](./playground-download.png)

Import the downloaded project into Eclipse, and then right-click on `build.xml` and select "Run as -> Ant build...".

In the window that pops up, in the JRE tab, select "Run in the same JRE as the workspace":

![Ant launch configuration dialog, showing the "Run in the same JRE" option](./ant-run-from-same-jre.png)

Click Run.

## Combining the EVL and EGL scripts

Modify `build.xml` so that the constraints in [the related EVL example](https://eclipse.dev/epsilon/playground/?evl) are executed before the EGL model-to-text transformation. For more information, see slide 7 of the Model Management Workflows lecture.

Note that there are several ways to do this:

* The most obvious one is to simply paste the Ant tasks in the main target of the EVL `build.xml` before the ones in the EGL `build.xml` file.
* The above example would result in a significant amount of duplication, however, and it would also mean loading the model twice (once before EVL, and again before EGL). You could change the buildfile so it only loads the model once at the beginning, runs EVL, then EGL, disposes of the model, and refreshes the project in Eclipse.
* You could go further and use the ability of Ant targets to [depend on each other](https://ant.apache.org/manual/targets.html), and set up a structure like this:
  * `load-model` target only loads the model.
  * `dispose-model` target disposes of the model and refreshes the project.
  * `run-evl` target only runs the EVL script.
  * `run-egl` target only runs the EGL script.
  * `main` target depends on `load-model`, `run-evl`, `run-egl`, and `dispose-model`, in that order.

Change `model.flexmi` so that it violates one of the EVL constraints (e.g. set the duration of the Analysis task to a negative number).

Run `build.xml` again: the build should fail in the validation step and code generation should not take place.
