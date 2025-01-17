package aoba.isp.aliyun.dns

import aoba.dns.RecordType
import aoba.`fun`.buildJson
import aoba.`fun`.stringTreeMap
import aoba.isp.aliyun.Aliyun
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

class Record(
    private val aliyun: Aliyun
) {
    @Serializable
    data class AddRecordResp(
        @SerialName("RecordId")
        val recordId: String,
        @SerialName("RequestId")
        val requestId: String
    )

    suspend fun addRecord(
        domain: String,
        sub: String,
        type: RecordType,
        value: String,
        ttl: Long? = null,
        priority: Long? = null,
        line: String? = null,
    ) = aliyun.post<AddRecordResp>(
        "alidns.cn-hangzhou.aliyuncs.com",
        "AddDomainRecord",
        "2015-01-09",
        stringTreeMap {
            "DomainName" to domain
            "RR" to sub
            "Type" to type.value
            "Value" to value
            "TTL" to ttl
            "Priority" to priority
            "Line" to line
        },
        buildJson {
            "DomainName" to domain
            "RR" to sub
            "Type" to type.value
            "Value" to value
            "TTL" to ttl
            "Priority" to priority
            "Line" to line
        }
    )
}