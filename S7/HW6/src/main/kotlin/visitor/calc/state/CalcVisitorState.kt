package visitor.calc.state

import token.*
import visitor.VisitorState
import java.math.BigDecimal
import java.util.*

class CalcVisitorState : VisitorState<BigDecimal?> {
    private val stack: Stack<Token> = Stack()

    fun proceedToken(token: Token) {
        when (token) {
            is OperationToken -> {
                val secondOperand = stack.pop()
                val firstOperand = stack.pop()

                val binOp: (BigDecimal, BigDecimal) -> BigDecimal =
                    when (token) {
                        is PlusToken -> { first, second ->
                            first + second
                        }

                        is MinusToken -> { first, second ->
                            first - second
                        }

                        is ProduceToken -> { first, second ->
                            first * second
                        }

                        is DivideToken -> { first, second ->
                            first / second
                        }

                        else -> throw IllegalStateException("Calculate error: Unexpected operation token $token")
                    }

                stack.push(
                    NumberToken(
                        binOp.invoke(
                            BigDecimal(firstOperand.value),
                            BigDecimal(secondOperand.value)
                        ).toString()
                    )
                )
            }

            is NumberToken -> stack.push(token)

            else -> throw IllegalStateException("Calculate error: Unexpected token $token")
        }
    }

    override fun getState(): BigDecimal = BigDecimal(stack.peek().value)
}