# Foundation (Extension functions & Utilities) - The Core
The foundation of SuperGlue lies within this directory, useful extension functions, utility classes
( and fully fledged utils like the ItemBuilder and void world generator )

## Using ItemBuilder Custom Properties
This is a bit of a niche, but sometimes the item builder just doesn't do everything you need it to- like using adventure components :(

to solve the above issue and many other item-related issues, I've added custom properties and processes that you can add to the ItemBuilder yourself.
The general idea is that there is a `properties: Map<String, Any>` field which you can add KV objects to at any given time, 
and there is also a `ItemBuilder#registerProcessStep(...)` method which allows you to modify the output of the .build() function.
These steps together with extension functions allow you to add completely custom functionality to the ItemBuilder.

An example of these custom properties is provided below:
```kt
/**
 * A one-time setup, registering your custom processes (best done in the onEnable)
 */
fun registerSteps() {
    ItemBuilder.registerProcessStep { meta, builder ->
        val customName = builder.properties["name"] as? String ?: return@registerProcessStep
        meta.setDisplayName(customName)
        // also, dance()?
    }
}

/**
 * Extension function enabling seamless integration with the ItemBuilder.
 */
fun ItemBuilder.setMyCustomName(name: String) = apply  {
    this.properties["name"] = name
}


fun anyOtherFunction() {
    // applies your custom process
    ItemBuilder(Material.DIAMOND).setMySpecialName("JOHN").build()
}
```