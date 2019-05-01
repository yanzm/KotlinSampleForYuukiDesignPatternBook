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

package net.yanzm.sample.mediator16.original

import java.awt.*
import java.awt.event.*

fun main() {
    LoginFrame("Mediator Sample")
}

interface Mediator {
    fun createColleagues()
    fun colleagueChanged()
}

interface Colleague {
    fun setMediator(mediator: Mediator)
    fun setColleagueEnabled(enabled: Boolean)
}

class ColleagueButton(caption: String) : Button(caption), Colleague {

    private lateinit var mediator: Mediator

    override fun setMediator(mediator: Mediator) {
        this.mediator = mediator
    }

    override fun setColleagueEnabled(enabled: Boolean) {
        isEnabled = enabled
    }
}

class ColleagueTextField(text: String, columns: Int) : TextField(text, columns), TextListener, Colleague {

    private lateinit var mediator: Mediator

    override fun setMediator(mediator: Mediator) {
        this.mediator = mediator
    }

    override fun setColleagueEnabled(enabled: Boolean) {
        isEnabled = enabled
        background = if (enabled) Color.white else Color.lightGray
    }

    override fun textValueChanged(e: TextEvent) {
        mediator.colleagueChanged()
    }
}

class ColleagueCheckbox(caption: String, group: CheckboxGroup, state: Boolean) : Checkbox(caption, group, state),
    ItemListener, Colleague {

    private lateinit var mediator: Mediator

    override fun setMediator(mediator: Mediator) {
        this.mediator = mediator
    }

    override fun setColleagueEnabled(enabled: Boolean) {
        isEnabled = enabled
    }

    override fun itemStateChanged(e: ItemEvent) {
        mediator.colleagueChanged()
    }
}

class LoginFrame(title: String) : Frame(title), ActionListener, Mediator {

    private lateinit var checkGuest: ColleagueCheckbox
    private lateinit var checkLogin: ColleagueCheckbox
    private lateinit var textUser: ColleagueTextField
    private lateinit var textPass: ColleagueTextField
    private lateinit var buttonOk: ColleagueButton
    private lateinit var buttonCancel: ColleagueButton

    init {
        background = Color.lightGray
        layout = GridLayout(4, 2)
        createColleagues()
        add(checkGuest)
        add(checkLogin)
        add(Label("Username:"))
        add(textUser)
        add(Label("Password:"))
        add(textPass)
        add(buttonOk)
        add(buttonCancel)
        colleagueChanged()
        pack()
        show()
    }

    override fun createColleagues() {
        val g = CheckboxGroup()
        checkGuest = ColleagueCheckbox("Guest", g, true)
        checkLogin = ColleagueCheckbox("Login", g, false)
        textUser = ColleagueTextField("", 10)
        textPass = ColleagueTextField("", 10)
        textPass.echoChar = '*'
        buttonOk = ColleagueButton("OK")
        buttonCancel = ColleagueButton("Cancel")

        checkGuest.setMediator(this)
        checkLogin.setMediator(this)
        textUser.setMediator(this)
        textPass.setMediator(this)
        buttonOk.setMediator(this)
        buttonCancel.setMediator(this)

        checkGuest.addItemListener(checkGuest)
        checkLogin.addItemListener(checkLogin)
        textUser.addTextListener(textUser)
        textPass.addTextListener(textPass)
        buttonOk.addActionListener(this)
        buttonCancel.addActionListener(this)
    }

    override fun colleagueChanged() {
        if (checkGuest.state) {
            textUser.setColleagueEnabled(false)
            textPass.setColleagueEnabled(false)
            buttonOk.setColleagueEnabled(true)
        } else {
            textUser.setColleagueEnabled(true)
            userpassChanged()
        }
    }

    private fun userpassChanged() {
        if (textUser.text.isNotEmpty()) {
            textPass.setColleagueEnabled(true)
            if (textPass.text.isNotEmpty()) {
                buttonOk.setColleagueEnabled(true)
            } else {
                buttonOk.setColleagueEnabled(false)
            }
        } else {
            textPass.setColleagueEnabled(false)
            buttonOk.setColleagueEnabled(false)
        }
    }

    override fun actionPerformed(e: ActionEvent) {
        println(e)
        System.exit(0)
    }
}
