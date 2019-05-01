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

package net.yanzm.sample.command22.original

import java.awt.Canvas
import java.awt.Color
import java.awt.Graphics
import java.awt.Point
import java.awt.event.*
import java.util.*
import javax.swing.Box
import javax.swing.BoxLayout
import javax.swing.JButton
import javax.swing.JFrame

fun main() {
    Main("Command Pattern Sample")
}

interface Command {
    fun execute()
}

class MacroCommand : Command {

    private val commands = Stack<Command>()

    override fun execute() {
        for (command in commands) {
            command.execute()
        }
    }

    fun append(cmd: Command) {
        if (cmd != this) {
            commands.push(cmd)
        }
    }

    fun undo() {
        if (!commands.empty()) {
            commands.pop()
        }
    }

    fun clear() {
        commands.clear()
    }
}

class DrawCommand(private val drawable: Drawable, private val position: Point) : Command {

    override fun execute() {
        drawable.draw(position.x, position.y)
    }
}

interface Drawable {
    fun draw(x: Int, y: Int)
}

class DrawCanvas(width: Int, height: Int, private val history: MacroCommand) : Canvas(), Drawable {

    private val color = Color.red
    private val radius = 6

    init {
        setSize(width, height)
        background = Color.white
    }

    override fun paint(g: Graphics) {
        history.execute()
    }

    override fun draw(x: Int, y: Int) {
        val g = graphics
        g.color = color
        g.fillOval(x - radius, y - radius, radius * 2, radius * 2)
    }
}

private class Main(title: String) : JFrame(title), ActionListener, MouseMotionListener, WindowListener {

    private val history = MacroCommand()
    private val canvas = DrawCanvas(400, 400, history)
    private val clearButton = JButton("clear")

    init {
        addWindowListener(this)
        canvas.addMouseMotionListener(this)
        clearButton.addActionListener(this)

        val buttonBox = Box(BoxLayout.X_AXIS)
        buttonBox.add(clearButton)
        val mainBox = Box(BoxLayout.Y_AXIS)
        mainBox.add(buttonBox)
        mainBox.add(canvas)
        contentPane.add(mainBox)

        pack()
        isVisible = true
    }

    override fun actionPerformed(e: ActionEvent) {
        if (e.source == clearButton) {
            history.clear()
            canvas.repaint()
        }
    }

    override fun mouseMoved(e: MouseEvent) {
    }

    override fun mouseDragged(e: MouseEvent) {
        val cmd = DrawCommand(canvas, e.point)
        history.append(cmd)
        cmd.execute()
    }

    override fun windowClosing(e: WindowEvent) {
        System.exit(0)
    }

    override fun windowDeiconified(e: WindowEvent) {}
    override fun windowClosed(e: WindowEvent) {}
    override fun windowActivated(e: WindowEvent) {}
    override fun windowDeactivated(e: WindowEvent) {}
    override fun windowOpened(e: WindowEvent) {}
    override fun windowIconified(e: WindowEvent) {}
}
