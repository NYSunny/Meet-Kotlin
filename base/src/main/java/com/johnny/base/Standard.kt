package com.johnny.base

/**
 * @author Johnny
 */

object DO

inline fun <R> DO.doo(block: () -> R): R = block()