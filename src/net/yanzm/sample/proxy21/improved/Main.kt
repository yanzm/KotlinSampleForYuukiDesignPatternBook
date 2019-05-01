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

package net.yanzm.sample.proxy21.improved

fun main() {
    val p: Printable = PrinterProxy("Alice")
    println("名前は現在${p.printerName}です。")
    p.printerName = "Bob"
    println("名前は現在${p.printerName}です。")
    p.printText("Hello, world.")
}

interface Printable {
    var printerName: String
    fun printText(string: String)
}

class Printer(override var printerName: String) : Printable {

    init {
        heavyJob("Printerのインスタンス($printerName)を生成中")
    }

    override fun printText(string: String) {
        println("=== $printerName ===")
        println(string)
    }

    private fun heavyJob(msg: String) {
        print(msg)
        repeat(5) {
            Thread.sleep(1000)
            print(".")
        }
        println("完了。")
    }
}

class PrinterProxy(name: String) : Printable {

    private var real: Printer? = null

    override var printerName: String = name
        set(value) {
            real?.printerName = value
            field = value
        }

    override fun printText(string: String) {
        realize()
        real?.printText(string)
    }

    @Synchronized
    private fun realize() {
        if (real == null) {
            real = Printer(printerName)
        }
    }
}
