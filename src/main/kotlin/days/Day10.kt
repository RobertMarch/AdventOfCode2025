package days

import kotlin.math.pow

private typealias ButtonConnections = List<Int>

private typealias SolutionVector = Map<Int, Int>

private typealias Solution = Pair<SolutionVector, Int>

class Day10 : Day(10) {

    private data class Machine(
        val indicatorLights: Int,
        val buttonValues: List<Int>,
        val buttonIndexes: List<ButtonConnections>,
        val joltageRequirements: List<Int>
    )

    private data class Counter(val index: Int, val remainingCount: Int, val connectedButtons: List<Int>)

    override fun solvePartOne(inputString: String): Int {
        val machines: List<Machine> = parseInput(inputString)

        return machines.sumOf { machine ->
            var currentOptions: Map<Set<Int>, Int> = mapOf(setOf<Int>() to 0)

            while (currentOptions.keys.first().size < machine.buttonValues.size) {
                val nextOptions: MutableMap<Set<Int>, Int> = mutableMapOf()
                currentOptions.forEach { option ->
                    machine.buttonValues.filterNot { option.key.contains(it) }.forEach { button ->
                        val nextValue: Int = button xor option.value
                        if (nextValue == machine.indicatorLights) {
                            return@sumOf option.key.size + 1
                        }
                        val nextButtonSet: Set<Int> = option.key.union(listOf(button))
                        nextOptions[nextButtonSet] = nextValue
                    }
                }
                currentOptions = nextOptions
            }

            10000
        }
    }

    private fun parseInput(inputString: String): List<Machine> {
        return inputString.lines().map { line ->
            val parts: List<String> = line.split(" ")
            val indicatorLights: Int = trimStartAndEndChars(parts[0])
                .replace(".", "0")
                .replace("#", "1")
                .reversed()
                .toInt(2)
            val joltageRequirements: List<Int> = trimStartAndEndChars(parts.last()).split(",").map { it.toInt() }

            val buttons: List<List<Int>> = parts.subList(1, parts.size - 1).map { button ->
                trimStartAndEndChars(button).split(",").map { it.toInt() }
            }
            val buttonValues: List<Int> = buttons.map { button -> button.sumOf { 2.0.pow(it).toInt() } }

            Machine(indicatorLights, buttonValues, buttons, joltageRequirements)
        }
    }

    private fun trimStartAndEndChars(value: String): String {
        return value.substring(1, value.length - 1)
    }

    override fun solvePartTwo(inputString: String): Any {
        val machines: List<Machine> = parseInput(inputString)

        return machines.sumOf { machine ->
            val connections: MutableMap<Int, List<Int>> = mutableMapOf()
            machine.buttonIndexes.forEachIndexed { buttonIndex, button ->
                button.forEach {
                    connections.compute(it) { _, value ->
                        (value ?: listOf()) + buttonIndex
                    }
                }
            }

            val counters: List<Counter> =
                connections.entries.map { Counter(it.key, machine.joltageRequirements[it.key], it.value) }
            val reducedCounters: List<Counter> = reduceCounters(counters)

            val res: List<SolutionVector> = getSolutionVectors(reducedCounters)
            val res2: List<Solution> = res
                .filter { solution -> solution.entries.count { it.key < 0 && it.value == 1 } <= 1 }
                .map { solution ->
                    Pair(
                        solution.filterKeys { it >= 0 },
                        -((solution.entries.find { it.key < 0 && it.value == 1 }?.key ?: 0) + 100)
                    )
                }

            val bestResult = findBestCombinationOfSolution(res2, reducedCounters)
            println("$bestResult, with ${res2.size} options: $res2  ")
            bestResult
        }
    }

    private fun reduceCounters(counters: List<Counter>): List<Counter> {
        var hasReduction: Boolean = true
        val updatedCounters: MutableList<Counter> = counters.toMutableList()
        while (hasReduction) {
            hasReduction = false
            for (counterIndex1 in updatedCounters.indices) {
                val counter1: Counter = updatedCounters[counterIndex1]
                for (counterIndex2 in updatedCounters.indices) {
                    if (counterIndex1 == counterIndex2) {
                        continue
                    }
                    val counter2: Counter = updatedCounters[counterIndex2]
                    if (counter1.connectedButtons.containsAll(counter2.connectedButtons)) {
                        val newCounter1: Counter = Counter(
                            counter1.index,
                            counter1.remainingCount - counter2.remainingCount,
                            counter1.connectedButtons - counter2.connectedButtons.toSet()
                        )
                        updatedCounters[counterIndex1] = newCounter1
                        hasReduction = true
                    }
                }
            }
            updatedCounters.removeIf { it.connectedButtons.isEmpty() }
        }
        return updatedCounters
    }

    private fun getSolutionVectors(counters: List<Counter>): List<Map<Int, Int>> {
        if (counters.isEmpty()) {
            return listOf(mapOf())
        }

        val determinedCounter: Counter? = counters.find { it.connectedButtons.size == 1 }

        val buttonIndex: Int
        val buttonPressCounts: List<Int>
        val hasMultipleOptions: Boolean = determinedCounter == null

        if (determinedCounter != null) {
            buttonIndex = determinedCounter.connectedButtons[0]
            buttonPressCounts = listOf(determinedCounter.remainingCount)
        } else {
            val counterToUse: Counter = counters[0]
            buttonIndex = counterToUse.connectedButtons[0]
            buttonPressCounts = listOf(0, 1)
        }

        return buttonPressCounts.flatMap { buttonPressCount ->
            val map: Map<Int, Int> = mapOf(
                buttonIndex to buttonPressCount,
                -(buttonIndex + 100) to if (hasMultipleOptions) buttonPressCount else 0
            )
            val nextCounters: List<Counter> = updateCountersFromButtonPress(counters, buttonIndex, buttonPressCount)
                .filterNot { it.connectedButtons.isEmpty() }

            val remainingSolutions: List<Map<Int, Int>> = getSolutionVectors(reduceCounters(nextCounters))

            remainingSolutions.map { it + map }
        }
    }

    private fun updateCountersFromButtonPress(counters: List<Counter>, buttonIndex: Int, buttonPressCount: Int): List<Counter> {
        return counters.map { counter ->
            Counter(
                counter.index,
                counter.remainingCount - (if (counter.connectedButtons.contains(buttonIndex)) buttonPressCount else 0),
                counter.connectedButtons - buttonIndex
            )
        }
    }

    private fun findBestCombinationOfSolution(solutionVectors: List<Solution>, reducedCounters: List<Counter>): Int {
        val baseSolution: SolutionVector = solutionVectors[0].first
        if (solutionVectors.size == 1) {
            return baseSolution.values.sum()
        }

        return findBest(baseSolution.mapValues { 0 }, solutionVectors.drop(1) + solutionVectors.first(), reducedCounters)
    }

    private fun findBest(
        baseSolution: SolutionVector,
        remainingVectors: List<Solution>,
        reducedCounters: List<Counter>
    ): Int {
        val vector: SolutionVector = remainingVectors.first().first
        val selectedButton: Int = remainingVectors.first().second

        var minSearch: Int
        var maxSearch: Int

        if (selectedButton >= 0) {
            minSearch = 0
            maxSearch = reducedCounters.filter { it.connectedButtons.contains(selectedButton) }.minOf { it.remainingCount }
        } else {
            minSearch = -5 + (vector.filter { it.value > 0 }
                .maxOfOrNull { - (baseSolution[it.key]!! / it.value) } ?: -1000)
            maxSearch = 5 + (vector.filter { it.value < 0 }
                .minOfOrNull { - (baseSolution[it.key]!! / it.value) } ?: 1000)

            if (vector.values.sum() > 0) {
                maxSearch = minSearch + 10
            } else {
                minSearch = maxSearch - 10
            }
        }

        return (minSearch..maxSearch).map {
            val newSolution: SolutionVector = addVectors(baseSolution, scaleVector(vector, it))

            if (remainingVectors.size == 1) {
                if (newSolution.all {v -> v.value >= 0 } && newSolution.values.sum() > 0) {
                    return@map newSolution.values.sum()
                } else {
                    return@map 100000
                }
            }
            findBest(newSolution, remainingVectors.drop(1), reducedCounters)
        }.min()
    }

    private fun addVectors(vector1: SolutionVector, vector2: SolutionVector): SolutionVector {
        return vector1.mapValues { entry -> entry.value + (vector2[entry.key] ?: 0) }
    }

    private fun scaleVector(vector: SolutionVector, scaleFactor: Int): SolutionVector {
        return vector.mapValues { it.value * scaleFactor }
    }
}

fun main() {
    val day = Day10()
    println(day.solvePartOneFromFile())
    println(day.solvePartTwoFromFile())
}
