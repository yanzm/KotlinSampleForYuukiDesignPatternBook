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

package net.yanzm.sample.memento18.improved

import kotlin.random.Random

fun main() {
    val gamer = Gamer(100)
    var memento = gamer.createMemento()

    repeat(100) { i ->
        println("==== $i")
        println("現状:$gamer")

        gamer.bet()

        val money = gamer.money

        println("所持金は${money}円になりました。")

        if (money > memento.money) {
            println("    (だいぶ増えたので、現在の状態を保存しておこう)")
            memento = gamer.createMemento()
        } else if (money < memento.money / 2) {
            println("    (だいぶ減ったので、以前の状態に復帰しよう)")
            gamer.restoreMemento(memento)
        }

        Thread.sleep(100)
        println()
    }
}

class Memento(val money: Int, val fruits: List<String>)

class Gamer(money: Int) {

    var money: Int = money
        private set

    private var fruits = mutableListOf<String>()

    fun bet() {
        when (Random.nextInt(6) + 1) {
            1 -> {
                money += 100
                println("所持金が増えました。")
            }
            2 -> {
                money /= 2
                println("所持金が半分になりました。")
            }
            6 -> {
                val fruit = getFruit()
                println("フルーツ($fruit)をもらいました。")
                fruits.add(fruit)
            }
            else -> println("何も起こりませんでした。")
        }
    }

    fun createMemento(): Memento = Memento(money, fruits.filter { it.startsWith("おいしい") })

    fun restoreMemento(memento: Memento) {
        this.money = memento.money
        this.fruits = memento.fruits.toMutableList()
    }

    override fun toString(): String {
        return "[money = $money, fruits = $fruits]"
    }

    private fun getFruit(): String {
        val prefix = if (Random.nextBoolean()) "おいしい" else ""
        return prefix + fruitsName.random()
    }

    companion object {
        private val fruitsName = arrayOf("リンゴ", "ぶどう", "バナナ", "みかん")
    }
}
