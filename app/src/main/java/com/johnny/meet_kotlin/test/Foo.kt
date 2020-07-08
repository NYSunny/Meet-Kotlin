package com.johnny.meet_kotlin.test

/**
 * @author Johnny
 */

fun main(args: Array<String>) {
    val name: String? = null
    name?.let {
        println(it)
    } ?: Unit.apply {
        val a = 5
        val b = 6
        println("a + b = ${a + b}")
    }

    name as? String
}

class Foo {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val name: String? = null
            name?.let {
                println(it)
            } ?: println("name = null")
        }
    }
}