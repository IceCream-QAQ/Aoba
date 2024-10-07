package aoba.isp.ctyun.cdn

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName


class Cert(private val ct: CtCDN) {

    @Serializable
    data class CreateResp(
        @SerialName("code")
        val code: Int,
        @SerialName("id")
        val id: Int,
        @SerialName("message")
        val message: String
    )

    /*** 创建证书
     * @param name 证书名称
     * @param key 证书私钥
     * @param cert 证书公钥
     * @param email 通知邮箱
     *
     * @see <a href="https://vip.ctcdn.cn/help/10005260/10014505/common/10014506">创建证书</a>
     */
    suspend fun create(
        name: String,
        key: String,
        cert: String,
        email: String? = null,
    ) = ct.post<CreateResp>(
        "/v1/cert/create"
    ) {
        "name" to name
        "key" to key
        "cert" to cert
        "email" to email
    }

    @Serializable
    data class QueryResp(
        @SerialName("code")
        val code: Int,
        @SerialName("message")
        val message: String,
        @SerialName("result")
        val result: Result
    ) {
        @Serializable
        data class Result(
            @SerialName("certs")
            val certs: String,
            @SerialName("cn")
            val cn: String,
            @SerialName("created")
            val created: Int,
            @SerialName("expires")
            val expires: Int,
            @SerialName("id")
            val id: Int,
            @SerialName("issue")
            val issue: Int,
            @SerialName("issuer")
            val issuer: String,
            @SerialName("key")
            val key: String,
            @SerialName("name")
            val name: String,
            @SerialName("sans")
            val sans: List<String>,
            @SerialName("usage_mode")
            val usageMode: Int
        )
    }

    /*** 查询证书
     *
     * name和id必填其中一个，若都填返回name查询结果
     *
     * @param name 证书备注名
     * @param id 证书ID
     * @param usageMode 筛选项，0（加速域名的证书和私钥），1（客户端CA链证书）2（源站CA链证书），3（CDN回源节点证书和私钥），4（加速域名的证书和私钥（国密）），其中1、2、3是双向认证场景使用的证书
     * @see <a href="https://vip.ctcdn.cn/help/10005260/10014505/common/10014508">查询证书</a>
     */
    suspend fun query(name: String? = null, id: Int? = null, usageMode: Int? = null): QueryResp {
        if (name == null && id == null) error("name 和 id 不能同时为空")
        return ct.get<QueryResp>("/v1/cert/query") {
            "name" to name
            "id" to id
            "usage_mode" to usageMode
        }
    }

    /*** 删除证书
     * @param name 证书备注名
     *
     * @see <a href="https://vip.ctcdn.cn/help/10005260/10014505/common/10014742?">删除证书</a>
     */
    suspend fun delete(name: String) = ct.post<CreateResp>("/v1/cert/delete") { "name" to name }

    @Serializable
    data class CertDomainsResp(
        @SerialName("code")
        val code: Int,
        @SerialName("message")
        val message: String,
        @SerialName("result")
        val result: List<String>
    )

    /*** 查询证书关联的域名
     * @param name 证书备注名
     *
     * @see <a href="https://vip.ctcdn.cn/help/10005260/10014505/common/10014507">查询证书关联的域名</a>
     */
    suspend fun certDomains(name: String) = ct.get<CertDomainsResp>("/v1/cert/list_domain_by_cert") { "name" to name }


}