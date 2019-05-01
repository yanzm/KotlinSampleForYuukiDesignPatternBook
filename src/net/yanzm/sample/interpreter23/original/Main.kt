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

package net.yanzm.sample.interpreter23.original

import java.util.*

const val program = """
program end
program go end
program go right go right go right go right end
program repeat 4 go right end end
program repeat 4 repeat 3 go right go left end right end end
"""

fun main() {
    program.split("\n")
        .asSequence()
        .filter { it.isNotEmpty() }
        .forEach {
            println("text = \"$it\"")
            val node = ProgramNode()
                .apply { parse(Context(it)) }
            println("node = $node")
        }
}

class Context(text: String) {

    private val tokenizer = StringTokenizer(text)
    private var currentToken: String? = null

    init {
        nextToken()
    }

    fun nextToken(): String? {
        if (tokenizer.hasMoreTokens()) {
            currentToken = tokenizer.nextToken()
        } else {
            currentToken = null
        }
        return currentToken
    }

    fun currentToken(): String? = currentToken

    @Throws(ParseException::class)
    fun skipToken(token: String) {
        if (token != currentToken) {
            throw ParseException("Warning: $token is expected, but $currentToken is found.")
        }
        nextToken()
    }

    fun currentNumber(): Int {
        return try {
            Integer.parseInt(currentToken)
        } catch (e: NumberFormatException) {
            throw ParseException("Warning: $e")
        }
    }
}

abstract class Node {

    @Throws(ParseException::class)
    abstract fun parse(context: Context)
}

/**
 * <program> ::= program <command list>
 */
class ProgramNode : Node() {

    private lateinit var commandListNode: Node

    override fun parse(context: Context) {
        context.skipToken("program")
        commandListNode = CommandListNode().apply { parse(context) }
    }

    override fun toString(): String = "[program $commandListNode]"
}

/**
 * <command list> ::= <command>* end
 */
class CommandListNode : Node() {

    private val list = mutableListOf<Node>()

    override fun parse(context: Context) {
        while (true) {
            val token = context.currentToken() ?: throw ParseException("Missing 'end'")
            if (token == "end") {
                context.skipToken("end")
                break
            }

            list.add(CommandNode().apply { parse(context) })
        }
    }

    override fun toString(): String = list.toString()
}

/**
 * <command> ::= <repeat command> | <primitive command>
 */
class CommandNode : Node() {

    private lateinit var node: Node

    override fun parse(context: Context) {
        val token = context.currentToken() ?: throw ParseException("Missing 'end'")

        if (token == "repeat") {
            node = RepeatCommandNode().apply { parse(context) }
        } else {
            node = PrimitiveCommandNode().apply { parse(context) }
        }
    }

    override fun toString(): String = node.toString()
}

/**
 * <repeat command> ::= repeat <number> <command list>
 */
class RepeatCommandNode : Node() {

    private var number: Int = 0
    private lateinit var commandListNode: Node

    override fun parse(context: Context) {
        context.skipToken("repeat")
        number = context.currentNumber()
        context.nextToken()
        commandListNode = CommandListNode().apply { parse(context) }
    }

    override fun toString(): String = "[repeat $number $commandListNode]"
}

/**
 * <primitive command> ::= go | right | left
 */
class PrimitiveCommandNode : Node() {

    private lateinit var name: String
    private val commandNames = listOf("go", "right", "left")

    override fun parse(context: Context) {
        name = context.currentToken() ?: throw ParseException("Missing command")
        if (name !in commandNames) throw ParseException("$name is undefined")
        context.skipToken(name)
    }

    override fun toString(): String = name
}

class ParseException(message: String) : Exception(message)
