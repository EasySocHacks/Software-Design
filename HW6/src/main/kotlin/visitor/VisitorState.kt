package visitor

interface VisitorState<S> {
    fun getState(): S
}