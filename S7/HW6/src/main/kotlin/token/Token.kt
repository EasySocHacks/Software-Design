package token

abstract class Token(
    val value: String? = null
)

abstract class OperationToken(value: String? = null, val priority: UInt) : Token(value)
abstract class BinaryOperationToken(value: String? = null, priority: UInt) : OperationToken(value, priority)

object DummyToken : Token()

object PlusToken : BinaryOperationToken("+", 1u)

object MinusToken : BinaryOperationToken("-", 1u)

object ProduceToken : BinaryOperationToken("*", 0u)

object DivideToken : BinaryOperationToken("/", 0u)

object OpenBracketToken : Token("(")

object CloseBracketToken : Token(")")

data class NumberToken(val number: String) : Token(number) {
    operator fun plus(numberToken: NumberToken) =
        NumberToken(number + numberToken.number)
}

object EOF : Token()