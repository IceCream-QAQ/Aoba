package aoba.`fun`

import java.nio.charset.StandardCharsets
import java.security.MessageDigest
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

fun hmac256(key: ByteArray, msg: String): ByteArray {
    val mac = Mac.getInstance("HmacSHA256")
    val secretKeySpec = SecretKeySpec(key, mac.algorithm)
    mac.init(secretKeySpec)
    return mac.doFinal(msg.toByteArray(StandardCharsets.UTF_8))
}

fun sha256Hex(s: String): String {
    val md = MessageDigest.getInstance("SHA-256")
    val d = md.digest(s.toByteArray(StandardCharsets.UTF_8))
    return hexStringL(d)
}

fun sign(secretSigning: ByteArray, stringToSign: String) =
    hexStringL(hmac256(secretSigning, stringToSign))

private val hexCodeU = "0123456789ABCDEF".toCharArray()
private val hexCodeL = "0123456789abcdef".toCharArray()

fun hexStringU(data: ByteArray): String =
    hexString(data, hexCodeU)

fun hexStringL(data: ByteArray): String =
    hexString(data, hexCodeL)

private fun hexString(data: ByteArray, hexCode: CharArray): String {
    val r = StringBuilder(data.size * 2)
    for (b in data) {
        r.append(hexCode[(b.toInt() shr 4) and 0xF])
        r.append(hexCode[(b.toInt() and 0xF)])
    }
    return r.toString()
}