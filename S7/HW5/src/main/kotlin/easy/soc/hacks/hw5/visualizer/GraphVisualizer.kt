package easy.soc.hacks.hw5.visualizer

import easy.soc.hacks.hw5.graph.Graph
import kotlin.random.Random

abstract class GraphVisualizer(
    private val graph: Graph,
    private val seed: Long
) {
    data class Point(
        val x: Double,
        val y: Double,
        val label: String = "",
        val radius: Double = 10.0
    )

    abstract val width: Double
    abstract val height: Double
    abstract val borderSize: Double
    abstract fun drawPoint(point: Point)
    abstract fun drawLine(from: Point, to: Point)
    abstract fun flush()

    fun visualize() {
        val random = Random(seed)

        val nodes = graph.getNodes()
        val points = nodes.sortedBy { it.index }.map {
            Point(
                random.nextDouble(0.0 + borderSize, width - borderSize),
                random.nextDouble(0.0 + borderSize, height - borderSize),
                it.index.toString()
            )
        }

        for (i in points.indices) {
            val point = points[i]
            drawPoint(point)

            for (edge in graph.getEdgesFrom(nodes[i])) {
                drawLine(point, points[edge.to.index])
            }
        }

        flush()
    }
}