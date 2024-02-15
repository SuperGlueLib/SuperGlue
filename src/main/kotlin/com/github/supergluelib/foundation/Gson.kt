package com.github.supergluelib.foundation

import com.github.supergluelib.foundation.extensions.toHashMap
import com.github.supergluelib.foundation.misc.BlockPos
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.TypeAdapter
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonToken
import com.google.gson.stream.JsonWriter
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.NamespacedKey
import org.bukkit.World
import java.lang.reflect.Type

@RequiresOptIn("This Gson adapter is new and may be susecptible to bugs and errors, please report any issues you find", RequiresOptIn.Level.WARNING)
@Retention(AnnotationRetention.BINARY)
@Target(AnnotationTarget.CLASS, AnnotationTarget.FUNCTION)
annotation class ExperimentalGsonAdapter

/**
 * Registering custom type adapters sometimes requires different methods, this ensures they will always register correctly.
 */
fun GsonBuilder.registerCustomTypeAdapter(adapter: CustomAdapter<*>) = adapter.register(this)

abstract class CustomAdapter<T>: TypeAdapter<T>(){
    abstract fun register(gson: GsonBuilder): GsonBuilder
    abstract fun writeOut(value: T, writer: JsonWriter)
    abstract fun readIn(reader: JsonReader): T?

    override fun write(out: JsonWriter, value: T?) {
        if (value == null) out.nullValue()
        else writeOut(value, out)
    }

    override fun read(input: JsonReader): T? {
        if (input.peek() == JsonToken.NULL) {
            input.nextNull()
            return null
        } else return readIn(input)
    }
}

@ExperimentalGsonAdapter
class WorldGsonAdapter(): CustomAdapter<World>() {
    override fun register(gson: GsonBuilder) = gson.registerTypeHierarchyAdapter(World::class.java, this)
    override fun readIn(reader: JsonReader) = Bukkit.getWorld(reader.nextString())
    override fun writeOut(value: World, writer: JsonWriter) { writer.value(value.name) }
}

@ExperimentalGsonAdapter
class BlockLocationAdapter(val world: World? = null): CustomAdapter<Location>() {
    override fun register(gson: GsonBuilder): GsonBuilder = gson.registerTypeAdapter(Location::class.java, this)
    private fun JsonReader.nextNameAndInt(): Int { nextName(); return nextInt() }
    override fun readIn(reader: JsonReader): Location {
        reader.beginObject()
        val x = reader.nextNameAndInt()
        val y = reader.nextNameAndInt()
        val z = reader.nextNameAndInt()
        reader.endObject()
        return Location(world, x.toDouble(), y.toDouble(), z.toDouble())
    }
    override fun writeOut(value: Location, writer: JsonWriter) {
        writer.beginObject()
            .name("x").value(value.blockX)
            .name("y").value(value.blockY)
            .name("z").value(value.blockZ)
            .endObject()
    }
}

class NamespacedKeyGsonAdapter(): CustomAdapter<NamespacedKey>() {
    override fun register(gson: GsonBuilder) = gson.registerTypeAdapter(NamespacedKey::class.java, this)
    override fun readIn(reader: JsonReader) = NamespacedKey.fromString(reader.nextString())
    override fun writeOut(value: NamespacedKey, writer: JsonWriter) { writer.value(value.toString()) }
}

class MapGsonAdapter<K, V>(val keyClass: Class<K>, val valueClass: Class<V>, val adapterGson: Gson = Gson()): CustomAdapter<Map<K, V>>() {
    private val type = object: TypeToken<Map<K, V>>() {}.rawType
    // [ ["key", "value"], ["key", "value"] ]
    override fun register(gson: GsonBuilder): GsonBuilder = gson.registerTypeHierarchyAdapter(type, this)

    override fun writeOut(value: Map<K, V>, writer: JsonWriter) {
        writer.beginArray()
        value.entries.forEach { entry ->
            writer.beginArray()
            writer
                .value(adapterGson.toJson(entry.key, keyClass))
                .value(adapterGson.toJson(entry.value, valueClass))
            writer.endArray()
        }
        writer.endArray()
    }

    override fun readIn(reader: JsonReader): Map<K, V> {
        val entries: MutableList<Pair<K, V>> = mutableListOf()
        reader.beginArray()
        while (reader.peek() == JsonToken.BEGIN_ARRAY) {
            reader.beginArray()
            // TODO Need to find a way to de-stringify this string
            val key = adapterGson.fromJson(reader.nextString(), keyClass)
            println(key)
            val value = adapterGson.fromJson(reader.nextString(), valueClass)
            reader.endArray()
            entries.add(key to value)
        }
        reader.endArray()
        return entries.toMap().toHashMap()
    }
}

class NestedMapGsonAdapter<K, V, O>(val firstKeyType: Class<K>, val secondKeyType: Class<V>, val valType: Type, val adapterGson: Gson): CustomAdapter<Map<K, Map<V, O>>>() {
    /*
        [
             { "K": [ [ "V", "O" ], [ "V", "O" ] ] },
             { "K": [ [ "V", "O" ], [ "V", "O" ] ] }
         ]

     */
    private val type = object: TypeToken<Map<K, Map<V, O>>>() {}.rawType
    override fun register(gson: GsonBuilder) = gson.registerTypeHierarchyAdapter(type, this)

    override fun writeOut(value: Map<K, Map<V, O>>, writer: JsonWriter) {
        writer.beginArray()
        for (fullEntry in value.entries) {
            writer.beginObject()
            writer.name(adapterGson.toJson(fullEntry.key, firstKeyType))
            writer.beginArray()
            for (nestedEntry in fullEntry.value.entries) {
                writer.beginArray()
                writer.value(adapterGson.toJson(nestedEntry.key, secondKeyType))
                writer.value(adapterGson.toJson(nestedEntry.value, valType))
                writer.endArray()
            }
            writer.endArray()
            writer.endObject()
        }
        writer.endArray()
    }

    override fun readIn(reader: JsonReader): Map<K, Map<V, O>> {
        val entries = mutableListOf<Pair<K, Map<V, O>>>()
        reader.beginArray()
        while (reader.peek() == JsonToken.BEGIN_OBJECT) {
            reader.beginObject()
            val key = adapterGson.fromJson(reader.nextName(), firstKeyType)
            reader.beginArray()
            val nestedEntries = mutableListOf<Pair<V, O>>()
            while (reader.peek() == JsonToken.BEGIN_ARRAY) {
                reader.beginArray()
                val nestedKey = adapterGson.fromJson(reader.nextString(), secondKeyType)
                val nestedValue = adapterGson.fromJson<O>(reader.nextString(), valType)
                reader.endArray()
                nestedEntries.add(nestedKey to nestedValue)
            }
            reader.endArray()
            reader.endObject()
            entries.add(key to nestedEntries.toMap().toHashMap())
        }
        reader.endArray()
        return entries.toMap().toHashMap()
    }
}

class BlockPosArrayGsonAdapter(): CustomAdapter<BlockPos>() {
    override fun register(gson: GsonBuilder): GsonBuilder = gson.registerTypeAdapter(BlockPos::class.java, this)
    override fun writeOut(value: BlockPos, writer: JsonWriter) {
        writer.beginArray()
        writer.value(value.x).value(value.y).value(value.z)
        writer.endArray()
    }

    override fun readIn(reader: JsonReader): BlockPos {
        reader.beginArray()
        val pos = BlockPos(reader.nextInt(), reader.nextInt(), reader.nextInt())
        reader.endArray()
        return pos
    }
}