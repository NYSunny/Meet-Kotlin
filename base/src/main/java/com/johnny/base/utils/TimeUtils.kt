package com.johnny.base.utils

/**
 * @author Johnny
 */

/**
 * 毫秒转为HH:mm:ss
 * 1s = 1000ms
 * 1m = 60s
 * 1h = 60m
 * 1d = 24h
 */
fun ms2HHmmss(ms: Long): String {
    val hours = (ms % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60)
    val h = if (hours < 10) {
        "0$hours"
    } else {
        hours.toString()
    }
    val minutes = (ms % (1000 * 60 * 60)) / (1000 * 60)
    val m: String = if (minutes < 10) {
        "0$minutes"
    } else {
        minutes.toString()
    }
    val seconds = (ms % (1000 * 60)) / 1000
    val s: String = if (seconds < 10) {
        "0$seconds"
    } else {
        seconds.toString()
    }
    return "$h:$m:$s"
}