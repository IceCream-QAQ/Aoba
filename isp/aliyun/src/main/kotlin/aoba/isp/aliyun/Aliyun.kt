package aoba.isp.aliyun

import aoba.`fun`.*
import aoba.isp.aliyun.dns.Dns
import aoba.isp.aliyun.sms.Sms
import aoba.type.AobaRequestException
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.decodeFromJsonElement
import okhttp3.Response
import java.net.URLEncoder
import java.text.SimpleDateFormat
import java.util.*
import kotlin.concurrent.getOrSet

class Aliyun(
    private val secretId: String,
    secretKey: String,
    private val web: Web = Web()
) {

    private val secretKey = secretKey.toByteArray()

    val dns by lazy { Dns(this) }
    val sms by lazy { Sms(this) }

    companion object {

        private val sdfThreadLocal = ThreadLocal<SimpleDateFormat>()
        private val sdf
            get() =
                sdfThreadLocal.getOrSet {
                    SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").apply { timeZone = SimpleTimeZone(0, "GMT") }
                }
    }


    suspend fun createRequest(
        host: String,
        action: String,
        version: String,
        query: TreeMap<String, String>? = null,
        body: String? = null
    ): Response {
        val sha256Body = body?.let { sha256Hex(it) } ?: ""
        val queryN = query?.entries?.joinToString("&") { (k, v) -> "$k=$v" } ?: ""
        val queryStr = query?.entries?.joinToString("&") { (k, v) -> "${percentCode(k)}=${percentCode(v)}" } ?: ""
        val sdfTime = sdf.format(Date())
        val requestId = UUID.randomUUID().toString()
        val signedHeaders =
            "content-type;host;x-acs-action;x-acs-content-sha256;x-acs-date;x-acs-signature-nonce;x-acs-version"
        val canonicalRequest = """
            POST
            /
            $queryStr
            content-type:application/json; charset=utf-8
            host:$host
            x-acs-action:$action
            x-acs-content-sha256:$sha256Body
            x-acs-date:$sdfTime
            x-acs-signature-nonce:$requestId
            x-acs-version:$version

            $signedHeaders
            $sha256Body
        """.trimIndent()

        val stringToSign = """
            ACS3-HMAC-SHA256
            ${sha256Hex(canonicalRequest)}
        """.trimIndent()

        val signature = sign(secretKey, stringToSign)

        val authorization =
            "ACS3-HMAC-SHA256 Credential=$secretId,SignedHeaders=$signedHeaders, Signature=$signature"

        return web.post {
            url("https://$host/?$queryN")

            headers {
                "x-acs-action" to action
                "x-acs-version" to version
                "Authorization" to authorization
                "x-acs-signature-nonce" to requestId
                "x-acs-date" to sdfTime
                "x-acs-content-sha256" to sha256Body
            }

            json(body ?: "")
        }
    }

    suspend inline fun <reified T> post(
        host: String,
        action: String,
        version: String,
        query: TreeMap<String, String>? = null,
        body: String? = null
    ): T {
        val resp = createRequest(host, action, version, query, body)
        if (!resp.isSuccessful) throw AobaRequestException(resp)
        val respBody = resp.body
            ?.string()
            ?.let { json.parseToJsonElement(it) as? JsonObject }
            ?: error("Response body is null")

//        val code = respBody["Code"]?.toString()
//        if (code != "OK" && code != "\"OK\"")
//            throw AliyunBusinessException(
//                resp,
//                AliyunRequestErrorInfo(
//                    respBody["RequestId"]?.toString() ?: "未提供",
//                    respBody["Code"]?.toString() ?: "未提供",
//                    respBody["Message"]?.toString() ?: "未提供",
//                )
//            )

        return json.decodeFromJsonElement(respBody)
    }

    private fun percentCode(str: String) = URLEncoder.encode(str, "UTF-8").replace("+", "%20").replace("*", "%2A")
}