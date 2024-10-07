package aoba.isp.ctyun.cdn

import aoba.`fun`.*
import aoba.isp.ct.cdn.Domain
import aoba.type.AobaRequestException
import kotlinx.serialization.json.decodeFromJsonElement
import kotlinx.serialization.json.jsonObject
import okhttp3.Response
import java.util.*

class CtCDN(
    private val secretId: String,
    secretKey: String,
    private val web: Web = Web()
) {

    val domain by lazy { Domain(this) }
    val cert by lazy { Cert(this) }
    val statistic by lazy { Statistic(this) }

    private val secretKey = Base64.getDecoder().decode(secretKey)

    companion object {
        private const val host = "open.ctcdn.cn"
        private const val ac = "app"
    }

    suspend inline fun <reified T> get(path: String, params: Map<String, Any?>) =
        doRequest<T>(params.entries.joinToString("&", "$path?") { (k, v) -> "$k=$v" })

    suspend inline fun <reified T> get(path: String, params: BuilderMap<String, Any>.() -> Unit) =
        get<T>(path, builderMap(params))

    suspend inline fun <reified T> post(path: String, block: JsonBuilder.() -> Unit) =
        doRequest<T>(path, buildJson(block))


    suspend inline fun <reified T> doRequest(path: String, body: String? = null): T {
        val response = request(path, body)
        if (!response.isSuccessful) throw AobaRequestException(response)
        val jo = json.parseToJsonElement(response.body!!.string()).jsonObject
//        val code = jo["code"]?.jsonPrimitive?.intOrNull ?: -1
//        if (code != 100000) throw CtException(CtResp(code, jo["message"]?.toString() ?: "未检出"), response)
        return json.decodeFromJsonElement(jo)
    }

    suspend fun request(path: String, body: String? = null): Response {
        val time = System.currentTimeMillis()

        val tempSignature = hmac256(secretKey, "$secretId:${time / 86400000}")
        val signedData = """
            $secretId
            $time
            $path
        """.trimIndent()
        val signature = hmac256(tempSignature, signedData)
            .let { Base64.getUrlEncoder().withoutPadding().encodeToString(it) }

        return web.post {
            url("https://$host/$path")

            headers {
                "x-alogic-app" to secretId
                "x-alogic-now" to time.toString()
                "x-alogic-ac" to ac
                "x-alogic-signature" to signature
            }

            body?.let { json(it) }
        }
    }
}