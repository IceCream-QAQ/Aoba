package aoba.isp.ctyun.cdn

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


class Statistic(private val ct: CtCDN) {

    @Serializable
    data class SummaryResp(
        @SerialName("code")
        val code: Int,
        @SerialName("end_time")
        val endTime: Int,
        @SerialName("interval")
        val interval: String,
        @SerialName("message")
        val message: String,
        @SerialName("req_summary_data_interval")
        val reqSummaryDataInterval: List<ReqSummaryDataInterval>,
        @SerialName("start_time")
        val startTime: Int
    ) {
        @Serializable
        data class ReqSummaryDataInterval(
            @SerialName("time_stamp")
            val timeStamp: Int,
            @SerialName("busi_type")
            val busiType: Int? = null,
            val province: Int? = null,
            val isp: String? = null,
            @SerialName("network_layer_protocol")
            val network: String? = null,
            @SerialName("application_layer_protocol")
            val application: String? = null,
            val abroad: Int? = null,
            @SerialName("request_num")
            val requestNum: Int,
            @SerialName("hit_request_rate")
            val hitRequestRate: Double,
            val qps: Double,
            @SerialName("hit_flow")
            val hitFlow: Int,
            @SerialName("flow")
            val flow: Int,
            @SerialName("hit_flow_rate")
            val hitFlowRate: Double,
            @SerialName("bandwidth")
            val bandwidth: Double,
        )
    }

    /** 查询整体统计数据
     *
     * - 单个用户一分钟限制调用10000次，并发不超过100；
     * - 单次查询输入域名的个数不能超过100个；
     * - 时间粒度为24h时，查询开始时间与结束时间需要跨天，否则查询的数据为空；
     * - 最大返回记录50000条记录；
     * - 若查询结束时间不包含在该批次的最后一秒，默认end_time为该批次最后一秒，例如：时间粒度为1h，end_time设置为17:30对应的时间戳，此时end_time默认成17:59:59；
     * - 时间片统计数据均为前打点，假如请求5分钟粒度数据，time_stamp= "2021-10-13 00:00"对应的时间戳，表示统计的数据为时间区间[2021-10-13 00:00, 2021-10-13 00:05)；
     * - 根据请求参数时间粒度（Interval）和聚合维度（group_by）个数的不同，单次请求可查询历史数据范围，数据延迟，对应如下，若开始时间超过可查询历史数据时间范围，超过部分的数据为0；
     *
     * 时间粒度|可查询历史数据时间范围|数据延迟|单次可查询的时间跨度
     * ---|---|---|---
     * 1m|最近7天|5分钟|1小时
     * 5m|最近365天|20分钟|5小时
     * 1h|最近365天|20分钟|1天
     * 24h|最近365天|20分钟|31天
     *
     * @param startTime 开始时间戳
     * @param endTime 结束时间戳
     * @param product 产品类型
     * @param interval 时间粒度
     * @param busiType 业务类型
     * @param domain 域名
     * @param province 省份
     * @param isp 运营商
     * @param network 网络层协议
     * @param application 应用层协议
     * @param abroad 区域
     * @param groupBy 结果聚合维度
     * @param continentCode 大洲
     * @param continentRegionCode 大洲区域
     *
     * @see <a href="https://vip.ctcdn.cn/help/10005260/10014167/common/10015602">查询整体统计数据</a>
     */
    suspend fun summary(
        startTime: Int,
        endTime: Int,
        product: CdnProduct,
        interval: String? = null,
        busiType: List<Int>? = null,
        domain: String? = null,
        province: List<Int>? = null,
        isp: List<Int>? = null,
        network: String? = null,
        application: String? = null,
        abroad: Int? = null,
        groupBy: String? = null,
        continentCode: List<Int>? = null,
        continentRegionCode: List<Int>? = null,
    ) = ct.post<SummaryResp>("/v2/statisticsanalysis/query_summary_data") {
        "start_time" to startTime
        "end_time" to endTime
        "product_type" to product.value
        "interval" to interval
        "busi_type" to busiType
        "domain" to domain
        "province" to province
        "isp" to isp
        "network_layer_protocol" to network
        "application_layer_protocol" to application
        "abroad" to abroad
        "group_by" to groupBy
        "continent_code" to continentCode
        "continent_region_code" to continentRegionCode
    }

}