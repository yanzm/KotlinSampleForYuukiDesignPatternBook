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

package net.yanzm.sample.strategy10.improved

import kotlin.random.Random

fun main(vararg args: String) {
    if (args.size != 2) {
        println("Usage: java Main seed1 seed2")
        println("Example: java Main 314 15")
        return
    }

    val seed1 = args[0].toInt()
    val seed2 = args[1].toInt()
    val player1 = Player("Taro", WinningStrategy(seed1))
    val player2 = Player("Hana", ProbStrategy(seed2))
    repeat(10000) {
        val nextHand1 = player1.nextHand()
        val nextHand2 = player2.nextHand()
        when {
            nextHand1.isStrongerThan(nextHand2) -> {
                println("Winner: $player1")
                player1.win()
                player2.lose()
            }
            nextHand2.isStrongerThan(nextHand1) -> {
                println("Winner: $player2")
                player1.lose()
                player2.win()
            }
            else -> {
                println("Even...")
                player1.even()
                player2.even()
            }
        }
    }
    println("Total result:")
    println(player1.toString())
    println(player2.toString())
}

enum class Hand(private val value: Int, private val handName: String) {
    GUU(0, "グー"),
    CHO(1, "チョキ"),
    PAA(2, "パー");

    fun isStrongerThan(h: Hand): Boolean = fight(h) == Result.WIN

    private fun fight(h: Hand): Result = when {
        this == h -> Result.EVEN
        (this.value + 1) % 3 == h.value -> Result.WIN
        else -> Result.LOSE
    }

    override fun toString(): String = handName

    companion object {
        fun of(handValue: Int): Hand = values()[handValue]
    }
}

enum class Result {
    WIN,
    LOSE,
    EVEN
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
            prevHand = Hand.of(random.nextInt(3))
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
        return Hand.of(handValue)
    }

    private fun getSum(hv: Int): Int = history[hv].sum()

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

    private var winCount = 0
    private var loseCount = 0
    private var gameCount = 0

    fun nextHand(): Hand = strategy.nextHand()

    fun win() {
        strategy.study(true)
        winCount++
        gameCount++
    }

    fun lose() {
        strategy.study(false)
        loseCount++
        gameCount++
    }

    fun even() {
        gameCount++
    }

    override fun toString(): String = "[$name:$gameCount games, $winCount win, $loseCount lose]"
}
