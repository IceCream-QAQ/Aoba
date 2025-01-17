package aoba.isp.aliyun.sms

import aoba.isp.aliyun.Aliyun
import aoba.isp.aliyun.sms.send.SendSMS

class Sms(private val aliyun: Aliyun) {

    val send by lazy { SendSMS(aliyun) }

}