package aoba.isp.ctyun.cdn

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

enum class CdnProduct(val value: String) {
    // 静态加速
    Static("001"),

    // 下载加速
    Download("003"),

    // 视频点播加速
    Video("004"),

    // CDN加速
    Cdn("008"),

    // 全站加速
    All("006"),

    // 安全加速
    Waf("007"),

    // 下载加速闲时
    DownloadIdle("014");

    object Serializer : KSerializer<CdnProduct> {
        override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("CdnProduct", PrimitiveKind.STRING)

        override fun deserialize(decoder: Decoder): CdnProduct = when (val value = decoder.decodeString()) {
            "001" -> Static
            "003" -> Download
            "004" -> Video
            "008" -> Cdn
            "006" -> All
            "007" -> Waf
            "014" -> DownloadIdle
            else -> throw IllegalArgumentException("Unknown CdnProduct: $value")
        }

        override fun serialize(encoder: Encoder, value: CdnProduct) {
            encoder.encodeString(value.value)
        }
    }
}


enum class CdnArea(val value: Int) {
    // 中国大陆
    ChineseMainland(1),

    // 海外
    Overseas(2),

    // 全球
    Global(3);

    object Serializer : KSerializer<CdnArea> {
        override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("CdnArea", PrimitiveKind.STRING)
        override fun deserialize(decoder: Decoder) = when (val value = decoder.decodeInt()) {
            1 -> ChineseMainland
            2 -> Overseas
            3 -> Global
            else -> error("Unknown value: $value")
        }

        override fun serialize(encoder: Encoder, value: CdnArea) {
            encoder.encodeInt(value.value)
        }
    }
}

enum class CdnStatus(val value: Int) {
    // 审核中
    Review(1),

    // 审核成功
    ReviewComplete(2),

    // 配置中
    Configuring(3),

    // 已启用
    Enabled(4),

    // 停止中
    Stopping(5),

    // 已停用
    Disabled(6),

    // 删除中
    Deleting(7),

    // 已删除
    Deleted(8),

    // 审核失败
    ReviewFailed(9),

    // 配置失败
    ConfiguringFailed(10),

    // 停止失败
    StoppingFailed(11),

    // 删除失败
    DeletingFailed(12);

    object Serializer : KSerializer<CdnStatus> {
        override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("CdnStatus", PrimitiveKind.STRING)

        override fun deserialize(decoder: Decoder): CdnStatus = when (val value = decoder.decodeInt()) {
            1 -> Review
            2 -> ReviewComplete
            3 -> Configuring
            4 -> Enabled
            5 -> Stopping
            6 -> Disabled
            7 -> Deleting
            8 -> Deleted
            9 -> ReviewFailed
            10 -> ConfiguringFailed
            11 -> StoppingFailed
            12 -> DeletingFailed
            else -> throw IllegalArgumentException("Unknown CdnStatus: $value")
        }

        override fun serialize(encoder: Encoder, value: CdnStatus) {
            encoder.encodeInt(value.value)
        }
    }
}

@Serializable
data class CdnOrigin(
    // 源站ip或域名
    val origin: String,
    // 源站端口
    val port: Int,
    // 权重
    val weight: Int,
    // 源站角色
    val role: String,
//    val protocol: String?
)