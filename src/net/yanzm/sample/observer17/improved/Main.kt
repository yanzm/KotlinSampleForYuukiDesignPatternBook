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

package net.yanzm.sample.observer17.improved

import kotlin.random.Random

fun main() {
    val generator = RandomNumberGenerator()
    val observer1 = DigitObserver()
    val observer2 = GraphObserver()
    generator.addObserver(observer1)
    generator.addObserver(observer2)
    generator.execute()
}

interface Observer {
    fun update(generator: NumberGenerator)
}

abstract class NumberGenerator {
    private val observers = mutableListOf<Observer>()

    fun addObserver(observer: Observer) {
        observers.add(observer)
    }

    fun deleteObserver(observer: Observer) {
        observers.remove(observer)
    }

    fun notifyObservers() {
        observers.forEach { it.update(this) }
    }

    abstract val number: Int
    abstract fun execute()
}

class RandomNumberGenerator : NumberGenerator() {

    override var number: Int = 0
        private set

    override fun execute() {
        repeat(20) {
            number = Random.nextInt(50)
            notifyObservers()
        }
    }
}

class DigitObserver : Observer {

    override fun update(generator: NumberGenerator) {
        println("DigitObserver: ${generator.number}")
        Thread.sleep(100)
    }
}

class GraphObserver : Observer {

    override fun update(generator: NumberGenerator) {
        print("GraphObserver:")
        repeat(generator.number) { print("*") }
        println()
        Thread.sleep(100)
    }
}
