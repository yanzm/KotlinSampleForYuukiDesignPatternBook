/*
 * Copyright (C) 2019 Yuki Anzai (@yanzm)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.yanzm.sample.flyweight20.improved

fun main(vararg args: String) {
    if (args.isEmpty()) {
        println("Usage: java Main digits")
        println("Example: java Main 1212123")
        return
    }

    BigString(args[0]).print()
}

private fun big0(): String {
    return """
        ....######......
        ..##......##....
        ..##......##....
        ..##......##....
        ..##......##....
        ..##......##....
        ....######......
        ................
        """.trimIndent()
}

private fun big1(): String {
    return """
        ......##........
        ..######........
        ......##........
        ......##........
        ......##........
        ......##........
        ..##########....
        ................
        """.trimIndent()
}

private fun big2(): String {
    return """
        ....######......
        ..##......##....
        ..........##....
        ......####......
        ....##..........
        ..##............
        ..##########....
        ................
        """.trimIndent()
}

private fun big3(): String {
    return """
        ....######......
        ..##......##....
        ..........##....
        ......####......
        ..........##....
        ..##......##....
        ....######......
        ................
        """.trimIndent()
}

private fun big4(): String {
    return """
        ........##......
        ......####......
        ....##..##......
        ..##....##......
        ..##########....
        ........##......
        ......######....
        ................
        """.trimIndent()
}

private fun big5(): String {
    return """
        ..##########....
        ..##............
        ..##............
        ..########......
        ..........##....
        ..##......##....
        ....######......
        ................
        """.trimIndent()
}

private fun big6(): String {
    return """
        ....######......
        ..##......##....
        ..##............
        ..########......
        ..##......##....
        ..##......##....
        ....######......
        ................
        """.trimIndent()
}

private fun big7(): String {
    return """
        ..##########....
        ..##......##....
        ..........##....
        ........##......
        ......##........
        ......##........
        ......##........
        ................
        """.trimIndent()
}

private fun big8(): String {
    return """
        ....######......
        ..##......##....
        ..##......##....
        ....######......
        ..##......##....
        ..##......##....
        ....######......
        ................
        """.trimIndent()
}

private fun big9(): String {
    return """
        ....######......
        ..##......##....
        ..##......##....
        ....########....
        ..........##....
        ..##......##....
        ....######......
        ................
        """.trimIndent()
}

private fun hyphen(): String {
    return """
        ................
        ................
        ................
        ................
        ..##########....
        ................
        ................
        ................
        """.trimIndent()
}

class BigChar(char: Char) {

    private val font: String = when (char) {
        '0' -> big0()
        '1' -> big1()
        '2' -> big2()
        '3' -> big3()
        '4' -> big4()
        '5' -> big5()
        '6' -> big6()
        '7' -> big7()
        '8' -> big8()
        '9' -> big9()
        '-' -> hyphen()
        else -> throw IllegalStateException()
    }

    fun print() {
        print(font)
        println()
    }
}

object BigCharFactory {

    private val pool = mutableMapOf<Char, BigChar>()

    @Synchronized
    fun getBigChar(ch: Char): BigChar = pool.getOrPut(ch) { BigChar(ch) }
}

class BigString(string: String) {

    private val bigChars = string.map { BigCharFactory.getBigChar(it) }.toTypedArray()

    fun print() {
        bigChars.forEach { it.print() }
    }
}
