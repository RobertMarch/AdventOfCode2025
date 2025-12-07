package days

class Day06 : Day(6) {

    override fun solvePartOne(inputString: String): Any {
        val rows: List<List<String>> = inputString.lines().map { line ->
            line.split(" ").filter { it.isNotBlank() }
        }
        val numbers: List<List<Long>> = rows.subList(0, rows.size - 1).map { row -> row.map { it.toLong() } }
        val operations: List<String> = rows.last()

        var result = 0L
        
        for (i in rows[0].indices) {
            val problemNumbers: List<Long> = numbers.map { row -> row[i] }
            result += evaluateProblem(problemNumbers, operations[i])
        }
        
        return result
    }

    override fun solvePartTwo(inputString: String): Any {
        val rows: List<String> = inputString.lines()
        val operations: List<String> = rows.last().split(" ").filter { it.isNotBlank() }

        val numbers: List<String> = rows.subList(0, rows.size - 1)

        val currentProblemNumbers: MutableList<Long> = mutableListOf()
        var operatorIndex: Int = 0
        var result: Long = 0L

        for (i in numbers[0].indices) {
            val numString: String = numbers.map { it[i] }.joinToString("")

            if (numString.isBlank()) {
                result += evaluateProblem(currentProblemNumbers, operations[operatorIndex])
                operatorIndex++
                currentProblemNumbers.removeAll(currentProblemNumbers)
            } else {
                currentProblemNumbers.add(numString.trim().toLong())
            }
        }
        if (currentProblemNumbers.isNotEmpty()){
            result += evaluateProblem(currentProblemNumbers, operations[operatorIndex])
        }

        return result
    }

    private fun evaluateProblem(numbers: List<Long>, operation: String): Long {
        return if (operation == "+") {
            numbers.sum()
        } else {
            numbers.reduce { acc, l -> acc * l }
        }
    }
}

fun main() {
    val day = Day06()
    println(day.solvePartOneFromFile())
    println(day.solvePartTwoFromFile())
}
