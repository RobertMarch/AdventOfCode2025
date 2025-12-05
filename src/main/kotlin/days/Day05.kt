package days

class Day05 : Day(5) {

    private data class Range(var start: Long, var end: Long)

    override fun solvePartOne(inputString: String): Int {
        val (freshIdRanges: List<Range>, ids: List<Long>) = parseInput(inputString)

        return ids.count { id ->
            freshIdRanges.any { it.start <= id && it.end >= id }
        }
    }

    override fun solvePartTwo(inputString: String): Long {
        val (freshIdRanges: List<Range>, _) = parseInput(inputString)

        val combinedRanges: MutableList<Range> = mutableListOf()
        var lastRange: Range? = null

        for (range in freshIdRanges) {
            if (lastRange != null && range.start <= lastRange.end) {
                if (range.end > lastRange.end) {
                    lastRange.end = range.end
                }
            } else {
                combinedRanges.add(range)
                lastRange = range
            }
        }

        return combinedRanges.sumOf { it.end - it.start + 1 }
    }

    private fun parseInput(inputString: String): Pair<List<Range>, List<Long>> {
        val parts: List<String> = inputString.replace("\r", "").split("\n\n")
        val freshIdRanges: List<Range> = parts[0].lines().map { line ->
            val ends: List<Long> = line.split("-").map { it.toLong() }
            Range(ends[0], ends[1])
        }.sortedBy { it.start }
        val ids: List<Long> = parts[1].lines().map { it.toLong() }

        return Pair(freshIdRanges, ids)
    }
}

fun main() {
    val day = Day05()
    println(day.solvePartOneFromFile())
    println(day.solvePartTwoFromFile())
}
