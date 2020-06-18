package com.johnny.meet_kotlin.test

import java.util.*
import kotlin.properties.Delegates

/**
 * @author Johnny
 */

class Test {
    @get:Synchronized
    @set:Synchronized
    var name = "张三"

    @Synchronized
    fun foo() {

    }
}

class RunnableImpl : Runnable {

    val name: String by Delegates.notNull()
    lateinit var name1: String

    init {
        name1 = ""
    }

    override fun run() {
        //...
        if (::name1.isInitialized) {

        }
    }
}

fun main(args: Array<String>) {
    val list = listOf("bdfdf", "acverfd", "acvdfdf", "cdfdfd", "dfddfdf")
    list.filter { it.startsWith("a") }
        .sortedBy { it }
        .map { it.toUpperCase(Locale.ROOT) }
        .forEach { println(it) }
}