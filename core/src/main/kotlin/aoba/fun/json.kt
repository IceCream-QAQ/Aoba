package aoba.`fun`

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.*

val json = Json {
    ignoreUnknownKeys = true
    isLenient = true
}

@JvmInline
value class JsonBuilder(
    val map: HashMap<String, JsonElement> = HashMap()
) : MutableMap<String, JsonElement> by map {

    inline infix fun <reified V> String.to(v: Map<String, V?>?) {
        if (v != null) map[this] = buildJsonObject { v.forEach { (k, v) -> put(k, json.encodeToJsonElement(v)) } }
    }

    inline infix fun <reified V> String.to(v: V?) = v?.let {
        map[this] = any2JsonElement(v)
    }

    inline infix fun <reified V> String.to(v: Collection<V?>?) {
        if (v != null) map[this] = buildJsonArray { v.forEach { add(json.encodeToJsonElement(it)) } }
    }

    inline fun <reified T> any2JsonElement(any: T): JsonElement = when (any) {
        is String -> JsonPrimitive(any)
        is Number -> JsonPrimitive(any)
        is Boolean -> JsonPrimitive(any)
//        is Collection<*> -> buildJsonArray { any.forEach { i -> i?.let { add(any2JsonElement(i)) } } }
        else -> json.encodeToJsonElement(any)
    }

    fun build(): JsonObject = JsonObject(map)
}

inline fun buildJson(block: JsonBuilder.() -> Unit): String =
    JsonBuilder().apply(block).let { json.encodeToString(it.build()) }