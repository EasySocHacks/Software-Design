package token.torinizer.state

import token.*
import java.util.*

class Tokenizer(
    private var rest: String,
    private val buffer: Stack<Token> = Stack()
) {
    private enum class TokenizerHelperEnum {
        WHITESPACE {
            override fun getToken(c: Char): Token? = when {
                c.isWhitespace() -> DummyToken
                else -> null
            }
        },

        PLUS {
            override fun getToken(c: Char): Token? = matchSymbolToken(c, '+', PlusToken)
        },

        MINUS {
            override fun getToken(c: Char): Token? = matchSymbolToken(c, '-', MinusToken)
        },

        PRODUCE {
            override fun getToken(c: Char): Token? = matchSymbolToken(c, '*', ProduceToken)
        },

        DIVIDE {
            override fun getToken(c: Char): Token? = matchSymbolToken(c, '/', DivideToken)
        },

        OPEN_BRACKET {
            override fun getToken(c: Char): Token? = matchSymbolToken(c, '(', OpenBracketToken)
        },

        CLOSE_BRACKET {
            override fun getToken(c: Char): Token? = matchSymbolToken(c, ')', CloseBracketToken)
        },

        DIGIT {
            override fun getToken(c: Char): Token? = when {
                c.isDigit() -> NumberToken(c.toString())
                else -> null
            }
        };

        fun matchSymbolToken(c: Char, match: Char, token: Token): Token? = when (c) {
            match -> token
            else -> null
        }

        abstract fun getToken(c: Char): Token?
    }

    private fun hasNextState(): Boolean {
        return rest.isNotEmpty()
    }

    private fun nextState() {
        if (rest.isEmpty()) {
            return
        }

        var token: Token? = DummyToken
        val c = rest.first()
        rest = rest.substring(1)
        for (element in TokenizerHelperEnum.values()) {
            token = element.getToken(c)

            if (token != null) {
                break
            }
        }

        while (token is NumberToken && buffer.isNotEmpty()) {
            val top = buffer.pop()

            if (top is NumberToken) {
                token = NumberToken("${top.number}${token.number}")
            } else {
                buffer.push(top)
                break
            }
        }

        buffer.push(token)
    }

    fun tokenize(): List<Token> {
        while (hasNextState()) {
            nextState()
        }

        return buffer.filter { token -> token !is DummyToken }.plus(EOF)
    }
}