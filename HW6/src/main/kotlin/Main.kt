import token.torinizer.state.Tokenizer
import visitor.calc.CalcVisitor
import visitor.calc.state.CalcVisitorState
import visitor.parser.ParserVisitor
import visitor.parser.state.ParserVisitorState
import visitor.print.PrintVisitor

fun main() {
    val tokenizer = Tokenizer("1+2*(3 + 4) * 5452")
    val tokens = tokenizer.tokenize()

    val parserState = ParserVisitorState()
    val parseVisitor = ParserVisitor
    parseVisitor.visit(tokens.iterator(), parserState)

    val printVisitor = PrintVisitor
    printVisitor.visit(parserState.getState().first.iterator())

    val calcState = CalcVisitorState()
    val calcVisitor = CalcVisitor
    calcVisitor.visit(parserState.getState().first.iterator(), calcState)

    println()
    println(calcState.getState())
}