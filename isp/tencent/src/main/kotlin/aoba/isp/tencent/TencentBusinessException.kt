package aoba.isp.tencent

import aoba.type.AobaBusinessException
import okhttp3.Response

class TencentBusinessException(
    response: Response,
    val error: TencentRequestErrorInfo
) : AobaBusinessException(response, error.toString())