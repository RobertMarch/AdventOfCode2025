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
            val resTotal = res[0].values.sum()
            println("$resTotal  ${res[0]}")
            return@sumOf resTotal
            val res2: List<Solution> = res
                .filter { solution -> solution.entries.count { it.key < 0 && it.value >= 1 } <= 1 }
                .map { solution ->
                    Pair(
                        solution.filterKeys { it >= 0 },
                        -((solution.entries.find { it.key < 0 && it.value >= 1 }?.key ?: 0) + 100)
                    )
                }

            val validResults: List<Boolean> = res2.map { r ->
                reducedCounters.all { counter ->
                    counter.connectedButtons.sumOf { r.first[it] ?: 0 } == counter.remainingCount
                }
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
                var counter1: Counter = updatedCounters[counterIndex1]
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
                        counter1 = newCounter1
                        hasReduction = true
                    }
                }
            }
            updatedCounters.removeIf { it.connectedButtons.isEmpty() && it.remainingCount == 0 }
            if (updatedCounters.any { it.connectedButtons.isEmpty() && it.remainingCount != 0 }) {
                // Early exit if any counters are not connected, but have a remaining count
                return updatedCounters
            }
        }

//        val additionalCounters: List<Counter> = findRelatedRows(updatedCounters)
//        if (additionalCounters.isNotEmpty()) {
//            return reduceCounters(counters + additionalCounters)
//        }

        return updatedCounters
    }

    private fun findRelatedRows(counters: List<Counter>): List<Counter> {
        val countersToCheck: List<Counter> = counters.filter { it.connectedButtons.size > 1 }

        val combinationOptions: MutableMap<Set<Counter>, Map<Int, Int>> = mutableMapOf(setOf<Counter>() to mapOf())

        countersToCheck.forEach { counter ->
            val newOptions: Map<Set<Counter>, Map<Int, Int>> = combinationOptions.map { option ->
                val newKey: Set<Counter> = option.key + counter
                val newValues: MutableMap<Int, Int> = option.value.toMutableMap()
                counter.connectedButtons.forEach { button ->
                    newValues.compute(button) { _, value -> (value ?: 0) + 1 }
                }
                Pair(newKey, newValues)
            }.toMap()
            combinationOptions.putAll(newOptions)
        }

        val possibleOptions = combinationOptions.filter { option ->
            option.value.isNotEmpty() && option.value.values.all { it % 2 == 0 }
        }

        val newCounters: List<Counter> = possibleOptions.entries.mapIndexed { index, option ->
            val totalCount: Int = option.key.sumOf { it.remainingCount }
            val repeatCount: Int = option.value.values.first()
            val connectedButtons: List<Int> = option.value.keys.toList()
            if (totalCount % repeatCount != 0) {
                println("Possible error in calculating related rows")
            }
            Counter(index + 100, totalCount / repeatCount, connectedButtons)
        }

        return newCounters
    }

    private fun getSolutionVectors(counters: List<Counter>): List<Map<Int, Int>> {
        if (counters.isEmpty()) {
            return listOf(mapOf())
        }

        val determinedCounter: Counter? = counters.find { it.connectedButtons.size == 1 }

        if (determinedCounter != null) {
            return getSolutionsForButtonPress(
                counters,
                determinedCounter.connectedButtons[0],
                determinedCounter.remainingCount
            )
        } else {
            val counterToUse: Counter = counters[0]
            val buttonIndex: Int = counterToUse.connectedButtons[0]
            val maxButtonPresses: Int = counters.filter { it.connectedButtons.contains(buttonIndex) }.minOf { it.remainingCount }
            val results: List<List<Map<Int, Int>>> = (0..maxButtonPresses).map { count ->
                getSolutionsForButtonPress(counters, buttonIndex, count)
            }.filterNot { it.isEmpty() }
            if (results.isEmpty()) {
                return results.flatten()
            }
            return listOf(results.flatten().minBy { it.values.sum() }) // .take(2)

            val base1: Map<Int, Int> = results[0][0]
            val base2: Map<Int, Int> = results[1][0]
            val vector: Map<Int, Int> = base1.keys.union(base2.keys).associateWith { key ->
                base2.getOrDefault(key, 0) - base1.getOrDefault(key, 0)
            }

            return results[0] + (results[1] - base2 + vector).map { it + Pair(-(buttonIndex + 100), 1) }

//            if (results[0].isNotEmpty() && results[1].isNotEmpty()) {
//                // If both solutions are valid, then button can be adjusted and return values
//                return results.flatMapIndexed { index, resMap ->
//                    if (index == 0) {
//                        resMap
//                    } else {
//                        resMap.map { it + Pair(-(buttonIndex + 100), 1) }
//                    }
//                }
//            }
//
//            // If both solutions are not valid, then button has exact required press count to be found
//            if (results[0].isNotEmpty()) {
//                return results[0]
//            }
//            if (results[1].isNotEmpty()) {
//                return results[1]
//            }
//
//            val r: List<Map<Int, Int>> = (2..counterToUse.remainingCount).flatMap { pressCount ->
//                getSolutionsForButtonPress(counters, buttonIndex, pressCount)
//            }.take(2)
//            if (r.size <= 1) {
//                return r
//            }
//            val base = r.first()
//            val vector = r[1].mapValues { entry -> entry.value - (base[entry.key] ?: 0) } + Pair(-(buttonIndex + 100), 2)
//            return listOf(base, vector)
        }
    }

    private fun getSolutionsForButtonPress(
        counters: List<Counter>,
        buttonIndex: Int,
        buttonPressCount: Int
    ): List<Map<Int, Int>> {
        if (buttonPressCount < 0) {
            val t = 1
        }
        val map: Map<Int, Int> = mapOf(
            buttonIndex to buttonPressCount,
        )
        val nextCounters: List<Counter> = updateCountersFromButtonPress(counters, buttonIndex, buttonPressCount)

        if (nextCounters.any { it.connectedButtons.isEmpty() && it.remainingCount != 0 }) {
            return listOf()
        }

        val reducedNextCounters: List<Counter> = reduceCounters(nextCounters.filterNot { it.connectedButtons.isEmpty() })

        if (reducedNextCounters.any { (it.connectedButtons.isEmpty() && it.remainingCount != 0) || it.remainingCount < 0 }) {
            return listOf()
        }

        val remainingSolutions: List<Map<Int, Int>> = getSolutionVectors(reducedNextCounters)

        return remainingSolutions.map { it + map }
    }

    private fun updateCountersFromButtonPress(
        counters: List<Counter>,
        buttonIndex: Int,
        buttonPressCount: Int
    ): List<Counter> {
        return counters.map { counter ->
            Counter(
                counter.index,
                counter.remainingCount - (if (counter.connectedButtons.contains(buttonIndex)) buttonPressCount else 0),
                counter.connectedButtons - buttonIndex
            )
        }
    }

    private fun findBestCombinationOfSolution(solutionVectors: List<Solution>, reducedCounters: List<Counter>): Int {
        val fixedSolutions: List<Solution> = solutionVectors.filter { it.second == -100 }

        if (solutionVectors.all { it.second == -100 }) {
            return fixedSolutions.minOf { solution ->
                if (solution.first.values.all { it >= 0 }) {
                    solution.first.values.sum()
                } else {
                    100000
                }
            }
        }

        val flexibleVectors: List<Solution> = solutionVectors - fixedSolutions

        return fixedSolutions.minOf { fixedSol ->
            findBest(fixedSol.first.mapValues { 0 }, flexibleVectors + fixedSol, reducedCounters)
        }
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
            maxSearch =
                reducedCounters.filter { it.connectedButtons.contains(selectedButton) }.minOf { it.remainingCount }
        } else {
            minSearch = -5 + (vector.filter { it.value > 0 }
                .maxOfOrNull { -(baseSolution[it.key]!! / it.value) } ?: -1000)
            maxSearch = 5 + (vector.filter { it.value < 0 }
                .minOfOrNull { -(baseSolution[it.key]!! / it.value) } ?: 1000)

            if (vector.values.sum() > 0) {
                maxSearch = minSearch + 10
            } else {
                minSearch = maxSearch - 10
            }
        }

        return (minSearch..maxSearch).map {
            val newSolution: SolutionVector = addVectors(baseSolution, scaleVector(vector, it))

            if (remainingVectors.size == 1) {
                if (newSolution.all { v -> v.value >= 0 } && newSolution.values.sum() > 0) {
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
