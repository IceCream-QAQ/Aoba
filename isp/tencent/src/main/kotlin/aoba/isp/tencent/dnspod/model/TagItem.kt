package aoba.isp.tencent.dnspod.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TagItem(
    @SerialName("TagKey")
    val key: String,
    @SerialName("TagValue")
    val value: String
)