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

package net.yanzm.sample.prototype6.original

fun main() {
    val manager = Manager()
    val upen = UnderlinePen('-')
    val mbox = MessageBox('*')
    val sbox = MessageBox('/')
    manager.register("strong message", upen)
    manager.register("warning box", mbox)
    manager.register("slash box", sbox)

    val p1 = manager.create("strong message")
    p1.use("Hello, world.")
    val p2 = manager.create("warning box")
    p2.use("Hello, world.")
    val p3 = manager.create("slash box")
    p3.use("Hello, world.")
}

interface Product {
    fun use(s: String)
    fun createClone(): Product
}

class Manager {
    private val showcase = mutableMapOf<String, Product>()

    fun register(name: String, proto: Product) {
        showcase[name] = proto
    }

    fun create(protoname: String): Product {
        val p = showcase.getValue(protoname)
        return p.createClone()
    }
}

data class MessageBox(private val decochar: Char) : Product {

    override fun use(s: String) {
        val length = s.toByteArray().size
        for (i in 0 until (length + 4)) {
            print(decochar)
        }
        println("")
        println("$decochar $s $decochar")
        for (i in 0 until (length + 4)) {
            print(decochar)
        }
        println("")
    }

    override fun createClone(): Product {
        return copy()
    }
}

data class UnderlinePen(private val ulchar: Char) : Product {

    override fun use(s: String) {
        val length = s.toByteArray().size
        println("\"$s\"")
        print(" ")
        for (i in 0 until length) {
            print(ulchar)
        }
        println("")
    }

    override fun createClone(): Product {
        return copy()
    }
}
