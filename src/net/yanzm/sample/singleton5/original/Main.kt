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

package net.yanzm.sample.singleton5.original

fun main() {
    println("Start.")
    val obj1 = Singleton.getInstance()
    val obj2 = Singleton.getInstance()
    if (obj1 === obj2) {
        println("obj1 and obj2 are same instance.")
    } else {
        println("obj1 and obj2 are difference instance.")
    }
    println("End.")
}

class Singleton {

    init {
        println("create instance")
    }

    companion object {
        private val singleton = Singleton()
        fun getInstance() = singleton
    }
}
