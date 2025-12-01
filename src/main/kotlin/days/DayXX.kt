package days

class DayXX : Day(99) {

    override fun solvePartOne(inputString: String): Any {
        return -1
    }

    override fun solvePartTwo(inputString: String): Any {
        return -1
    }
}

fun main() {
    val day = DayXX()
    println(day.solvePartOneFromFile())
    println(day.solvePartTwoFromFile())
}
