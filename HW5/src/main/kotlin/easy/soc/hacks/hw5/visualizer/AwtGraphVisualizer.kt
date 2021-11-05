package easy.soc.hacks.hw5.visualizer

import easy.soc.hacks.hw5.graph.Graph
import java.awt.Color
import java.awt.Frame
import java.awt.Graphics
import java.awt.Graphics2D
import java.awt.event.WindowAdapter
import java.awt.event.WindowEvent
import java.awt.geom.Ellipse2D
import java.awt.geom.Line2D
import kotlin.system.exitProcess

class AwtGraphVisualizer(
    graph: Graph,
    seed: Long = 0L,
    override val width: Double = 1000.0,
    override val height: Double = 800.0,
    override val borderSize: Double = 200.0
) : GraphVisualizer(graph, seed) {
    private val drawingStackList: MutableList<(Graphics2D) -> Unit> = mutableListOf()

    override fun drawPoint(point: Point) {
        drawingStackList.add { graphics ->
            graphics.paint = Color.black
            graphics.fill(
                Ellipse2D.Double(
                    point.x,
                    point.y,
                    point.radius,
                    point.radius
                )
            )

            graphics.paint = Color.red
            graphics.drawString(
                point.label,
                (point.x + point.radius).toFloat(),
                (point.y + point.radius).toFloat()
            )
        }
    }

    override fun drawLine(from: Point, to: Point) {
        drawingStackList.add { graphics ->
            graphics.paint = Color.black
            graphics.draw(
                Line2D.Double(
                    from.x + from.radius / 2.0,
                    from.y + from.radius / 2.0,
                    to.x + to.radius / 2.0,
                    to.y + to.radius / 2.0
                )
            )
        }
    }

    override fun flush() {
        val awtVisualizer = AwtVisualizer(
            { graphics ->
                drawingStackList.fold(Unit) { _, op -> op(graphics) }
            },
            width,
            height
        )

        awtVisualizer.addWindowListener(object : WindowAdapter() {
            override fun windowClosing(we: WindowEvent) {
                exitProcess(0)
            }
        })

        awtVisualizer.pack()
        awtVisualizer.isVisible = true
    }
}

class AwtVisualizer(
    private val drawingStack: (Graphics2D) -> Unit,
    private val width: Double,
    private val height: Double
) : Frame() {
    override fun paint(graphics: Graphics) {
        val graphics2D = graphics as Graphics2D

        setSize(width.toInt(), height.toInt())

        drawingStack(graphics2D)
    }
}