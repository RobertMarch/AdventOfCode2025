package days

import point.Point2D

class Day04 : Day(4) {

    private val directions: List<Point2D> = listOf(
        Point2D(-1, -1),
        Point2D(-1, 0),
        Point2D(-1, 1),
        Point2D(0, 1),
        Point2D(0, -1),
        Point2D(1, -1),
        Point2D(1, 0),
        Point2D(1, 1),
    )

    override fun solvePartOne(inputString: String): Int {
        val paperRolls: Set<Point2D> = parseInput(inputString)

        return getRemovableRolls(paperRolls).size
    }

    override fun solvePartTwo(inputString: String): Any {
        val paperRolls: MutableSet<Point2D> = parseInput(inputString).toMutableSet()

        var count: Int = 0
        var rollsToRemove: Set<Point2D> = getRemovableRolls(paperRolls)

        while (rollsToRemove.isNotEmpty()) {
            count += rollsToRemove.size
            paperRolls.removeAll(rollsToRemove)
            rollsToRemove = getRemovableRolls(paperRolls)
        }

        return count
    }

    private fun parseInput(inputString: String): Set<Point2D> {
        val paperRolls: MutableSet<Point2D> = mutableSetOf()

        inputString.lines().mapIndexed { yIndex, line ->
            line.mapIndexed { xIndex, char ->
                if (char == '@') {
                    paperRolls.add(Point2D(xIndex.toLong(), yIndex.toLong()))
                }
            }
        }

        return paperRolls
    }

    private fun getRemovableRolls(paperRolls: Set<Point2D>): Set<Point2D> {
        return paperRolls.filter { roll ->
            val neighbours = directions.count { paperRolls.contains(roll + it) }

            neighbours < 4
        }.toSet()
    }
}

fun main() {
    val day = Day04()
    println(day.solvePartOneFromFile())
    println(day.solvePartTwoFromFile())
}
