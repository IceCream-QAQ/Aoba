package aoba.json

import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerializationException
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

object EnableDisableBooleanSerializer : KSerializer<Boolean> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("EnableDisableBoolean", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: Boolean) {
        val stringValue = if (value) "ENABLE" else "DISABLE"
        encoder.encodeString(stringValue)
    }

    override fun deserialize(decoder: Decoder): Boolean {
        return when (val stringValue = decoder.decodeString()) {
            "ENABLE" -> true
            "DISABLE" -> false
            else -> throw SerializationException("Unexpected value: $stringValue")
        }
    }
}

object YesNoBooleanSerializer : KSerializer<Boolean> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("YesNoBoolean", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: Boolean) {
        val stringValue = if (value) "YES" else "NO"
        encoder.encodeString(stringValue)
    }

    override fun deserialize(decoder: Decoder): Boolean {
        return when (val stringValue = decoder.decodeString()) {
            "YES" -> true
            "NO" -> false
            else -> throw SerializationException("Unexpected value: $stringValue")
        }
    }
}