package aoba.isp.tencent.dnspod

import aoba.`fun`.buildJson
import aoba.isp.tencent.TencentCloud
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

class Record(private val qCloud: TencentCloud) {

    enum class RecordType(val value: String) {
        A("A"),
        CNAME("CNAME"),
        MX("MX"),
        TXT("TXT"),
        AAAA("AAAA"),
        NS("NS"),
        CAA("CAA"),
        SRV("SRV"),
        HTTPS("HTTPS"),
        SVCB("SVCB"),
        SPF("SPF")
    }

    @Serializable
    data class AddRecordResp(
        @SerialName("RequestId")
        val recordId: Int,
        @SerialName("RecordId")
        val requestId: String
    )

    suspend fun addRecord(
        domain: String,
        subDomain: String,
        recordType: RecordType,
        value: String,
        ttl: Int = 600,
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
            "RecordLine" to "默认"
            "WEIGHT" to weight
            "MX" to mx
            "Remark" to remark
        }
    )

}