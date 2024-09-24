package aoba.isp.cloudflare.turnstile

import aoba.`fun`.Web
import aoba.`fun`.buildJson
import aoba.`fun`.json
import aoba.type.AobaRequestException
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

class Turnstile(
    private val secret: String,
    private val web: Web = Web()
) {

    @Serializable
    data class SiteVerifyResp(
        @SerialName("action")
        val action: String? = null,
        @SerialName("cdata")
        val cdata: String? = null,
        @SerialName("challenge_ts")
        val challengeTs: String? = null,
        @SerialName("error-codes")
        val errorCodes: List<String>,
        @SerialName("hostname")
        val hostname: String? = null,
        @SerialName("success")
        val success: Boolean
    )

    suspend fun siteVerify(
        response: String,
        remoteIp: String? = null,
        idempotencyKey: String? = null
    ): SiteVerifyResp {
        val resp = web.post("https://challenges.cloudflare.com/turnstile/v0/siteverify") {
            json(buildJson {
                "secret" to secret
                "response" to response
                "remoteip" to remoteIp
                "idempotency_key" to idempotencyKey
            })
        }
        if (!resp.isSuccessful) throw AobaRequestException(resp)
        return resp.body
            ?.string()
            ?.let { json.decodeFromString<SiteVerifyResp>(it) }
            ?: throw AobaRequestException(resp)
    }

}