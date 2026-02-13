# Additional exercises

- Generate dedicated tree-based editors for your metamodels as demonstrated in the EMF lecture
- Replace the default icons of your editor
    - Icons under `<dsl>.edit/icons/full/obj16`
    - You will need to find appropriate 16x16 icons
- Customise the labels of model elements on the tree editor by modifying the `getText(...)` methods of `<dsl>.edit/src/<dsl>.provider/<type>ItemProvider` classes
    - Don't forget to set them to `@generated NOT`
