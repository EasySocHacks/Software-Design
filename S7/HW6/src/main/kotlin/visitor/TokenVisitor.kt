package visitor

import token.Token

abstract class TokenVisitor<out S : VisitorState<*>> {
    abstract fun visit(token: Token, state: VisitorState<*>?)

    fun visit(iterator: Iterator<Token>, state: VisitorState<*>? = null) {
        while (iterator.hasNext()) {
            visit(iterator.next(), state)
        }
    }
}