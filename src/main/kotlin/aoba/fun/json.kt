package aoba.`fun`

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.*

val json = Json {
    ignoreUnknownKeys = true
}

@JvmInline
value class JsonBuilder(
    val map: HashMap<String, JsonElement> = HashMap()
) : MutableMap<String, JsonElement> by map {
    inline infix fun <reified V> String.to(v: V?) = v?.let {
        when (it) {
            is String -> map[this] = JsonPrimitive(it)
            is Number -> map[this] = JsonPrimitive(it)
            is Boolean -> map[this] = JsonPrimitive(it)
//            is Array<*> -> map[this] = buildJsonArray { array ->  }
            is List<*> -> map[this] = buildJsonArray { it.forEach { i -> add(json.encodeToJsonElement(i)) } }
            else -> map[this] = json.encodeToJsonElement(it)
        }
    }

    fun build(): JsonObject = JsonObject(map)
}

inline fun buildJson(block: JsonBuilder.() -> Unit): String =
    JsonBuilder().apply(block).let { json.encodeToString(it.build()) }