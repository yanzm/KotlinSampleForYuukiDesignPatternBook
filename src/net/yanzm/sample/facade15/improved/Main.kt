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

package net.yanzm.sample.facade15.improved

import java.io.FileWriter
import java.io.IOException
import java.io.Writer

fun main() {
    PageMaker.makeWelcomePage("a@sample.com", "welcome.html")
}

class Database private constructor() {
    companion object {
        fun getProperties(): Map<String, String> {
            return mapOf(
                "a@sample.com" to "a",
                "b@sample.com" to "b",
                "c@sample.com" to "c",
                "d@sample.com" to "d"
            )
        }
    }
}

class HtmlWriter(private val writer: Writer) {

    @Throws(IOException::class)
    fun title(title: String) {
        writer.apply {
            write("<html>")
            write("<head>")
            write("<title>$title</title>")
            write("</head>")
            write("<body>\n")
            write("<h1>$title</h1>\n")
        }
    }

    @Throws(IOException::class)
    fun paragraph(msg: String) {
        writer.write("<p>$msg</p>")
    }

    @Throws(IOException::class)
    fun link(href: String, caption: String) {
        paragraph("<a href=\"$href\">$caption</a>")
    }

    @Throws(IOException::class)
    fun mailto(mailaddr: String, username: String) {
        link("mailto:$mailaddr", username)
    }

    @Throws(IOException::class)
    fun close() {
        writer.write("</body>")
        writer.write("</html>\n")
        writer.close()
    }
}

class PageMaker private constructor() {

    companion object {
        fun makeWelcomePage(mailAddress: String, filename: String) {
            val mailProperties = Database.getProperties()
            val username = mailProperties.getValue(mailAddress)
            HtmlWriter(FileWriter(filename)).apply {
                title("Welcome to $username's page!")
                paragraph("$username のページへようこそ。")
                paragraph("メールまっていますね。")
                mailto(mailAddress, username)
                close()
            }
            println("$filename is created for $mailAddress ($username)")
        }
    }
}
