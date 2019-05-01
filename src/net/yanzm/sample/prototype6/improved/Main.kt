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

package net.yanzm.sample.prototype6.improved

fun main() {
    val manager = Manager().apply {
        register("strong message", UnderlinePen('-'))
        register("warning box", MessageBox('*'))
        register("slash box", MessageBox('/'))
    }

    val p1 = manager.create("strong message")
    p1.use("Hello, world.")
    val p2 = manager.create("warning box")
    p2.use("Hello, world.")
    val p3 = manager.create("slash box")
    p3.use("Hello, world.")
}

interface Product {
    fun use(text: String)
    fun createClone(): Product
}

class Manager {
    private val showcase = mutableMapOf<String, Product>()

    fun register(name: String, proto: Product) {
        showcase[name] = proto
    }

    fun create(name: String): Product = showcase.getValue(name).createClone()
}

data class MessageBox(private val char: Char) : Product {

    override fun use(text: String) {
        val length = text.toByteArray().size
        repeat(length + 4) { print(char) }
        println("")
        println("$char $text $char")
        repeat(length + 4) { print(char) }
        println("")
    }

    override fun createClone(): Product = copy()
}

data class UnderlinePen(private val char: Char) : Product {

    override fun use(text: String) {
        val length = text.toByteArray().size
        println("\"$text\"")
        print(" ")
        repeat(length) { print(char) }
        println("")
    }

    override fun createClone(): Product = copy()
}
