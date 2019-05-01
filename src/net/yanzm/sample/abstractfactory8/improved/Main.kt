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

package net.yanzm.sample.abstractfactory8.improved

import net.yanzm.sample.abstractfactory8.improved.factory.Factory

fun main(vararg args: String) {
    if (args.size != 1) {
        println("Usage: java Main class.printerName.of.ConcreteFactory")
        println("Example 1: listfactory.ListFactory")
        println("Example 2: tablefactory.TableFactory")
        return
    }

    val factory = checkNotNull(Factory.getFactory(args[0]))

    val asahi = factory.createLink("朝日新聞", "https://www.asahi.com/")
    val yomiuri = factory.createLink("読売新聞", "https://www.yomiuri.co.jp/")

    val us_yahoo = factory.createLink("Yahoo!", "https://www.yahoo.com/")
    val jp_yahoo = factory.createLink("Yahoo!Japan", "https://www.yahoo.co.jp/")
    val excite = factory.createLink("Excite", "https://www.excite.com/")
    val google = factory.createLink("Google", "https://www.google.com/")

    val traynews = factory.createTray("新聞").apply {
        add(asahi)
        add(yomiuri)
    }

    val trayyahoo = factory.createTray("Yahoo!").apply {
        add(us_yahoo)
        add(jp_yahoo)
    }

    val traysearch = factory.createTray("サーチエンジン").apply {
        add(trayyahoo)
        add(excite)
        add(google)
    }

    factory.createPage("LinkPage", "Taro").apply {
        add(traynews)
        add(traysearch)
        output()
    }
}
