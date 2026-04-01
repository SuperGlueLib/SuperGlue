package com.github.supergluelib.foundation

import com.github.mlgpenguin.deyaml.Deyaml
import com.github.supergluelib.foundation.util.YamlUtil

object YAML {
    val util = YamlUtil()

    inline fun <reified T : Any> load(resourceName: String): T = Deyaml.load<T>(util.deser(resourceName))

    inline fun <reified T : Any> loadOrCreate(resourceName: String, default: T): T = if (util.exists(resourceName)) {
        load<T>(resourceName)
    } else {
        Foundations.log("Creating $resourceName automatically")

        // Create file and parent directories
        val file = util.file(resourceName)
        if (!file.parentFile.exists()) file.parentFile.mkdirs()
        file.createNewFile() // implicitly "if not exists"

        save(default, resourceName)
        default
    }

    fun <T: Any> save(obj: T, resourceName: String) = util.save(obj, resourceName)

}