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

package net.yanzm.sample.abstractfactory8.improved.tablefactory

import net.yanzm.sample.abstractfactory8.improved.factory.Factory
import net.yanzm.sample.abstractfactory8.improved.factory.Link
import net.yanzm.sample.abstractfactory8.improved.factory.Page
import net.yanzm.sample.abstractfactory8.improved.factory.Tray

class TableFactory : Factory() {

    override fun createLink(caption: String, url: String): Link = TableLink(caption, url)

    override fun createTray(caption: String): Tray = TableTray(caption)

    override fun createPage(title: String, author: String): Page = TablePage(title, author)
}

class TableLink(caption: String, url: String) : Link(caption, url) {

    override fun makeHTML(): String = "<td><a href=\"$url\">$caption</a></td>"
}

class TableTray(caption: String) : Tray(caption) {
    override fun makeHTML(): String {
        return StringBuilder()
            .append("<td>")
            .append("<table width=\"100%\" border=\"1\"><tr>")
            .append("<td bgcolor=\"#cccccc\" align=\"center\" colspan=\"${tray.size}\"><b>$caption</b></td>")
            .append("</tr>\n")
            .append("<tr>\n")
            .apply {
                tray.forEach { append(it.makeHTML()) }
            }
            .append("</tr></table>\n")
            .append("</td>\n")
            .toString()
    }
}

class TablePage(title: String, author: String) : Page(title, author) {
    override fun makeHTML(): String {
        return StringBuilder()
            .append("<html><head><title>$title</title></head>\n")
            .append("<body>\n")
            .append("<h1>$title</h1>\n")
            .append("<table width=\"80%\" border=\"3\">\n")
            .apply {
                content.forEach { append("<tr>${it.makeHTML()}</tr>") }
            }
            .append("</table>\n")
            .append("<hr><address>$author</address>\n")
            .append("</body></html>\n")
            .toString()
    }
}
