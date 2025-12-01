package days

class Day01 : Day(1) {

    override fun solvePartOne(inputString: String): Int {
        var currentPosition: Int = 50
        var visitedCount: Int = 0

        inputString.lines().forEach {
            val direction: Int = if (it[0] == 'R') 1 else -1
            val distance: Int = it.substring(1).toInt()

            currentPosition += direction * distance + 100
            currentPosition %= 100

            if (currentPosition == 0) {
                visitedCount++
            }
        }

        return visitedCount
    }

    override fun solvePartTwo(inputString: String): Int {
        var currentPosition: Int = 50
        var visitedCount: Int = 0

        inputString.lines().forEach {
            val direction: Int = if (it[0] == 'R') 1 else -1
            val distance: Int = it.substring(1).toInt()

            val fullRotations: Int = distance / 100
            visitedCount += fullRotations

            val partRotation: Int = distance % 100
            val nextPosition: Int = currentPosition + direction * partRotation

            if (currentPosition != 0 && (nextPosition <= 0 || nextPosition >= 100)) {
                visitedCount++
            }

            currentPosition = (nextPosition + 100) % 100
        }

        return visitedCount
    }
}

fun main() {
    val day = Day01()
    println(day.solvePartOneFromFile())
    println(day.solvePartTwoFromFile())
}
