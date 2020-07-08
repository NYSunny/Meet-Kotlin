package com.johnny.base.utils

import okhttp3.internal.toHexString
import java.lang.Exception
import java.security.MessageDigest

/**
 * @author Johnny
 */

fun sha1(data: String): String {
    val sb = StringBuilder()
    return try {
        val md = MessageDigest.getInstance("SHA1")
        md.update(data.toByteArray())
        val bits: ByteArray = md.digest()
        for (bit in bits) {
            var value: Int = bit.toInt()
            if (value < 0) value += 256
            if (value < 16) sb.append('0')
            sb.append(value.toHexString())
        }
        sb.toString()
    } catch (_: Exception) {
        ""
    }
}