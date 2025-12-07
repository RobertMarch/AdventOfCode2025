package days

import point.Point2D

class Day07 : Day(7) {

    private val down: Point2D = Point2D(0, 1)

    private val splitterNextPositions: List<Point2D> = listOf(Point2D(-1, 0), Point2D(1, 0))

    override fun solvePartOne(inputString: String): Long {
        val (start: Point2D, splitters: Set<Point2D>) = parseInput(inputString)

        var beamPositions: Set<Point2D> = setOf(start)
        var splitCount: Long = 0

        for (y in inputString.lines().indices) {
            val nextBeamPositions: MutableSet<Point2D> = beamPositions.flatMap { beam ->
                val nextBeam: Point2D = beam + down
                if (splitters.contains(nextBeam)) {
                    splitCount++
                    return@flatMap splitterNextPositions.map { nextBeam + it }
                }
                listOf(nextBeam)
            }.toMutableSet()

            beamPositions = nextBeamPositions
        }

        return splitCount
    }

    override fun solvePartTwo(inputString: String): Any {
        val (start: Point2D, splitters: Set<Point2D>) = parseInput(inputString)

        var beamPositions: Map<Point2D, Long> = mapOf(Pair(start, 1))

        for (y in inputString.lines().indices) {
            val nextBeamPositions: MutableMap<Point2D, Long> = mutableMapOf()

            beamPositions.keys.forEach { beam ->
                val nextBeam: Point2D = beam + down
                if (splitters.contains(nextBeam)) {
                    splitterNextPositions.map { nextBeam + it }
                        .forEach {
                            nextBeamPositions.compute(it) { _, value -> (value ?: 0) + beamPositions[beam]!! }
                        }
                } else {
                    nextBeamPositions.compute(nextBeam) { _, value -> (value ?: 0) + beamPositions[beam]!! }
                }
            }

            beamPositions = nextBeamPositions
        }

        return beamPositions.values.sum()
    }

    private fun parseInput(inputString: String): Pair<Point2D, Set<Point2D>> {
        var start: Point2D? = null
        val splitters: MutableSet<Point2D> = mutableSetOf()

        inputString.lines().mapIndexed { yIndex, line ->
            line.mapIndexed { xIndex, char ->
                if (char == 'S') {
                    start = Point2D(xIndex.toLong(), yIndex.toLong())
                }
                if (char == '^') {
                    splitters.add(Point2D(xIndex.toLong(), yIndex.toLong()))
                }
            }
        }

        return Pair(start!!, splitters)
    }
}

fun main() {
    val day = Day07()
    println(day.solvePartOneFromFile())
    println(day.solvePartTwoFromFile())
}
