package aoba.`fun`

import kotlinx.coroutines.CompletableDeferred
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import okio.BufferedSink
import java.io.IOException

@JvmInline
value class Web(
    private val client: OkHttpClient = OkHttpClient()
) {
    object EmptyBody : RequestBody() {
        override fun contentType(): MediaType? = null
        override fun contentLength(): Long = 0
        override fun writeTo(sink: BufferedSink) {}
    }

    @JvmInline
    value class RequestBuilder(private val rb: Request.Builder = Request.Builder()) {
        @JvmInline
        value class HeaderBuilder(private val builder: Request.Builder) {
            infix fun String.to(that: String) = builder.addHeader(this, that)
        }

        constructor(url: String) : this(Request.Builder().url(url))

        fun headers(body: HeaderBuilder.() -> Unit) {
            HeaderBuilder(rb).apply(body)
        }

        fun url(url: String) = rb.url(url)

        fun json(body: String, method: String = "POST") =
            rb.method(method, body.toRequestBody("application/json".toMediaType()))

        fun method(method: String, body: RequestBody = EmptyBody) =
            rb.method(method, body)

        fun form() = Unit
        fun formData() = Unit

        fun build() = rb.build()
    }

    suspend fun execute(request: Request): Response {
        val wait = CompletableDeferred<Response>()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                wait.completeExceptionally(e)
            }

            override fun onResponse(call: Call, response: Response) {
                wait.complete(response)
            }
        })
        return kotlin.runCatching { wait.await() }.getOrElse { throw IllegalStateException("Web 请求错误", it) }
    }

    suspend fun get(url: String, body: RequestBuilder.() -> Unit) = execute(RequestBuilder(url).apply(body).build())

    suspend inline fun post(url: String = "", body: RequestBuilder.() -> Unit) =
        execute(RequestBuilder(url).apply(body).build())
}

