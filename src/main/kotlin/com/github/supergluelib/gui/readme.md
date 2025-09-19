# GUIs in SuperGlue

SuperGlue provides a fully featured GUI Builder for your use. It primarily uses Gradles DSL format to provide methods
for building a GUI.

An example is provided below where a player receives a diamond for clicking on the button:

```kt
class BasicGUI(): GUI() {

    override fun generateInventory() = createInventory("&b&lFree Diamonds", 27) {
        setBorder(Panes.LIGHT_BLUE)
        setButton(13, ItemBuilder(Material.DIAMOND, "&bClick to get a Diamond! :D").build()) {
            player.giveOrDropItem(ItemStack(Material.DIAMOND))
        }
    }

}
```

## Additional Features
The GUI class implementation is designed to be minimal and basic by default, you can enable additional features and functionality 
through the use of interfaces. The available interfaces are provided below:

- **CloseEvent**: Implements a 'CloseEvent' Handler allowing you to implement logic when the GUI is closed.
- **OpenEvent**: Implements an 'OpenEvent' Handler allowing you to implement logic when a player opens the GUI.
- **ForceKeepOpen**: provides a `canClose(Player): Boolean` method which determines whether a player is allowed to close their GUI yet.

Additionally, if the built-in button builders and functions do not suit your needs, you can instead opt to override the onClick method manually 
which is 100% supported and scoped to your GUI.

## Using GUI Extension (Builder) functions
(TLDR; If you are using SuperGlue GUIs, these are automatically made available to you, otherwise you can import them)

- GUI Extension or GUI Builder functions were previously available in the global scope via import, however this is uneccessary
overhead and they have since been moved to the GUIExtensions interface.  
- Thanks to this change, by default in IntelliJ you can hit `ctrl + o` and view all the functions available to you (when scoped).  
- If you wish to use any GUI extension functions in a non-superglue-gui setting, add the `GUIExtensions` interface to your scope and they will become available.