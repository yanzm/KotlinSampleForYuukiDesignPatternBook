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

package net.yanzm.sample.interpreter23.improved

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
            val node = ProgramNode.parse(Context(it))
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

sealed class Node

/**
 * <program> ::= program <command list>
 */
class ProgramNode(val commandListNode: Node) : Node() {

    override fun toString(): String = "[program $commandListNode]"

    companion object {
        fun parse(context: Context): ProgramNode {
            context.skipToken("program")
            return ProgramNode(CommandListNode.parse(context))
        }
    }
}

/**
 * <command list> ::= <command>* end
 */
class CommandListNode(val list: List<Node>) : Node() {

    override fun toString(): String = list.toString()

    companion object {
        fun parse(context: Context): CommandListNode {
            val list = mutableListOf<Node>()
            while (true) {
                val token = context.currentToken() ?: throw ParseException("Missing 'end'")
                if (token == "end") {
                    context.skipToken("end")
                    break
                }

                list.add(CommandNode.parse(context))
            }
            return CommandListNode(list)
        }
    }
}

/**
 * <command> ::= <repeat command> | <primitive command>
 */
class CommandNode(val node: Node) : Node() {

    override fun toString(): String = node.toString()

    companion object {
        fun parse(context: Context): CommandNode {
            val token = context.currentToken() ?: throw ParseException("Missing 'end'")
            val node = if (token == "repeat") {
                RepeatCommandNode.parse(context)
            } else {
                PrimitiveCommandNode.parse(context)
            }
            return CommandNode(node)
        }
    }
}

/**
 * <repeat command> ::= repeat <number> <command list>
 */
class RepeatCommandNode(val number: Int, val commandListNode: Node) : Node() {

    override fun toString(): String = "[repeat $number $commandListNode]"

    companion object {
        fun parse(context: Context): RepeatCommandNode {
            context.skipToken("repeat")
            val number = context.currentNumber()
            context.nextToken()
            return RepeatCommandNode(number, CommandListNode.parse(context))
        }
    }
}

/**
 * <primitive command> ::= go | right | left
 */
data class PrimitiveCommandNode(val name: String) : Node() {

    override fun toString(): String = name

    companion object {
        private val commandNames = listOf("go", "right", "left")

        fun parse(context: Context): PrimitiveCommandNode {
            val name = context.currentToken() ?: throw ParseException("Missing command")
            if (name !in commandNames) throw ParseException("$name is undefined")
            context.skipToken(name)
            return PrimitiveCommandNode(name)
        }
    }
}

class ParseException(message: String) : Exception(message)
