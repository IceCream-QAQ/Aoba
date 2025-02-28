package aoba.isp.volcengine

import aoba.type.AobaBusinessException
import okhttp3.Response

class VolcEngineBusinessException(
    response: Response,
    val error: VolcEngineRequestErrorInfo
) : AobaBusinessException(response, error.toString())