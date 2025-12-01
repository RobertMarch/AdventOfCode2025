package days

import java.io.File

abstract class Day(private val dayNumber: Int) {

    private fun readFileAsString(): String {
        return File("src/main/resources/inputs/day%02d.txt".format(dayNumber)).readText()
    }

    fun solvePartOneFromFile(): Any {
        return solvePartOne(readFileAsString())
    }

    fun solvePartTwoFromFile(): Any {
        return solvePartTwo(readFileAsString())
    }

    abstract fun solvePartOne(inputString: String): Any

    abstract fun solvePartTwo(inputString: String): Any
}
