package aoba.isp.tencent

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TencentRequestErrorInfo(
    @SerialName("RequestId")
    val requestId: String,
    @SerialName("Code")
    val code: String,
    @SerialName("Message")
    val message: String
)