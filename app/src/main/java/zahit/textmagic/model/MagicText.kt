package zahit.textmagic.model

import android.view.View

data class MagicText(
    val text: String,
    val points: MutableList<Point> = mutableListOf(),
    val textViewId: Int = View.generateViewId()
) {
    fun addPoint(point: Point) {
        points.add(point)
    }

    override fun equals(other: Any?): Boolean {
        return textViewId == (other as? MagicText)?.textViewId
    }

    override fun hashCode(): Int {
        return textViewId.hashCode()
    }
}

data class Point(
    val x: Float,
    val y: Float
)