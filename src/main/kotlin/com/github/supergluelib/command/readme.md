# Lamp
- SuperGlue provides [Lamp](https://github.com/Revxrsal/Lamp) by default
- It also provides some additional methods built on top of Lamp

## Setting up
You can setup Lamp any time, but usually this is done in the onEnable. In order to use SuperGlue,
you need to put `Foundations.setup(this)` in your onEnable, adding basic lamp commands is as simple as calling
the registerCommands method on the Foundations object (typically as below):   
`Foundations.setup(this).registerCommands(...)` (and putting your command classes in the brackets)

If you need to configure additional parameters, like auto completers, parameter validators, etc. you can do so by calling 
`Foundations.setupCommands(...)` BEFORE registering any commands, as this exposes lamps builder.

## SuperGlue - Lamp Annotations

SuperGlue provides the following custom Lamp annotations:

- @NotSelf - Value parameter validator: Prevents the sender from referencing themself
- @NotFullInventory - Value parameter validator: Fails if the target player's inventory is full