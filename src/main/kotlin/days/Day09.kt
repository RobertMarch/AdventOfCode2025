package days

import point.Point2D
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min
import kotlin.math.sign

class Day09 : Day(9) {

    private val directions: List<Point2D> = listOf(
        Point2D(1, 1),
        Point2D(1, -1),
        Point2D(-1, 1),
        Point2D(-1, -1),
    )

    override fun solvePartOne(inputString: String): Long {
        val redTiles: List<Point2D> = inputString.lines().map { line ->
            val coords: List<Long> = line.split(",").map { it.toLong() }
            Point2D(coords[0], coords[1])
        }

        return redTiles.maxOf { a ->
            redTiles.maxOf { b -> area(a, b) }
        }
    }

    private fun area(a: Point2D, b: Point2D): Long {
        val v: Point2D = a - b
        return (abs(v.x) + 1) * (abs(v.y) + 1)
    }

    override fun solvePartTwo(inputString: String): Long {
        val redTiles: List<Point2D> = inputString.lines().map { line ->
            val coords: List<Long> = line.split(",").map { it.toLong() }
            Point2D(coords[0], coords[1])
        }
        val cornerDetails: Map<Point2D, Pair<Boolean, Point2D>> = redTiles.mapIndexed { index, corner ->
            val inDirection = corner - redTiles[(index - 1 + redTiles.size) % redTiles.size]
            val outDirection = redTiles[(index + 1) % redTiles.size] - corner
            val isExternal: Boolean = isCornerExternal(inDirection, outDirection)
            var cornerVector: Point2D = outDirection - inDirection
            cornerVector = reduceVectorToSigns(cornerVector)
            Pair(corner, Pair(isExternal, cornerVector))
        }.toMap()
        val outsidePoints: List<Point2D> = cornerDetails.flatMap { entry ->
            val externalDirections: List<Point2D> = directions.filter { (it == entry.value.second) != entry.value.first }
            externalDirections.map { entry.key + it }
        }
        val allEdgePoints: List<Point2D> = redTiles.flatMapIndexed { index, point ->
            val nextPoint: Point2D = redTiles[(index + 1) % redTiles.size]
            val dir: Point2D = reduceVectorToSigns(nextPoint - point)

            val edgePoints: MutableList<Point2D> = mutableListOf()
            var currEdgePoint: Point2D = point + dir

            while (currEdgePoint != nextPoint) {
                edgePoints.add(currEdgePoint)
                currEdgePoint += dir
            }

            edgePoints
        }

        val areas: List<Triple<Long, Point2D, Point2D>> = redTiles.flatMapIndexed { aIndex, a ->
            redTiles.subList(aIndex, redTiles.size)
                .map { b -> Triple(area(a, b), a,b) }
        }.sortedByDescending { it.first }

        return areas.first { area ->
            redTiles.none { isContained(area.second, area.third, it) }
                    && outsidePoints.none { isContained(area.second, area.third, it) }
                    && allEdgePoints.none { isContained(area.second, area.third, it) }
        }.first
    }

    private fun reduceVectorToSigns(v: Point2D): Point2D {
        return Point2D(v.x.sign.toLong(), v.y.sign.toLong())
    }

    private fun isCornerExternal(inDirection: Point2D, outDirection: Point2D): Boolean {
        return (inDirection.x > 0 && outDirection.y > 0)
                || (inDirection.y > 0 && outDirection.x < 0)
                || (inDirection.x < 0 && outDirection.y < 0)
                || (inDirection.y < 0 && outDirection.x > 0)
    }

    private fun isContained(corner1: Point2D, corner2: Point2D, pointToCheck: Point2D): Boolean {
        val minX: Long = min(corner1.x, corner2.x)
        val maxX: Long = max(corner1.x, corner2.x)
        val minY: Long = min(corner1.y, corner2.y)
        val maxY: Long = max(corner1.y, corner2.y)
        return pointToCheck.x > minX && pointToCheck.x < maxX
                && pointToCheck.y > minY && pointToCheck.y < maxY
    }
}

fun main() {
    val day = Day09()
    println(day.solvePartOneFromFile())
    println(day.solvePartTwoFromFile())
}
