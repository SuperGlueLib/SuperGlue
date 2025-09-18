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