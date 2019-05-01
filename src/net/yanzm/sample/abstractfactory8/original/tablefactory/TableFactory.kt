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

package net.yanzm.sample.abstractfactory8.original.tablefactory

import net.yanzm.sample.abstractfactory8.original.factory.Factory
import net.yanzm.sample.abstractfactory8.original.factory.Link
import net.yanzm.sample.abstractfactory8.original.factory.Page
import net.yanzm.sample.abstractfactory8.original.factory.Tray

class TableFactory : Factory() {

    override fun createLink(caption: String, url: String): Link {
        return TableLink(caption, url)
    }

    override fun createTray(caption: String): Tray {
        return TableTray(caption)
    }

    override fun createPage(title: String, author: String): Page {
        return TablePage(title, author)
    }
}

class TableLink(caption: String, url: String) : Link(caption, url) {

    override fun makeHTML(): String {
        return "<td><a href=\"$url\">$caption</a></td>"
    }
}

class TableTray(caption: String) : Tray(caption) {
    override fun makeHTML(): String {
        val sb = StringBuilder()
        sb.append("<td>")
        sb.append("<table width=\"100%\" border=\"1\"><tr>")
        sb.append("<td bgcolor=\"#cccccc\" align=\"center\" colspan=\"${tray.size}\"><b>$caption</b></td>")
        sb.append("</tr>\n")
        sb.append("<tr>\n")
        for (item in tray) {
            sb.append(item.makeHTML())
        }
        sb.append("</tr></table>\n")
        sb.append("</td>\n")
        return sb.toString()
    }
}

class TablePage(title: String, author: String) : Page(title, author) {
    override fun makeHTML(): String {
        val sb = StringBuilder()
        sb.append("<html><head><title>$title</title></head>\n")
        sb.append("<body>\n")
        sb.append("<h1>$title</h1>\n")
        sb.append("<table width=\"80%\" border=\"3\">\n")
        for (item in content) {
            sb.append("<tr>${item.makeHTML()}</tr>")
        }
        sb.append("</table>\n")
        sb.append("<hr><address>$author</address>\n")
        sb.append("</body></html>\n")
        return sb.toString()
    }
}
