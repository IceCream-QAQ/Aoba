package aoba.isp.aliyun

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AliyunRequestErrorInfo(
    @SerialName("RequestId")
    val requestId: String,
    @SerialName("Code")
    val code: String,
    @SerialName("Message")
    val message: String
)