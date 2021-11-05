package easy.soc.hacks.hw5

import easy.soc.hacks.hw5.graph.AdjacencyListGraph
import easy.soc.hacks.hw5.graph.Graph
import easy.soc.hacks.hw5.graph.MatrixGraph
import easy.soc.hacks.hw5.visualizer.AwtGraphVisualizer
import easy.soc.hacks.hw5.visualizer.FXGraphVisualizer
import easy.soc.hacks.hw5.visualizer.GraphVisualizer
import java.io.File
import kotlin.system.exitProcess

class Main {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val usage = "Usage: Main.kt <-v (FX, AWT)> <-g (MATRIX, AL)> <-gf (graph file path)> [-s (seed)]"

            val visualizer: GraphVisualizer
            val graph: Graph

            var visualizerName: String? = null
            var graphName: String? = null
            var graphPath: String? = null
            var seed = 0L

            var command: String? = null
            for (i in args.indices) {
                val arg = args[i]

                when (command) {
                    "visualizerName" -> visualizerName = arg
                    "graphName" -> graphName = arg
                    "graphPath" -> graphPath = arg
                    "seed" -> seed = arg.toLong()
                    else ->
                        if (i != args.size - 1) {
                            command = when (arg) {
                                "-v", "--visualizer" -> "visualizerName"
                                "-g", "--graph" -> "graphName"
                                "-gf", "-gp", "--graph-path", "--graph-file" -> "graphPath"
                                "-s", "--seed" -> "seed"
                                else -> {
                                    println(usage)
                                    exitProcess(0)
                                }
                            }

                            continue
                        } else {
                            println(usage)
                            exitProcess(0)
                        }
                }

                command = null
            }

            if (visualizerName == null || graphName == null || graphPath == null) {
                println(usage)
                exitProcess(0)
            }

            val graphFile = File(graphPath)

            graph = when (graphName.uppercase()) {
                "MATRIX" -> MatrixGraph.MatrixGraphParser(graphFile).graph
                "AL", "ADJACENCY_LIST", "ADJACENCY-LIST" -> AdjacencyListGraph.AdjacencyListGraphParser(graphFile).graph
                else -> throw IllegalArgumentException("Mu such graph by name ${graphName.uppercase()}")
            }

            visualizer = when (visualizerName.uppercase()) {
                "FX", "JAVAFX", "JAVA-FX", "JAVA_FX" -> FXGraphVisualizer(graph, seed)
                "AWT", "JAVAAWT", "JAVA-AWT", "JAVA_AWT" -> AwtGraphVisualizer(graph, seed)
                else -> throw IllegalArgumentException("No such visualizer by name ${visualizerName.uppercase()}")
            }

            visualizer.visualize()
        }
    }
}