package aoba.isp.aliyun

import aoba.type.AobaBusinessException
import okhttp3.Response

class AliyunBusinessException(
    response: Response,
    val error: AliyunRequestErrorInfo
) : AobaBusinessException(response, error.toString())