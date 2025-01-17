package aoba.isp.aliyun.dns

import aoba.isp.aliyun.Aliyun

class Dns(
    private val aliyun: Aliyun
) {
    val record by lazy { Record(aliyun) }
    val domain by lazy { Domain(aliyun) }
}