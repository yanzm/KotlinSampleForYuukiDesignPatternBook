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

package net.yanzm.sample.composite11.improved

fun main() {
    println("Making root entries...")
    val rootdir = Directory("root")
    val bindir = Directory("bin").apply {
        add(File("vi", 10000))
        add(File("latex", 20000))
    }
    val tmpdir = Directory("tmp")
    val usrdir = Directory("usr")
    rootdir.add(bindir)
    rootdir.add(tmpdir)
    rootdir.add(usrdir)
    rootdir.printList()

    println("")
    println("Making user entries...")
    usrdir.add(
        Directory("yuki").apply {
            add(File("diary.html", 100))
            add(File("Composite.java", 200))
        }
    )
    usrdir.add(
        Directory("hanako").apply {
            add(File("memo.tex", 300))
        }
    )
    usrdir.add(
        Directory("tomura").apply {
            add(File("game.doc", 400))
            add(File("junk.mail", 500))
        }
    )
    rootdir.printList()
}

abstract class Entry {
    abstract val name: String
    abstract val size: Int

    @Throws(FileTreatmentException::class)
    open fun add(entry: Entry): Entry {
        throw FileTreatmentException()
    }

    fun printList() {
        printList("")
    }

    abstract fun printList(prefix: String)

    override fun toString(): String = "$name ($size)"
}

class File(override val name: String, override val size: Int) : Entry() {

    override fun printList(prefix: String) {
        println("$prefix/$this")
    }
}

class Directory(override val name: String) : Entry() {

    private val directory = mutableListOf<Entry>()

    override val size: Int
        get() = directory.sumBy(Entry::size)

    override fun add(entry: Entry): Entry {
        directory.add(entry)
        return this
    }

    override fun printList(prefix: String) {
        println("$prefix/$this")
        directory.forEach { it.printList("$prefix/$name") }
    }
}

class FileTreatmentException : Exception()
