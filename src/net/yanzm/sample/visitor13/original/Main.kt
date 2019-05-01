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

package net.yanzm.sample.visitor13.original

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
    abstract fun getName(): String
    abstract fun getSize(): Int

    @Throws(FileTreatmentException::class)
    open fun add(entry: Entry): Entry {
        throw FileTreatmentException()
    }

    @Throws(FileTreatmentException::class)
    open fun iterator(): Iterator<Entry> {
        throw FileTreatmentException()
    }

    override fun toString(): String {
        return getName() + " (" + getSize() + ")"
    }
}

class File(private val name: String, private val size: Int) : Entry() {

    override fun getName(): String {
        return name
    }

    override fun getSize(): Int {
        return size
    }

    override fun accept(v: Visitor) {
        v.visit(this)
    }
}

class Directory(private val name: String) : Entry() {

    private val dir = mutableListOf<Entry>()

    override fun getName(): String {
        return name
    }

    override fun getSize(): Int {
        var size = 0
        for (entry in dir) {
            size += entry.getSize()
        }
        return size
    }

    override fun add(entry: Entry): Entry {
        dir.add(entry)
        return this
    }

    override fun iterator(): Iterator<Entry> {
        return dir.iterator()
    }

    override fun accept(v: Visitor) {
        v.visit(this)
    }
}

class ListVisitor : Visitor() {

    private var currentdir = ""

    override fun visit(file: File) {
        println("$currentdir/$file")
    }

    override fun visit(directory: Directory) {
        println("$currentdir/$directory")
        val savedir = currentdir
        currentdir = currentdir + "/" + directory.getName()
        for (entry in directory.iterator()) {
            entry.accept(this)
        }
        currentdir = savedir
    }
}

class FileTreatmentException : RuntimeException()
