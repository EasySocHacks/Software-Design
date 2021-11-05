package easy.soc.hacks.hw5.visualizer

import easy.soc.hacks.hw5.graph.Graph
import javafx.application.Application
import javafx.scene.Group
import javafx.scene.Scene
import javafx.scene.canvas.Canvas
import javafx.scene.canvas.GraphicsContext
import javafx.scene.paint.Color.BLACK
import javafx.scene.paint.Color.RED
import javafx.scene.shape.Line
import javafx.stage.Stage

class FXGraphVisualizer(
    graph: Graph,
    seed: Long = 0L,
    override val width: Double = 1000.0,
    override val height: Double = 800.0,
    override val borderSize: Double = 200.0
) : GraphVisualizer(graph, seed) {
    init {
        fxWidth = width
        fxHeight = height
    }

    override fun drawPoint(point: Point) {
        fxDrawingStack.add { graphicsContext, _ ->
            graphicsContext.fill = BLACK
            graphicsContext.fillOval(
                point.x,
                point.y,
                point.radius,
                point.radius
            )

            graphicsContext.fill = RED
            graphicsContext.fillText(
                point.label,
                point.x,
                point.y
            )
        }
    }

    override fun drawLine(from: Point, to: Point) {
        fxDrawingStack.add { graphicsContext, root ->
            graphicsContext.fill = BLACK
            root.children.add(
                Line(
                    from.x + from.radius / 2.0,
                    from.y + from.radius / 2.0,
                    to.x + to.radius / 2.0,
                    to.y + to.radius / 2.0
                )
            )
        }
    }

    override fun flush() {
        Application.launch(FXVisualizer::class.java)
    }

}

var fxWidth: Double = 1000.0
var fxHeight: Double = 800.0
val fxDrawingStack: MutableList<(GraphicsContext, Group) -> Unit> = mutableListOf()

class FXVisualizer : Application() {
    override fun start(primaryStage: Stage) {
        val root = Group()
        val canvas = Canvas(fxWidth, fxHeight)
        val graphicsContext: GraphicsContext = canvas.graphicsContext2D

        fxDrawingStack.fold(Unit) { _, op -> op(graphicsContext, root) }

        root.children.add(canvas)
        primaryStage.scene = Scene(root)
        primaryStage.show()
    }
}