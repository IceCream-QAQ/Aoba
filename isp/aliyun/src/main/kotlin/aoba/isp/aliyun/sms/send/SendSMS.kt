package aoba.isp.aliyun.sms.send

import aoba.`fun`.buildJson
import aoba.`fun`.stringTreeMap
import aoba.isp.aliyun.Aliyun
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

class SendSMS(private val aliyun: Aliyun) {

    @Serializable
    data class SendSMSResp(
        @SerialName("BizId")
        val biz: String
    )

    suspend fun sendSMS(
        /*** 下发手机号码
         * 采用 E.164 标准，格式为 + 国家或地区码 手机号，单次请求最多支持200个手机号且要求全为境内手机号或全为境外手机号。
         * 例如：+8613711112222， 其中前面有一个+号 ，86为国家码，13711112222为手机号。
         * 注：发送国内短信格式还支持0086、86或无任何国家或地区码的11位手机号码，前缀默认为+86。
         * 示例值："+8618511122233"
         */
        phoneNumbers: String,
        // 短信签名内容，使用 UTF-8 编码，必须填写已审核通过的签名
        signName: String? = null,
        // 短信模板 ID
        templateCode: String,
        // 短信模板变量对应的实际值。支持传入多个参数。
        params: String? = null,
        // 上行短信扩展码。上行短信指发送给通信服务提供商的短信，用于定制某种服务、完成查询，或是办理某种业务等，需要收费，按运营商普通短信资费进行扣费。
        extendCode: String? = null,
        // 外部流水扩展字段。
        outId: String? = null,
    ) = aliyun.post<SendSMSResp>(
        "dysmsapi.aliyuncs.com",
        "SendSms",
        "2017-05-25",
        stringTreeMap {
            "PhoneNumbers" to phoneNumbers
            "SignName" to signName
            "TemplateCode" to templateCode
            "TemplateParam" to params
            "SmsUpExtendCode" to extendCode
            "OutId" to outId
        },
        buildJson {
            "PhoneNumbers" to phoneNumbers
            "SignName" to signName
            "TemplateCode" to templateCode
            "TemplateParam" to params
            "SmsUpExtendCode" to extendCode
            "OutId" to outId
        }
    )

}