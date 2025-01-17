package aoba.isp.ctyun

import aoba.type.AobaBusinessException
import kotlinx.serialization.Serializable
import okhttp3.Response

@Serializable
data class CtResp(
    val code: Int,
    val message: String
)

class CtException(
    val resp: CtResp,
    response: Response,
) : AobaBusinessException(response, "请求 ${response.request.url} 失败: ${resp.code}, ${resp.message}")