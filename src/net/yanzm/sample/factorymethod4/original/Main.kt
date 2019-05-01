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

package net.yanzm.sample.factorymethod4.original

fun main() {
    val factory = IDCardFactory()
    val card1 = factory.create("A")
    val card2 = factory.create("B")
    val card3 = factory.create("C")
    card1.use()
    card2.use()
    card3.use()
}

abstract class Product {
    abstract fun use()
}

abstract class Factory {

    fun create(owner: String): Product {
        val p = createProduct(owner)
        registerProduct(p)
        return p
    }

    protected abstract fun createProduct(owner: String): Product
    protected abstract fun registerProduct(product: Product)
}

class IDCard(val owner: String) : Product() {

    init {
        println("create a card of $owner.")
    }

    override fun use() {
        println("use $owner's card.")
    }
}

class IDCardFactory : Factory() {

    private val owners = mutableListOf<String>()

    override fun createProduct(owner: String): Product {
        return IDCard(owner)
    }

    override fun registerProduct(product: Product) {
        owners.add((product as IDCard).owner)
    }

    fun getOwners() = owners
}
