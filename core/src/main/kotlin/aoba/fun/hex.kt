package aoba.`fun`

import java.nio.charset.StandardCharsets
import java.security.MessageDigest
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec
import javax.xml.bind.DatatypeConverter

fun hmac256(key: ByteArray, msg: String): ByteArray {
    val mac = Mac.getInstance("HmacSHA256")
    val secretKeySpec = SecretKeySpec(key, mac.algorithm)
    mac.init(secretKeySpec)
    return mac.doFinal(msg.toByteArray(StandardCharsets.UTF_8))
}

fun sha256Hex(s: String): String {
    val md = MessageDigest.getInstance("SHA-256")
    val d = md.digest(s.toByteArray(StandardCharsets.UTF_8))
    return DatatypeConverter.printHexBinary(d).toLowerCase()
}

fun sign(secretSigning: ByteArray, stringToSign: String) =
    DatatypeConverter.printHexBinary(hmac256(secretSigning, stringToSign)).toLowerCase()