package com.johnny.base

/**
 * @author Johnny
 */

object DO {
    const val a = 1
}

inline fun <T, R> T.doo(block: T.() -> R): R = block()