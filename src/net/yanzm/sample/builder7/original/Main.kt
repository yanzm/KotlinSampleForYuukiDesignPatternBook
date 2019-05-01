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

package net.yanzm.sample.builder7.original

import java.io.FileWriter
import java.io.PrintWriter

fun main(vararg args: String) {
    fun usage() {
        println("Usage: java Main plain  プレーンテキストで文書作成")
        println("Usage: java Main html  HTLMファイルで文書作成")
    }

    if (args.size != 1) {
        usage()
        return
    }

    when (args[0]) {
        "plain" -> {
            val textBuilder = TextBuilder()
            val director = Director(textBuilder)
            director.construct()
            val result = textBuilder.getResult()
            println(result)
        }
        "html" -> {
            val htmlBuilder = HTMLBuilder()
            val director = Director(htmlBuilder)
            director.construct()
            val filename = htmlBuilder.getResult()
            println("$filename が作成されました。")
        }
        else -> usage()
    }
}


abstract class Builder {
    abstract fun makeTitle(title: String)
    abstract fun makeString(str: String)
    abstract fun makeItems(items: Array<String>)
    abstract fun close()
}

class Director(private val builder: Builder) {

    fun construct() {
        builder.makeTitle("Greeting")
        builder.makeString("朝から昼にかけて")
        builder.makeItems(
            arrayOf(
                "おはようございます。",
                "こんにちは。"
            )
        )
        builder.makeString("夜に")
        builder.makeItems(
            arrayOf(
                "こんばんは。",
                "おやすみなさい。",
                "さようなら。"
            )
        )
        builder.close()
    }
}

class TextBuilder : Builder() {

    private val sb = StringBuilder()

    override fun makeTitle(title: String) {
        sb.append("==============================\n")
        sb.append("『${title}』\n")
        sb.append("\n")
    }

    override fun makeString(str: String) {
        sb.append("■ $str\n")
        sb.append("\n")
    }

    override fun makeItems(items: Array<String>) {
        for (item in items) {
            sb.append("  ・$item\n")
        }
        sb.append("\n")
    }

    override fun close() {
        sb.append("==============================\n")
    }

    fun getResult() = sb.toString()
}

class HTMLBuilder : Builder() {

    private lateinit var filename: String
    private lateinit var writer: PrintWriter

    override fun makeTitle(title: String) {
        filename = "$title.html"
        writer = PrintWriter(FileWriter(filename))
        writer.println("<html><head><title>$title</title></head><body>")
        writer.println("<h1>$title</h1>")
    }

    override fun makeString(str: String) {
        writer.println("<p>$str</p>")
    }

    override fun makeItems(items: Array<String>) {
        writer.println("<ul>")
        for (item in items) {
            writer.println("<li>$item</li>")
        }
        writer.println("</ul>")
    }

    override fun close() {
        writer.println("</body></html>")
        writer.close()
    }

    fun getResult() = filename
}
