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

package net.yanzm.sample.memento18.original

import java.util.*

fun main() {
    val gamer = Gamer(100)
    var memento = gamer.createMemento()
    for (i in 0 until 100) {
        println("==== $i")
        println("現状:$gamer")

        gamer.bet()

        println("所持金は${gamer.getMoney()}円になりました。")

        if (gamer.getMoney() > memento.getMoney()) {
            println("    (だいぶ増えたので、現在の状態を保存しておこう)")
            memento = gamer.createMemento()
        } else if (gamer.getMoney() < memento.getMoney() / 2) {
            println("    (だいぶ減ったので、以前の状態に復帰しよう)")
            gamer.restoreMemento(memento)
        }

        Thread.sleep(100)
        println()
    }
}

class Memento(private val money: Int) {

    private val fruits = mutableListOf<String>()

    fun getMoney(): Int {
        return money
    }

    fun addFruit(fruit: String) {
        fruits.add(fruit)
    }

    fun getFruits(): List<String> {
        return fruits.toList()
    }
}

class Gamer(private var money: Int) {
    private var fruits = mutableListOf<String>()
    private val random = Random()
    private val fruitsname = arrayOf("リンゴ", "ぶどう", "バナナ", "みかん")

    fun getMoney(): Int {
        return money
    }

    fun bet() {
        val dice = random.nextInt(6) + 1
        if (dice == 1) {
            money += 100
            println("所持金が増えました。")
        } else if (dice == 2) {
            money /= 2
            println("所持金が半分になりました。")
        } else if (dice == 6) {
            val f = getFruit()
            println("フルーツ($f)をもらいました。")
            fruits.add(f)
        } else {
            println("何も起こりませんでした。")
        }
    }

    fun createMemento(): Memento {
        val m = Memento(money)
        for (f in fruits) {
            if (f.startsWith("おいしい")) {
                m.addFruit(f)
            }
        }
        return m
    }

    fun restoreMemento(memento: Memento) {
        this.money = memento.getMoney()
        this.fruits = memento.getFruits().toMutableList()
    }

    override fun toString(): String {
        return "[money = $money, fruits = $fruits]"
    }

    private fun getFruit(): String {
        var prefix = ""
        if (random.nextBoolean()) {
            prefix = "おいしい"
        }
        return prefix + fruitsname[random.nextInt(fruitsname.size)]
    }
}
