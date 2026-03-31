package com.github.supergluelib.foundation.util

import com.github.mlgpenguin.deyaml.Deyaml
import com.github.mlgpenguin.deyaml.SnakeYamlStorageLayer
import com.github.supergluelib.foundation.Foundations
import java.io.File

class YamlUtil {
    val storage = SnakeYamlStorageLayer()

    fun file(resourceName: String): File = File(Foundations.plugin.dataFolder, resourceName)
    fun exists(resourceName: String): Boolean = file(resourceName).exists()
    fun deser(resourceName: String): Map<String, Any> = storage.load(file(resourceName))
    fun <T: Any> save(obj: T, loc: String) = storage.save(Deyaml.deserialise(obj), file(loc))


}