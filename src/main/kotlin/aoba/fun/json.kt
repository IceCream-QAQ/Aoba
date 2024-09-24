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
        map[this] = any2JsonElement(v)
    }

    fun any2JsonElement(any: Any): JsonElement = when (any) {
        is String -> JsonPrimitive(any)
        is Number -> JsonPrimitive(any)
        is Boolean -> JsonPrimitive(any)
        is List<*> -> buildJsonArray { any.forEach { i -> i?.let { add(any2JsonElement(i)) } } }
        else -> json.encodeToJsonElement(any)
    }

    fun build(): JsonObject = JsonObject(map)
}

inline fun buildJson(block: JsonBuilder.() -> Unit): String =
    JsonBuilder().apply(block).let { json.encodeToString(it.build()) }