# Interpreter - 文法規則をクラスで表現する

```
<program> ::= program <command list>
<command list> ::= <command>* end
<command> ::= <repeat command> | <primitive command>
<repeat command> ::= repeat <number> <command list>
<primitive command> ::= go | right | left
```

* ProgramNode
* CommandListNode
* CommandNode
* RepeatCommandNode
* PrimitiveCommandNode
