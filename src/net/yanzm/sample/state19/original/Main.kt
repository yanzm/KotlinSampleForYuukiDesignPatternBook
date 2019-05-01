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

package net.yanzm.sample.state19.original

import java.awt.*
import java.awt.event.ActionEvent
import java.awt.event.ActionListener

fun main() {
    val frame = SafeFrame("State Sample")
    while (true) {
        for (hour in 0 until 24) {
            frame.setClock(hour)
            Thread.sleep(1000)
        }
    }
}

interface State {
    fun doClock(context: Context, hour: Int)
    fun doUse(context: Context)
    fun doAlarm(context: Context)
    fun doPhone(context: Context)
}

class DayState private constructor() : State {

    override fun doClock(context: Context, hour: Int) {
        if (hour < 9 || 17 <= hour) {
            context.changeState(NightState.getInstance())
        }
    }

    override fun doUse(context: Context) {
        context.recordLog("金庫使用(昼間)")
    }

    override fun doAlarm(context: Context) {
        context.callSecurityCenter("非常ベル(昼間)")
    }

    override fun doPhone(context: Context) {
        context.callSecurityCenter("通常の通話(昼間)")
    }

    override fun toString(): String {
        return "[昼間]"
    }

    companion object {
        private val singleton = DayState()

        fun getInstance(): DayState {
            return singleton
        }
    }
}

class NightState private constructor() : State {

    override fun doClock(context: Context, hour: Int) {
        if (9 <= hour && hour < 17) {
            context.changeState(DayState.getInstance())
        }
    }

    override fun doUse(context: Context) {
        context.recordLog("非常:夜間の金庫使用!")
    }

    override fun doAlarm(context: Context) {
        context.callSecurityCenter("非常ベル(夜間)")
    }

    override fun doPhone(context: Context) {
        context.callSecurityCenter("夜間の通話録音")
    }

    override fun toString(): String {
        return "[夜間]"
    }

    companion object {
        private val singleton = NightState()

        fun getInstance(): NightState {
            return singleton
        }
    }
}

interface Context {
    fun setClock(hour: Int)
    fun changeState(state: State)
    fun callSecurityCenter(msg: String)
    fun recordLog(msg: String)
}

class SafeFrame(title: String) : Frame(title), ActionListener, Context {

    private val textClock = TextField(60)
    private val textScreen = TextArea(10, 60)
    private val buttonUse = Button("金庫使用")
    private val buttonAlarm = Button("非常ベル")
    private val buttonPhone = Button("通常通話")
    private val buttonExit = Button("終了")

    private var state: State = DayState.getInstance()

    init {
        background = Color.lightGray
        layout = BorderLayout()

        add(textClock, BorderLayout.NORTH)
        textClock.isEditable = false

        add(textScreen, BorderLayout.CENTER)
        textScreen.isEditable = false

        val panel = Panel()
        panel.add(buttonUse)
        panel.add(buttonAlarm)
        panel.add(buttonPhone)
        panel.add(buttonExit)
        add(panel, BorderLayout.SOUTH)

        pack()
        show()

        buttonUse.addActionListener(this)
        buttonAlarm.addActionListener(this)
        buttonPhone.addActionListener(this)
        buttonExit.addActionListener(this)
    }

    override fun actionPerformed(e: ActionEvent) {
        println(e)
        if (e.source == buttonUse) {
            state.doUse(this)
        } else if (e.source == buttonAlarm) {
            state.doAlarm(this)
        } else if (e.source == buttonPhone) {
            state.doPhone(this)
        } else if (e.source == buttonExit) {
            System.exit(0)
        } else {
            println("?")
        }
    }

    override fun setClock(hour: Int) {
        var clockString = "現在時刻は"
        if (hour < 10) {
            clockString += "0$hour:00"
        } else {
            clockString += "$hour:00"
        }
        println(clockString)
        textClock.text = clockString
        state.doClock(this, hour)
    }

    override fun changeState(state: State) {
        println("${this.state}から${state}へ状態が変化しました。")
        this.state = state
    }

    override fun callSecurityCenter(msg: String) {
        textScreen.append("call! $msg\n")
    }

    override fun recordLog(msg: String) {
        textScreen.append("record ... $msg\n")
    }
}
