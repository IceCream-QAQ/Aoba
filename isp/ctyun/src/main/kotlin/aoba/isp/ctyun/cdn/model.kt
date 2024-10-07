package aoba.isp.ctyun.cdn

enum class CdnProduct(val value: String) {
    // 静态加速
    Static("001"),
    // 下载加速
    Download("003"),
    // 视频点播加速
    Video("004"),
    // CDN加速
    Cdn("008"),
    // 全站加速
    All("006"),
    // 安全加速
    Waf("007"),
    // 下载加速闲时
    DownloadIdle("014"),
}