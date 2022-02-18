package easy.soc.hacks.hw5.graph

import easy.soc.hacks.hw5.graph.Graph.*
import java.io.File

class AdjacencyListGraph(
    list: List<List<Int>>
) : Graph {
    class AdjacencyListGraphParser(file: File) : GraphParser(file) {
        override val graph: Graph

        init {
            val adjacencyListGraphList: MutableList<List<Int>> = mutableListOf()

            var lineNumber = -1
            file.forEachLine { line ->
                lineNumber++

                val splitByColon = line.split(":")

                if (splitByColon.size != 2) {
                    throw IllegalArgumentException("Each matrix representation line must be in format 'from:[ to[, to]]'")
                }

                if (splitByColon.first() != lineNumber.toString()) {
                    throw IllegalArgumentException("Line No {x} in matrix representation must starts with '{x}:'")
                }

                adjacencyListGraphList.add(
                    splitByColon.last().split(",").map { element ->
                        element.trim().toInt()
                    }.toList()
                )
            }

            graph = AdjacencyListGraph(adjacencyListGraphList)
        }
    }

    private val nodes: MutableList<Node> = mutableListOf()
    private val edgeMatrix: MutableList<MutableList<Edge?>> = mutableListOf()

    init {
        for (i in list.indices) {
            nodes.add(Node(i))
            edgeMatrix.add(mutableListOf())

            for (j in list.indices) {
                edgeMatrix[i].add(null)
            }
        }

        for (from in list.indices) {
            for (to in list[from]) {
                assert(to >= 0 && to < list.size)

                edgeMatrix[from][to] = Edge(nodes[from], nodes[to])
            }
        }
    }

    override fun getNodes(): List<Node> = nodes.toList()

    override fun getNode(index: Int): Node = nodes[index]

    override fun getEdgesFrom(node: Node): List<Edge> = edgeMatrix[node.index].filterNotNull()

    override fun getEdgesTo(node: Node): List<Edge> = edgeMatrix.mapNotNull { it[node.index] }
}