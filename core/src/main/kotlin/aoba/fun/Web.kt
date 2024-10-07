package aoba.`fun`

import kotlinx.coroutines.CompletableDeferred
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import java.io.IOException

@JvmInline
value class Web(
    private val client: OkHttpClient = OkHttpClient()
) {

    class RequestBuilder(url: String) {
        @JvmInline
        value class HeaderBuilder(private val builder: Request.Builder) {
            infix fun String.to(that: String) = builder.addHeader(this, that)
        }
        private val rb: Request.Builder = Request.Builder()

        init {
            if (url != "") rb.url(url)
        }

        fun headers(body: HeaderBuilder.() -> Unit) {
            HeaderBuilder(rb).apply(body)
        }

        fun url(url: String) = rb.url(url)
        fun json(body: String) = rb.post(body.toRequestBody("application/json".toMediaType()))
        fun form() = Unit
        fun formData() = Unit

        fun build() = rb.build()
    }



    suspend fun execute(request: Request): Response {
        val wait = CompletableDeferred<Response>()
        client.newCall(request).enqueue(object : okhttp3.Callback {
            override fun onFailure(call: okhttp3.Call, e: IOException) {
                wait.completeExceptionally(e)
            }

            override fun onResponse(call: okhttp3.Call, response: Response) {
                wait.complete(response)
            }
        })
        return wait.await()
    }

    suspend fun get(url: String, body: RequestBuilder.() -> Unit) = execute(RequestBuilder(url).apply(body).build())

    suspend inline fun post(url: String = "", body: RequestBuilder.() -> Unit) =
        execute(RequestBuilder(url).apply(body).build())
}

