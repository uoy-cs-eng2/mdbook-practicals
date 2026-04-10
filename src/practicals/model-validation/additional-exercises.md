# Additional exercises

Add support for model validation to the Sirius editor you developed in the previous practical using EVL, as shown [here](https://github.com/uoy-cs-eng2/psl-sirius?tab=readme-ov-file#model-validation).

![](images/validation-markers.png)

Your validation project (the equivalent of `psl.validation` in the example above) should be an Eclipse plug-in project that will host your EVL constraints as well as a `plugin.xml` file that will bind the constraints to the namespace of your metamodel (see below).

```xml
<?xml version="1.0" encoding="UTF-8"?>
<?eclipse version="3.4"?>
<plugin>
   <extension
         point="org.eclipse.epsilon.evl.emf.validation">
      <constraintsBinding
            compose="true"
            constraints="your-constraints.evl"
            namespaceURI="your-language-namespace-uri">
      </constraintsBinding>
   </extension>
	<extension point="org.eclipse.ui.ide.markerResolution">
		<markerResolutionGenerator
        class="org.eclipse.epsilon.evl.emf.validation.EvlMarkerResolutionGenerator"
        markerType="org.eclipse.emf.ecore.diagnostic"></markerResolutionGenerator>
		<markerResolutionGenerator
        class="org.eclipse.epsilon.evl.emf.validation.EvlMarkerResolutionGenerator"
        markerType="org.eclipse.sirius.diagram.ui.diagnostic"></markerResolutionGenerator>
	</extension>
</plugin>
```

Your validation project should also require the `org.eclipse.epsilon.evl.emf.validation` plug-in in its `MANIFEST.MF` (as `psl.validation` does).