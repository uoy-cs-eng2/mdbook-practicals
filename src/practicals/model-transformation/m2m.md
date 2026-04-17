# Model-to-model transformation

**Exercise 1:** Modify [this ETL transformation](https://eclipse.dev/epsilon/playground/?4c163eb6) on Epsilon's playground so that it generates 2 deliverables per task
- One interim report in the middle of the task
- One final report at the end of the task

**Exercise 2:** Study [this Flock migration transformation](https://eclipse.dev/epsilon/playground/?flock) on Epsilon's playground. Now complete [this Flock migration](https://eclipse.dev/epsilon/playground/?8e886c96) to add effort elements to the migrated model.

> [!NOTE]
> You can develop the transformations above either directly on the Playground or in Eclipse, as demonstrated in the respective lectures. If you develop them on the Playground, we advise you to also download copies (using the Playground's `Download -> Ant (Eclipse)` menu and dialog) and run them within Eclipse.

## Exercise 3

Write a M2M transformation with ETL that produces a MiniVoiceXML model from a call centre model. MiniVoiceXML is a toy version of the [W3C VoiceXML specification](https://www.w3.org/TR/voicexml20/).

### MiniVoiceXML metamodel

- A `Document` has 0+ `Dialogs`, which can be `Forms` or `Menus`: execution starts from first `Dialog`
- A `Form` has 0+ `FormItems`: `Form` gets inputs from the caller and assigns them to variables
    - `Field`: prompt the caller, read line of input, and assign input to var with given `name`
    - `Transfer`: transfer the caller to another number (`dest` uses `tel:NUMBER`)
    - `Block`: 0+ executable elements
        - `GoTo`: switch to another Dialog
        - `Prompt`: output text to user
- A `Menu` has a `Prompt` and 1+ `Choice`: runs the `Prompt` then shows options and waits for choice
    - `dtmf`: number (1-9) to be typed by caller
    - `next`: Dialog to traverse to if chosen

![The MiniVoiceXML metamodel](minivoicexml.png)

### The transformation

- Clone [this Github](https://github.com/uoy-cs-eng2/minivoicexml)
    - Alternatively, [download a copy of its contents](https://github.com/uoy-cs-eng2/minivoicexml/archive/refs/heads/main.zip)
- Import the metamodel project (`minivoicexml`) into Eclipse
- In a nested Eclipse, import the interpreter (`minivoicexml.interpreter`) project
- In the nested Eclipse, create a new project named `callcentre2minivoicexml`
- In the new project, create an ETL script with these rules:
    - `Model` → `Document` with the `Dialogs` produced from the `Steps`
        - The first `Dialog` should be the one from the first `Step` that would be run
    - `Statement` → `Form` with `Block` containing a `Prompt`
    - `CallRedirection` → `Form` with `Transfer` (watch out for `dest` format)
    - `InputQuestion` → `Form` with `Field` containing a `Prompt`
    - `Decision` → `Menu` with `Prompt`
    - `Transition` →
        - If the source is a `Decision`: add `Choice` to the `Menu` from the source node
            - Remember to set the `dtmf`, `text`, and `next` features correctly!
        - Any other: add `GoTo` to the `Block` at the end of the `Form` from source node
            - If such a `Block` does not exist, add it as well
            - Remember to set the `next` reference to the equivalent of the `Transition` target
- Run your ETL script on your sample model:
    - Configure the launch so the new model is saved to `generated-voicexml.model` inside your `callcentre2minivoicexml` project
- Run the resulting MiniVoiceXML models on the interpreter:
    - In `minivoicexml.interpreter`, right-click on `Launcher.launch`, select `Run As –> Launcher`
    - The interpreter will run from the `Console` view: check that the model behaves as expected by entering your answers

## Solutions

- Exercise 1

```etl
rule Project2Project
	transform s : Source!Project
	to t : Target!Project {

	t.name = s.name;

	// Transform the tasks of the source
	// project using the Task2Deliverable
	// rule and assign the result to
	// t.deliverables
	t.deliverables ::= s.tasks;
}

// Transform each task to two deliverables
// to be submitted in the middle and at the end of the task
rule Task2Deliverable
	transform t : Source!Task
	to interim : Target!Deliverable, 
       final : Target!Deliverable {

	interim.name = t.name + " Interim Report";
	interim.due = t.start + (t.duration / 2);

	final.name = t.name + " Final Report";
	final.due = t.start + t.duration;

	// The lead of the deliverables
	// is the person with the highest
	// effort in the task
	var lead = t.effort.sortBy(e|-e.percentage).
		first()?.person;

    interim.lead ::= lead;
    final.lead ::= lead;
}

// @lazy means that persons will be transformed 
// only upon request. As such, Charlie will not 
// appear in the target model because he leads
// no deliverables
@lazy
rule Person2Person
	transform s : Source!Person
	to t : Target!Person {

	t.name = s.name;
}
```
- Exercise 2: A [reference solution](https://eclipse.dev/epsilon/playground/?pslv0-v1) is available on Epsilon's playground.
- Exercise 3: A [reference solution](https://eclipse.dev/epsilon/playground/?callcentre2minivoicexml) is available on Epsilon's playground.
 