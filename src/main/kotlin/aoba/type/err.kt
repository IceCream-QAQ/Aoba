package aoba.type

import okhttp3.Response

class AobaRequestException(
    val response: Response
) : Exception("请求出错，Url: ${response.request.url}，状态码: ${response.code}。")

open class AobaBusinessException(
    val response: Response,
    error: String
) : Exception("业务出错，Url: ${response.request.url}，状态码: ${response.code}，异常信息: $error。")