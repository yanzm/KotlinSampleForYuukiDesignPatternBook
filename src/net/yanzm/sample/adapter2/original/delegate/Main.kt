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

package net.yanzm.sample.adapter2.original.delegate

fun main() {
    val p = PrintBanner("Hello")
    p.printWeak()
    p.printStrong()
}

class Banner(private val string: String) {

    fun showWithParen() {
        println("($string)")
    }

    fun showWithAster() {
        println("*$string*")
    }
}

abstract class Print {
    abstract fun printWeak()
    abstract fun printStrong()
}

class PrintBanner(string: String) : Print() {

    private val banner = Banner(string)

    override fun printWeak() {
        banner.showWithParen()
    }

    override fun printStrong() {
        banner.showWithAster()
    }
}
