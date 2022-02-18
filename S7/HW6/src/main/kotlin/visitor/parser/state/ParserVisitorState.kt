package visitor.parser.state

import token.*
import visitor.VisitorState
import java.util.*

class ParserVisitorState : VisitorState<Pair<List<Token>, Stack<Token>>> {
    private val stack: Stack<Token> = Stack()
    private val result: MutableList<Token> = mutableListOf()

    fun appendToken(token: Token) {
        when (token) {
            is NumberToken -> result.add(token)

            is OperationToken -> {
                if (stack.isEmpty()) {
                    stack.push(token)
                    return
                }

                val top = stack.peek()

                if (top is OperationToken && top.priority <= token.priority) {
                    result.add(stack.pop())
                }

                stack.push(token)
            }

            is OpenBracketToken -> stack.push(token)

            is CloseBracketToken -> {
                while (stack.isNotEmpty()) {
                    val top = stack.pop()

                    if (top is OpenBracketToken) {
                        return
                    }

                    result.add(top)
                }

                throw IllegalStateException("Parse error: No matching bracket found while parsing")
            }

            is EOF ->
                while (stack.isNotEmpty()) {
                    result.add(stack.pop())
                }
        }
    }

    override fun getState(): Pair<List<Token>, Stack<Token>> = result to stack
}