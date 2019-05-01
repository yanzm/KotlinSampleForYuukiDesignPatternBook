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

package net.yanzm.sample.visitor13.improved

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
    rootdir.accept(ListVisitor())

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
    rootdir.accept(ListVisitor())
}

abstract class Visitor {
    abstract fun visit(file: File)
    abstract fun visit(directory: Directory)
}

interface Element {
    fun accept(v: Visitor)
}

abstract class Entry : Element {
    abstract val name: String
    abstract val size: Int

    @Throws(FileTreatmentException::class)
    open fun add(entry: Entry): Entry {
        throw FileTreatmentException()
    }

    @Throws(FileTreatmentException::class)
    open fun iterator(): Iterator<Entry> {
        throw FileTreatmentException()
    }

    override fun toString(): String = "$name ($size)"
}

class File(override val name: String, override val size: Int) : Entry() {

    override fun accept(v: Visitor) {
        v.visit(this)
    }
}

class Directory(override val name: String) : Entry() {

    private val dir = mutableListOf<Entry>()

    override val size: Int
        get() = dir.sumBy(Entry::size)

    override fun add(entry: Entry): Entry {
        dir.add(entry)
        return this
    }

    override fun iterator(): Iterator<Entry> = dir.iterator()

    override fun accept(v: Visitor) {
        v.visit(this)
    }
}

class ListVisitor : Visitor() {

    private var currentDir = ""

    override fun visit(file: File) {
        println("$currentDir/$file")
    }

    override fun visit(directory: Directory) {
        println("$currentDir/$directory")
        val saveDir = currentDir
        currentDir = "$currentDir/${directory.name}"
        directory.iterator().forEach { it.accept(this) }
        currentDir = saveDir
    }
}

class FileTreatmentException : RuntimeException()
