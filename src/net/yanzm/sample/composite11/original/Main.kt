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

package net.yanzm.sample.composite11.original

fun main() {
    println("Making root entries...")
    val rootdir = Directory("root")
    val bindir = Directory("bin")
    val tmpdir = Directory("tmp")
    val usrdir = Directory("usr")
    rootdir.add(bindir)
    rootdir.add(tmpdir)
    rootdir.add(usrdir)
    bindir.add(File("vi", 10000))
    bindir.add(File("latex", 20000))
    rootdir.printList()

    println("")
    println("Making user entries...")
    val yuki = Directory("yuki")
    val hanako = Directory("hanako")
    val tomura = Directory("tomura")
    usrdir.add(yuki)
    usrdir.add(hanako)
    usrdir.add(tomura)
    yuki.add(File("diary.html", 100))
    yuki.add(File("Composite.java", 200))
    hanako.add(File("memo.tex", 300))
    tomura.add(File("game.doc", 400))
    tomura.add(File("junk.mail", 500))
    rootdir.printList()
}

abstract class Entry {
    abstract fun getName(): String
    abstract fun getSize(): Int

    @Throws(FileTreatmentException::class)
    open fun add(entry: Entry): Entry {
        throw FileTreatmentException()
    }

    fun printList() {
        printList("")
    }

    abstract fun printList(prefix: String)

    override fun toString(): String {
        return "${getName()} (${getSize()})"
    }
}

class File(private val name: String, private val size: Int) : Entry() {

    override fun getName(): String {
        return name
    }

    override fun getSize(): Int {
        return size
    }

    override fun printList(prefix: String) {
        println("$prefix/$this")
    }
}

class Directory(private val name: String) : Entry() {

    private val directory = mutableListOf<Entry>()

    override fun getName(): String {
        return name
    }

    override fun getSize(): Int {
        var size = 0
        for (entry in directory) {
            size += entry.getSize()
        }
        return size
    }

    override fun add(entry: Entry): Entry {
        directory.add(entry)
        return this
    }

    override fun printList(prefix: String) {
        println("$prefix/$this")
        for (entry in directory) {
            entry.printList("$prefix/$name")
        }
    }
}

class FileTreatmentException : Exception()
