package aoba.isp.tencent

import aoba.`fun`.*
import aoba.isp.tencent.dnspod.DnsPod
import aoba.type.AobaRequestException
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.decodeFromJsonElement
import okhttp3.Response
import java.text.SimpleDateFormat
import java.util.*
import kotlin.concurrent.getOrSet

class TencentCloud(private val secretId: String, secretKey: String) {

    private val secretKey = "TC3$secretKey".toByteArray()

    val dnspod by lazy { DnsPod(this) }

    companion object {
        val defaultProdHostMap: MutableMap<String, String> = hashMapOf()

        private val sdfThreadLocal = ThreadLocal<SimpleDateFormat>()
        val sdf
            get() =
                sdfThreadLocal.getOrSet {
                    SimpleDateFormat("yyyy-MM-dd").apply { timeZone = TimeZone.getTimeZone("UTC") }
                }
    }

    private val web = Web()

    val prodHostMap = hashMapOf<String, String>().apply { putAll(defaultProdHostMap) }

    suspend fun createRequest(
        prod: String,
        action: String,
        region: String? = null,
        version: String,
        body: String,
        host: String
    ): Response {
        val time = System.currentTimeMillis()
        val timeInt = (time / 1000).toInt()
        val sdfTime = sdf.format(Date(time))
        val canonicalRequest = """
            POST
            /

            content-type:application/json; charset=utf-8
            host:$host

            content-type;host
            ${sha256Hex(body)}
        """.trimIndent()

        val credentialScope = "$sdfTime/$prod/tc3_request"
        val stringToSign = """
            TC3-HMAC-SHA256
            $timeInt
            $credentialScope
            ${sha256Hex(canonicalRequest)}
        """.trimIndent()

        val secretDate = hmac256(secretKey, sdfTime)
        val secretService = hmac256(secretDate, prod)
        val secretSigning = hmac256(secretService, "tc3_request")
        val signature = sign(secretSigning, stringToSign)

        val authorization =
            "TC3-HMAC-SHA256 Credential=$secretId/$credentialScope, SignedHeaders=content-type;host, Signature=$signature"

        return web.post {
            url("https://$host/")

            headers {
                "Authorization" to authorization
                "X-TC-Action" to action
                "X-TC-Timestamp" to timeInt.toString()
                "X-TC-Version" to version
                region?.let { "X-TC-Region" to it }
            }

            json(body)
        }
    }

    suspend inline fun <reified T> post(
        prod: String,
        action: String,
        region: String? = null,
        version: String,
        body: String,
        host: String = prodHostMap.getOrPut(prod) { "$prod.tencentcloudapi.com" }
    ): T {
        val resp = createRequest(prod, action, region, version, body, host)
        if (!resp.isSuccessful) throw AobaRequestException(resp)
        val respBody = resp.body
            ?.string()
            ?.let { json.parseToJsonElement(it) as? JsonObject }
            ?.let { it["Response"] as? JsonObject }
            ?: error("Response body is null")

        val respError = respBody["Error"] as? JsonObject
        respError?.let {
            throw TencentBusinessException(
                resp,
                TencentRequestErrorInfo(
                    it["Code"]?.toString() ?: "未提供",
                    it["Code"]?.toString() ?: "未提供",
                    respBody["RequestId"]?.toString() ?: "未提供",
                )
            )
        }
        return json.decodeFromJsonElement(respBody)
    }
}