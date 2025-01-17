package aoba.isp.tencent.dnspod

import aoba.dns.RecordType
import aoba.`fun`.buildJson
import aoba.isp.tencent.TencentCloud
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

class Record(private val qCloud: TencentCloud) {

    @Serializable
    data class AddRecordResp(
        @SerialName("RequestId")
        val recordId: String,
        @SerialName("RecordId")
        val requestId: String
    )

    suspend fun addRecord(
        domain: String,
        subDomain: String,
        recordType: RecordType,
        value: String,
        recordLine: String = "默认",
        ttl: Int? = null,
        weight: Int? = null,
        mx: Int? = null,
        remark: String? = null,
    ) = qCloud.post<AddRecordResp>(
        "dnspod",
        "CreateRecord",
        "",
        "2021-03-23",
        buildJson {
            "Domain" to domain
            "SubDomain" to subDomain
            "RecordType" to recordType.value
            "Value" to value
            "TTL" to ttl
            "RecordLine" to recordLine
            "Weight" to weight
            "MX" to mx
            "Remark" to remark
        }
    )

}