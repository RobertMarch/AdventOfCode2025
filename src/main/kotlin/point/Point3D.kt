package point

import kotlin.math.abs

class Point3D(val x: Long, val y: Long, val z: Long) {

    operator fun plus(other: Point3D): Point3D {
        return Point3D(x + other.x, y + other.y, z + other.z)
    }

    operator fun minus(other: Point3D): Point3D {
        return Point3D(x - other.x, y - other.y, z - other.z)
    }

    operator fun times(scaleFactor: Long): Point3D {
        return Point3D(x * scaleFactor, y * scaleFactor, z * scaleFactor)
    }

    fun manhattenDist(): Long {
        return abs(x) + abs(y) + abs(z)
    }

    fun lengthSquared(): Long {
        return x*x + y*y + z*z
    }

    fun toMinimalString(): String {
        return "$x,$y,$z"
    }

    override fun toString(): String {
        return "(x: $x, y: $y, z: $z)"
    }

    override fun equals(other: Any?): Boolean {
        return (other is Point3D) && (x == other.x) && (y == other.y) && (z == other.z)
    }

    override fun hashCode(): Int {
        return x.hashCode() + y.hashCode() + z.hashCode()
    }
}
