package aoba.isp.tencent.sms

import aoba.isp.tencent.TencentCloud
import aoba.isp.tencent.sms.send.SendSMS

class Sms(private val qCloud: TencentCloud) {

    val send by lazy { SendSMS(qCloud) }

}