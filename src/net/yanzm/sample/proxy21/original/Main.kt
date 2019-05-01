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

package net.yanzm.sample.proxy21.original

fun main() {
    val p: Printable = PrinterProxy("Alice")
    println("名前は現在${p.getPrinterName()}です。")
    p.setPrinterName("Bob")
    println("名前は現在${p.getPrinterName()}です。")
    p.printText("Hello, world.")
}

interface Printable {
    fun setPrinterName(name: String)
    fun getPrinterName(): String?
    fun printText(string: String)
}

class Printer : Printable {

    private var name: String? = null

    constructor() {
        heavyJob("Printerのインスタンスを生成中")
    }

    constructor(name: String?) {
        this.name = name
        heavyJob("Printerのインスタンス($name)を生成中")
    }

    override fun setPrinterName(name: String) {
        this.name = name
    }

    override fun getPrinterName(): String? {
        return name
    }

    override fun printText(string: String) {
        println("=== $name ===")
        println(string)
    }

    private fun heavyJob(msg: String) {
        print(msg)
        for (i in 0 until 5) {
            Thread.sleep(1000)
            print(".")
        }
        println("完了。")
    }
}

class PrinterProxy : Printable {

    private var name: String? = null
    private var real: Printer? = null

    constructor() {
    }

    constructor(name: String) {
        this.name = name
    }

    override fun setPrinterName(name: String) {
        real?.setPrinterName(name)
        this.name = name
    }

    override fun getPrinterName(): String? {
        return name
    }

    override fun printText(string: String) {
        realize()
        real!!.printText(string)
    }

    @Synchronized
    private fun realize() {
        if (real == null) {
            real = Printer(name)
        }
    }
}
