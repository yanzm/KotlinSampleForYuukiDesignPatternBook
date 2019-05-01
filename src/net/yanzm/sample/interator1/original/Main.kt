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

package net.yanzm.sample.interator1.original

fun main() {
    val bookShelf = BookShelf(4).apply {
        appendBook(Book("Around the World in 80 Days"))
        appendBook(Book("Bible"))
        appendBook(Book("Cinderella"))
        appendBook(Book("Daddy-Long-Legs"))
    }

    val iterator = bookShelf.iterator()
    while (iterator.hasNext()) {
        val book = iterator.next()
        println(book.name)
    }

}

interface Aggregate<T> {
    fun iterator(): Iterator<T>
}

interface Iterator<T> {
    fun hasNext(): Boolean
    fun next(): T
}

data class Book(val name: String)

class BookShelf(maxSize: Int) : Aggregate<Book> {

    private val books = arrayOfNulls<Book>(maxSize)
    private var last: Int = 0

    fun getBookAt(index: Int): Book {
        return books[index]!!
    }

    fun appendBook(book: Book) {
        books[last] = book
        last++
    }

    fun getLength() = books.size

    override fun iterator(): Iterator<Book> {
        return BookShelfIterator(this)
    }
}

class BookShelfIterator(private val bookShelf: BookShelf) : Iterator<Book> {

    private var index = 0

    override fun hasNext(): Boolean {
        return index < bookShelf.getLength()
    }

    override fun next(): Book {
        val book = bookShelf.getBookAt(index)
        index++
        return book
    }
}
