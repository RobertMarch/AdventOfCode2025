package days

class Day02 : Day(2) {

    override fun solvePartOne(inputString: String): Any {
        return solve(inputString, true)
    }

    override fun solvePartTwo(inputString: String): Any {
        return solve(inputString, false)
    }

    private fun solve(inputString: String, partOne: Boolean): Long {
        var resultTotal: Long = 0

        inputString.split(",").map { range ->
            val ends: List<Long> = range.split("-").map { it.toLong() }
            for (id in ends[0]..ends[1]) {
                if (if (partOne) isInvalidRepeatedOnce(id) else isInvalidRepeatedAtLeastOnce(id)) {
                    resultTotal += id
                }
            }
        }

        return resultTotal
    }

    private fun isInvalidRepeatedOnce(id: Long): Boolean {
        val idString: String = id.toString()
        if (idString.length % 2 != 0) {
            return false
        }
        return idString.substring(0, idString.length/2) == idString.substring(idString.length/2)
    }

    private fun isInvalidRepeatedAtLeastOnce(id: Long): Boolean {
        val idString: String = id.toString()
        val idStringLength: Int = idString.length
        for (repeatLength in 1..idStringLength / 2) {
            if (idStringLength % repeatLength != 0) {
                continue
            }
            val repeatSequence: String = idString.substring(0, repeatLength)
            if (idString.replace(repeatSequence, "").isEmpty()) {
                return true
            }
        }
        return false
    }
}

fun main() {
    val day = Day02()
    println(day.solvePartOneFromFile())
    println(day.solvePartTwoFromFile())
}
