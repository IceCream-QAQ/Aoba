package aoba.isp.volcengine.cdn

import aoba.isp.volcengine.VolcEngine

class Cdn(
    private val ve: VolcEngine
) {
    val domain by lazy { Domain(ve) }

    enum class ServiceType(val type: String) {
        DOWNLOAD("download"),
        WEB("web"),
        VIDEO("video")
    }

    enum class Status(val status: String) {
        ONLINE("online"),
        OFFLINE("offline"),
        CONFIG("config")
    }

}