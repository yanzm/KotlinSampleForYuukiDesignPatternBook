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

package net.yanzm.sample.observer17.original

import java.util.*

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
        for (o in observers) {
            o.update(this)
        }
    }

    abstract fun getNumber(): Int
    abstract fun execute()
}

class RandomNumberGenerator : NumberGenerator() {

    private val random = Random()
    private var number: Int = 0

    override fun getNumber(): Int {
        return number
    }

    override fun execute() {
        for (i in 0 until 20) {
            number = random.nextInt(50)
            notifyObservers()
        }
    }
}

class DigitObserver : Observer {

    override fun update(generator: NumberGenerator) {
        println("DigitObserver: ${generator.getNumber()}")
        Thread.sleep(100)
    }
}

class GraphObserver : Observer {

    override fun update(generator: NumberGenerator) {
        print("GraphObserver:")
        val count = generator.getNumber()
        for (i in 0 until count) {
            print("*")
        }
        println("")
        Thread.sleep(100)
    }
}
