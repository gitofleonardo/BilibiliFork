package com.hhvvg.bilibilifork.network

import java.util.regex.Pattern

fun parseCookies(cookie: String): Map<String, String> {
    val cookies = cookie.split(";")
    val pattern = Pattern.compile("([\\S]+)=([\\S]+)")
    val map = HashMap<String, String>()
    for (ck in cookies) {
        val matcher = pattern.matcher(ck.trim())
        if (matcher.matches()) {
            val key = matcher.group(1)!!
            val value = matcher.group(2)!!
            map[key] = value
        }
    }
    return map
}