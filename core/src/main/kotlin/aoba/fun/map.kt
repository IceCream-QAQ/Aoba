package aoba.`fun`


@JvmInline
value class BuilderMap<K, V>(val map: HashMap<K, V> = HashMap()) : MutableMap<K, V> by map {
    infix fun K.to(v: V?) = v?.let { put(this, it) }
}

inline fun builderMap(block: BuilderMap<String, Any>.() -> Unit): HashMap<String, Any> =
    BuilderMap<String, Any>().apply(block).map