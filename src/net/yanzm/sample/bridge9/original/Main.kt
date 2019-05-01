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

package net.yanzm.sample.bridge9.original

fun main() {
    val d1 = Display(StringDisplayImpl("Hello, Japan."))
    val d2 = CountDisplay(StringDisplayImpl("Hello, World."))
    val d3 = CountDisplay(StringDisplayImpl("Hello, Universe."))
    d1.display()
    d2.display()
    d3.display()
    d3.multiDisplay(5)

}

open class Display(private val impl: DisplayImpl) {

    fun open() {
        impl.rawOpen()
    }

    fun print() {
        impl.rawPrint()
    }

    fun close() {
        impl.rawClose()
    }

    fun display() {
        open()
        print()
        close()
    }
}

class CountDisplay(impl: DisplayImpl) : Display(impl) {

    fun multiDisplay(times: Int) {
        open()
        for (i in 0 until times) {
            print()
        }
        close()
    }
}

abstract class DisplayImpl {
    abstract fun rawOpen()
    abstract fun rawPrint()
    abstract fun rawClose()
}

class StringDisplayImpl(private val string: String) : DisplayImpl() {

    private val width = string.toByteArray().size

    override fun rawOpen() {
        printLine()
    }

    override fun rawPrint() {
        println("|$string|")
    }

    override fun rawClose() {
        printLine()
    }

    private fun printLine() {
        print("+")
        for (i in 0 until width) {
            print("-")
        }
        println("+")
    }
}
