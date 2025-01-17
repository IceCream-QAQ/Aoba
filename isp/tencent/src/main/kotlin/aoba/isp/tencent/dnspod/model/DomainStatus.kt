package aoba.isp.tencent.dnspod.model

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.util.*


enum class DomainStatus {
    // 正常
    ENABLE,

    // 暂停
    PAUSE,

    // 封禁
    SPAM;

    object Serializer : KSerializer<DomainStatus> {
        override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("DomainStatus", PrimitiveKind.STRING)

        override fun deserialize(decoder: Decoder): DomainStatus {
            return when (val value = decoder.decodeString().uppercase(Locale.getDefault())) {
                "ENABLE" -> ENABLE
                "PAUSE" -> PAUSE
                "SPAM" -> SPAM
                else -> throw IllegalArgumentException("Unknown DomainStatus: $value")
            }
        }

        override fun serialize(encoder: Encoder, value: DomainStatus) {
            encoder.encodeString(value.name)
        }
    }
}