package visitor.parser

import token.Token
import visitor.TokenVisitor
import visitor.VisitorState
import visitor.parser.state.ParserVisitorState

object ParserVisitor : TokenVisitor<ParserVisitorState>() {
    override fun visit(token: Token, state: VisitorState<*>?) {
        (state as ParserVisitorState).appendToken(token)
    }
}