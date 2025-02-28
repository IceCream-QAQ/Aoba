package aoba.isp.volcengine

import aoba.`fun`.*
import aoba.isp.volcengine.cdn.Cdn
import aoba.type.AobaRequestException
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.decodeFromJsonElement
import okhttp3.Response
import java.text.SimpleDateFormat
import java.util.*
import kotlin.concurrent.getOrSet

class VolcEngine(
    private val secretId: String,
    secretKey: String,
    private val web: Web = Web()
) {

    private val secretKey = secretKey.toByteArray()

    val cdn by lazy { Cdn(this) }


    companion object {

        private val sdfThreadLocal = ThreadLocal<SimpleDateFormat>()
        private val sdf
            get() =
                sdfThreadLocal.getOrSet {
                    SimpleDateFormat("yyyyMMdd'T'HHmmss'Z'").apply { timeZone = SimpleTimeZone(0, "GMT") }
                }
    }


    suspend fun createRequest(
        prod: String,
        region: String,
        query: String,
        body: String,
        host: String
    ): Response {
        val time = System.currentTimeMillis()
        val sdfDatetime = sdf.format(Date(time))
        val sdfDate = sdfDatetime.substring(0, 8)

        val kDate = hmac256(secretKey, sdfDate)
        val kRegion = hmac256(kDate, region)
        val kService = hmac256(kRegion, prod)
        val kSigning = hmac256(kService, "request")


        val credentialScope = "$sdfDate/$region/$prod/request"
        val canonicalRequest = """
            POST
            /
            $query
            content-type:application/json; charset=utf-8
            host:$host

            content-type;host
            ${sha256Hex(body)}
        """.trimIndent()
        val stringToSign = """
            HMAC-SHA256
            $sdfDatetime
            $credentialScope
            ${sha256Hex(canonicalRequest)}
        """.trimIndent()

        val signature = hexStringL(hmac256(kSigning, stringToSign))
        val authorization =
            "HMAC-SHA256 Credential=$secretId/$credentialScope, SignedHeaders=content-type;host, Signature=$signature"

        return web.post {
            url("https://$host/?$query")

            headers {
                "X-Date" to sdfDatetime
                "Authorization" to authorization
//                "X-Algorithm" to "HMAC-SHA256"
//                "X-Credential" to "$secretId/$sdfDate/$region/$prod/request"
//                "X-SignedHeaders" to "content-type;host"
//                "X-Signature" to hmac256(kSigning, stringToSign)
            }

            json(body)
        }
    }

    suspend inline fun <reified T> post(
        prod: String,
        region: String,
        action: String,
        version: String,
        body: String,
        host: String = "${prod.lowercase(Locale.getDefault())}.volcengineapi.com"
    ): T {
        val query = "Action=$action&Version=$version"

        val resp = createRequest(prod, region, query, body, host)
        if (!resp.isSuccessful) throw AobaRequestException(resp)
        val respBody = resp.body
            ?.string()
            ?.let { json.parseToJsonElement(it) as? JsonObject }

        if (respBody == null) throw AobaRequestException(resp)

        val metadata = respBody["ResponseMetadata"] as? JsonObject ?: throw AobaRequestException(resp)
        if (metadata["Error"] != null) {
            val error = metadata["Error"] as JsonObject
            throw VolcEngineBusinessException(
                resp,
                VolcEngineRequestErrorInfo(
                    metadata["RequestId"]?.toString() ?: "未提供",
                    error["Code"]?.toString() ?: "未提供",
                    error["Message"]?.toString() ?: "未提供",
                )
            )
        }

        return (respBody["Result"] as? JsonObject)
            ?.let { json.decodeFromJsonElement(it) }
            ?: Unit as T
    }
}