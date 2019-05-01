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

package net.yanzm.sample.abstractfactory8.original.factory

import java.io.FileWriter
import java.io.IOException

abstract class Item(protected val caption: String) {
    abstract fun makeHTML(): String
}

abstract class Link(caption: String, protected val url: String) : Item(caption)

abstract class Tray(caption: String) : Item(caption) {
    protected val tray = mutableListOf<Item>()

    fun add(item: Item) {
        tray.add(item)
    }
}

abstract class Page(protected val title: String, protected val author: String) {
    protected val content = mutableListOf<Item>()

    fun add(item: Item) {
        content.add(item)
    }

    fun output() {
        try {
            val filename = "$title.html"
            val writer = FileWriter(filename)
            writer.write(makeHTML())
            writer.close()
            println("$filename を作成しました。")
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    abstract fun makeHTML(): String
}

abstract class Factory {
    abstract fun createLink(caption: String, url: String): Link
    abstract fun createTray(caption: String): Tray
    abstract fun createPage(title: String, author: String): Page

    companion object {
        fun getFactory(className: String): Factory? {
            return try {
                Class.forName(className).newInstance() as Factory
            } catch (e: ClassNotFoundException) {
                println("クラス $className が見つかりません。")
                null
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }
    }
}
