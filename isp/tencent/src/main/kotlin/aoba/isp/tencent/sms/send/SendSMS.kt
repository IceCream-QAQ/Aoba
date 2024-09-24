package aoba.isp.tencent.sms.send

import aoba.`fun`.buildJson
import aoba.isp.tencent.TencentCloud
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

class SendSMS(private val qCloud: TencentCloud) {

    @Serializable
    data class SendSMSResp(
        @SerialName("SendStatusSet")
        val sendStatusList: List<SendStatus>,
        @SerialName("RequestId")
        val requestId: String
    )

    @Serializable
    data class SendStatus(
        @SerialName("Code")
        val code: String,
        @SerialName("Fee")
        val fee: Int,
        @SerialName("IsoCode")
        val isoCode: String,
        @SerialName("Message")
        val message: String,
        @SerialName("PhoneNumber")
        val phoneNumber: String,
        @SerialName("SerialNo")
        val serialNo: String,
        @SerialName("SessionContext")
        val sessionContext: String
    )

    suspend fun sendSMS(
        region: String,
        /*** 下发手机号码
         * 采用 E.164 标准，格式为 + 国家或地区码 手机号，单次请求最多支持200个手机号且要求全为境内手机号或全为境外手机号。
         * 例如：+8613711112222， 其中前面有一个+号 ，86为国家码，13711112222为手机号。
         * 注：发送国内短信格式还支持0086、86或无任何国家或地区码的11位手机号码，前缀默认为+86。
         * 示例值：["+8618511122233"]
         */
        phoneNumbers: List<String>,
        // 短信 SdkAppId
        smsSdkAppId: String,
        // 短信模板 ID
        templateId: String,
        // 短信签名内容，使用 UTF-8 编码，必须填写已审核通过的签名
        signName: String? = null,
        // 模板参数，若无模板参数，则设置为空。
        params: List<String>? = null,
        // 短信码号扩展号
        extendCode: String? = null,
        // 用户的 session 内容，可以携带用户侧 ID 等上下文信息，server 会原样返回。注意长度需小于512字节。
        sessionContext: String? = null,
        // 国内短信无需填写该项；国际/港澳台短信已申请独立 SenderId 需要填写该字段，默认使用公共 SenderId，无需填写该字段。
        senderId: String? = null
    ) = qCloud.post<SendSMSResp>(
        "sms",
        "SendSms",
        region,
        "2021-01-11",
        buildJson {
            "PhoneNumberSet" to phoneNumbers
            "SmsSdkAppId" to smsSdkAppId
            "TemplateId" to templateId
            "SignName" to signName
            "TemplateParamSet" to params
            "ExtendCode" to extendCode
            "SessionContext" to sessionContext
            "SenderId" to senderId
        }
    )

}