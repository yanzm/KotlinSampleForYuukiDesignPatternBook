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

package net.yanzm.sample.chainofresponsibility14.original

fun main() {
    val alice = NoSupport("Alice")
    val bob = LimitSupport("Bob", 100)
    val charlie = SpecialSupport("Charlie", 429)
    val diana = LimitSupport("Diana", 200)
    val elmo = OddSupport("Elmo")
    val fred = LimitSupport("Fred", 300)
    alice.setNext(bob).setNext(charlie).setNext(diana).setNext(elmo).setNext(fred)
    for (i in 0 until 500 step 33) {
        alice.support(Trouble(i))
    }
}

class Trouble(private val number: Int) {
    fun getNumber(): Int {
        return number
    }

    override fun toString(): String {
        return "[Trouble $number]"
    }
}

abstract class Support(private val name: String) {
    private var next: Support? = null

    fun setNext(next: Support): Support {
        this.next = next
        return next
    }

    fun support(trouble: Trouble) {
        if (resolve(trouble)) {
            done(trouble)
        } else if (next != null) {
            next!!.support(trouble)
        } else {
            fail(trouble)
        }
    }

    override fun toString(): String {
        return "[$name]"
    }

    protected abstract fun resolve(trouble: Trouble): Boolean

    protected fun done(trouble: Trouble) {
        println("$trouble is resolved by $this.")
    }

    protected fun fail(trouble: Trouble) {
        println("$trouble cannot be resolved.")
    }
}

class NoSupport(name: String) : Support(name) {
    override fun resolve(trouble: Trouble): Boolean {
        return false
    }
}

class LimitSupport(name: String, private val limit: Int) : Support(name) {
    override fun resolve(trouble: Trouble): Boolean {
        return trouble.getNumber() < limit
    }
}

class OddSupport(name: String) : Support(name) {
    override fun resolve(trouble: Trouble): Boolean {
        return trouble.getNumber() % 2 == 1
    }
}

class SpecialSupport(name: String, private val number: Int) : Support(name) {
    override fun resolve(trouble: Trouble): Boolean {
        return trouble.getNumber() == number
    }
}
