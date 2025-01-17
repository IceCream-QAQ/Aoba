package aoba.`fun`

import java.util.TreeMap


@JvmInline
value class BuilderMap<K, V>(val map: HashMap<K, V> = HashMap()) : MutableMap<K, V> by map {
    infix fun K.to(v: V?) = v?.let { put(this, it) }
}

@JvmInline
value class StringTreeMap(val map: TreeMap<String,String> = TreeMap()) : MutableMap<String,String> by map {
    infix fun String.to(v: String?) = v?.let { put(this, it) }
}

inline fun builderMap(block: BuilderMap<String, Any>.() -> Unit): HashMap<String, Any> =
    BuilderMap<String, Any>().apply(block).map

inline fun stringTreeMap(block: StringTreeMap.() -> Unit): TreeMap<String, String> =
    StringTreeMap().apply(block).map