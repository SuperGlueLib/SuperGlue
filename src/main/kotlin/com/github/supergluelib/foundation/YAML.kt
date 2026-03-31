package com.github.supergluelib.foundation

import com.github.mlgpenguin.deyaml.Deyaml
import com.github.supergluelib.foundation.util.YamlUtil

object YAML {
    val util = YamlUtil()

    inline fun <reified T : Any> load(resourceName: String): T = Deyaml.load<T>(util.deser(resourceName))

    fun <T: Any> save(obj: T, resourceName: String) = util.save(obj, resourceName)

}