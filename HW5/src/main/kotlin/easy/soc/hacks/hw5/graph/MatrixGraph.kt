package easy.soc.hacks.hw5.graph

import easy.soc.hacks.hw5.graph.Graph.*
import java.io.File

class MatrixGraph(
    private val matrix: List<List<Boolean>>
) : Graph {
    class MatrixGraphParser(file: File) : GraphParser(file) {
        override val graph: Graph

        init {
            val matrixGraphList: MutableList<List<Boolean>> = mutableListOf()

            file.forEachLine { line ->
                matrixGraphList.add(
                    line.split(" ").map { element ->
                        when (element.trim()) {
                            "0" -> false
                            "1" -> true
                            else -> throw IllegalArgumentException("Matrix must consists of elements either '0' or '1'")
                        }
                    }.toList()
                )
            }

            graph = MatrixGraph(matrixGraphList)
        }
    }

    private val nodes: MutableList<Node> = mutableListOf()
    private val edgeMatrix: MutableList<MutableList<Edge?>> = mutableListOf()

    init {
        assert(
            matrix
                .map { it.size == matrix.size }
                .all { it }
        )

        for (i in matrix.indices) {
            for (j in matrix[i].indices) {
                assert(matrix[i][j] != matrix[j][i])
            }
        }

        for (i in matrix.indices) {
            nodes.add(Node(i))
            edgeMatrix.add(mutableListOf())
        }

        for (i in matrix.indices) {
            for (j in matrix[i].indices) {
                edgeMatrix[i].add(
                    if (matrix[i][j]) {
                        Edge(nodes[i], nodes[j])
                    } else {
                        null
                    }
                )
            }
        }
    }

    override fun getNodes(): List<Node> = nodes.toList()

    override fun getNode(index: Int): Node = nodes[index]

    override fun getEdgesFrom(node: Node): List<Edge> = edgeMatrix[node.index].filterNotNull()

    override fun getEdgesTo(node: Node): List<Edge> = edgeMatrix.mapNotNull { it[node.index] }
}