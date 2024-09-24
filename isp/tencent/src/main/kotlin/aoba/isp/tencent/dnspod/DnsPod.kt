package aoba.isp.tencent.dnspod

import aoba.isp.tencent.TencentCloud


class DnsPod(
    private val qCloud: TencentCloud
) {
    val domain by lazy { Domain(qCloud) }
    val record by lazy { Record(qCloud) }

}