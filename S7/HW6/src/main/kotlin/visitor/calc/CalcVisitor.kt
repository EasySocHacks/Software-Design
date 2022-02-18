package visitor.calc

import token.Token
import visitor.TokenVisitor
import visitor.VisitorState
import visitor.calc.state.CalcVisitorState

object CalcVisitor: TokenVisitor<CalcVisitorState>() {
    override fun visit(token: Token, state: VisitorState<*>?) {
        (state as CalcVisitorState).proceedToken(token)
    }
}