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

package net.yanzm.sample.strategy10.original

import java.util.*

fun main(vararg args: String) {
    if (args.size != 2) {
        println("Usage: java Main randomseed1 randomseed2")
        println("Example: java Main 314 15")
        return
    }

    val seed1 = args[0].toInt()
    val seed2 = args[1].toInt()
    val player1 = Player("Taro", WinningStrategy(seed1))
    val player2 = Player("Hana", ProbStrategy(seed2))
    for (i in 0 until 10000) {
        val nextHand1 = player1.nextHand()
        val nextHand2 = player2.nextHand()
        if (nextHand1.isStrongerThan(nextHand2)) {
            println("Winner: $player1")
            player1.win()
            player2.lose()
        } else if (nextHand2.isStrongerThan(nextHand1)) {
            println("Winner: $player2")
            player1.lose()
            player2.win()
        } else {
            println("Even...")
            player1.even()
            player2.even()
        }
    }
    println("Total result:")
    println(player1.toString())
    println(player2.toString())
}

class Hand(private val handvalue: Int) {

    fun isStrongerThan(h: Hand): Boolean {
        return fight(h) == 1
    }

    fun isWeakerThant(h: Hand): Boolean {
        return fight(h) == -1
    }

    private fun fight(h: Hand): Int {
        return when {
            this == h -> 0
            (this.handvalue + 1) % 3 == h.handvalue -> 1
            else -> -1
        }
    }

    override fun toString(): String {
        return name[handvalue]
    }

    companion object {
        const val HANDVALUE_GUU = 0
        const val HANDVALUE_CHO = 1
        const val HANDVALUE_PAA = 2

        val hand = arrayOf(
            Hand(HANDVALUE_GUU),
            Hand(HANDVALUE_CHO),
            Hand(HANDVALUE_PAA)
        )

        val name = arrayOf(
            "グー", "チョキ", "パー"
        )

        fun getHand(handvalue: Int): Hand {
            return hand[handvalue]
        }
    }
}

interface Strategy {
    fun nextHand(): Hand
    fun study(win: Boolean)
}

class WinningStrategy(seed: Int) : Strategy {
    private val random = Random(seed.toLong())
    private var won = false
    private lateinit var prevHand: Hand

    override fun nextHand(): Hand {
        if (!won) {
            prevHand = Hand.getHand(random.nextInt(3))
        }
        return prevHand
    }

    override fun study(win: Boolean) {
        won = win
    }
}

class ProbStrategy(seed: Int) : Strategy {
    private val random = Random(seed.toLong())
    private var prevHandValue = 0
    private var currentHandValue = 0
    private val history = arrayOf(
        intArrayOf(1, 1, 1),
        intArrayOf(1, 1, 1),
        intArrayOf(1, 1, 1)
    )

    override fun nextHand(): Hand {
        val bet = random.nextInt(getSum(currentHandValue))
        val handValue = when {
            bet < history[currentHandValue][0] -> 0
            bet < history[currentHandValue][0] + history[currentHandValue][1] -> 1
            else -> 2
        }
        prevHandValue = currentHandValue
        currentHandValue = handValue
        return Hand.getHand(handValue)
    }

    private fun getSum(hv: Int): Int {
        var sum = 0
        for (i in 0 until 3) {
            sum += history[hv][i]
        }
        return sum
    }

    override fun study(win: Boolean) {
        if (win) {
            history[prevHandValue][currentHandValue]++
        } else {
            history[prevHandValue][(currentHandValue + 1) % 3]++
            history[prevHandValue][(currentHandValue + 2) % 3]++
        }
    }
}

class Player(private val name: String, private val strategy: Strategy) {

    private var wincount = 0
    private var losecount = 0
    private var gamecount = 0

    fun nextHand(): Hand {
        return strategy.nextHand()
    }

    fun win() {
        strategy.study(true)
        wincount++
        gamecount++
    }

    fun lose() {
        strategy.study(false)
        losecount++
        gamecount++
    }

    fun even() {
        gamecount++
    }

    override fun toString(): String {
        return "[$name:$gamecount games, $wincount win, $losecount lose]"
    }
}
