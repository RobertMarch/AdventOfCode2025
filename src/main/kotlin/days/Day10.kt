package days

import kotlin.math.pow

class Day10 : Day(10) {

    private data class Machine(
        val indicatorLights: Int,
        val buttonValues: List<Int>,
        val buttonIndexes: List<List<Int>>,
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

            val counters: List<Counter> = connections.entries.map { Counter(it.key, machine.joltageRequirements[it.key], it.value) }
            val reducedCounters: List<Counter> = reduceCounters(counters)

            getSolutionVectors(reducedCounters)
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
                        val newCounter1 = Counter(
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

        return updatedCounters
    }

    private fun getSolutionVectors(counters: List<Counter>): Int {
        if (counters.isEmpty()) {
            return 0
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
            val results: List<Int> = (0..maxButtonPresses).map { count ->
                getSolutionsForButtonPress(counters, buttonIndex, count)
            }
            if (results.isEmpty()) {
                return 100000
            }
            return results.min()
        }
    }

    private fun getSolutionsForButtonPress(
        counters: List<Counter>,
        buttonIndex: Int,
        buttonPressCount: Int
    ): Int {
        val nextCounters: List<Counter> = updateCountersFromButtonPress(counters, buttonIndex, buttonPressCount)

        if (nextCounters.any { it.connectedButtons.isEmpty() && it.remainingCount != 0 }) {
            return 100000
        }

        val reducedNextCounters: List<Counter> = reduceCounters(nextCounters.filterNot { it.connectedButtons.isEmpty() })

        if (reducedNextCounters.any { (it.connectedButtons.isEmpty() && it.remainingCount != 0) || it.remainingCount < 0 }) {
            return 100000
        }

        val bestRemainingSolution: Int = getSolutionVectors(reducedNextCounters)

        return bestRemainingSolution + buttonPressCount
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
}

fun main() {
    val day = Day10()
    println(day.solvePartOneFromFile())
    println(day.solvePartTwoFromFile())
}
