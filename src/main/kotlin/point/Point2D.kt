package point

import kotlin.math.abs

class Point2D(val x: Long, val y: Long) {

    operator fun plus(other: Point2D): Point2D {
        return Point2D(x + other.x, y + other.y)
    }

    operator fun minus(other: Point2D): Point2D {
        return Point2D(x - other.x, y - other.y)
    }

    operator fun times(scaleFactor: Long): Point2D {
        return Point2D(x * scaleFactor, y * scaleFactor)
    }

    fun manhattenDist(): Long {
        return abs(x) + abs(y)
    }

    fun toMinimalString(): String {
        return "$x,$y"
    }

    override fun toString(): String {
        return "(x: $x, y: $y)"
    }

    override fun equals(other: Any?): Boolean {
        return (other is Point2D) && (x == other.x) && (y == other.y)
    }

    override fun hashCode(): Int {
        return x.hashCode() + y.hashCode()
    }
}
