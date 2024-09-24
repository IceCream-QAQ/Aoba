package aoba.isp.tencent.dnspod.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DomainCountInfo(
    @SerialName("AllTotal")
    val allTotal: Int,
    @SerialName("DomainTotal")
    val domainTotal: Int,
    @SerialName("ErrorTotal")
    val errorTotal: Int,
    @SerialName("GroupTotal")
    val groupTotal: Int,
    @SerialName("LockTotal")
    val lockTotal: Int,
    @SerialName("MineTotal")
    val mineTotal: Int,
    @SerialName("PauseTotal")
    val pauseTotal: Int,
    @SerialName("ShareOutTotal")
    val shareOutTotal: Int,
    @SerialName("ShareTotal")
    val shareTotal: Int,
    @SerialName("SpamTotal")
    val spamTotal: Int,
    @SerialName("VipExpire")
    val vipExpire: Int,
    @SerialName("VipTotal")
    val vipTotal: Int
)