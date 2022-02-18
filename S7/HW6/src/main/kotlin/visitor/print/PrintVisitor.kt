package visitor.print

import token.Token
import visitor.TokenVisitor
import visitor.VisitorState

object PrintVisitor: TokenVisitor<VisitorState<*>>() {
    override fun visit(token: Token, state: VisitorState<*>?) {
        print("${token.value} ")
    }
}