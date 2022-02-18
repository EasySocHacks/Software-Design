package easy.soc.hacks.hw5.graph

import java.io.File

interface Graph {
    abstract class GraphParser(
        val file: File
    ) {
        abstract val graph: Graph
    }

    class Node(
        val index: Int
    )

    class Edge(
        val from: Node,
        val to: Node
        )

    fun getNodes(): List<Node>

    fun getNode(index: Int): Node

    fun getEdgesFrom(node: Node): List<Edge>

    fun getEdgesTo(node: Node): List<Edge>
}