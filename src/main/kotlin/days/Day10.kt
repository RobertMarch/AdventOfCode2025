package days

import kotlin.math.min
import kotlin.math.pow

private typealias ButtonConnections = List<Int>

class Day10 : Day(10) {

    private data class Machine(
        val indicatorLights: Int,
        val buttonValues: List<Int>,
        val buttonIndexes: List<ButtonConnections>,
        val joltageRequirements: List<Int>
    )

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
            }.sortedByDescending { it.size }
            val buttonValues: List<Int> = buttons.map { button -> button.sumOf { 2.0.pow(it).toInt() } }

            Machine(indicatorLights, buttonValues, buttons, joltageRequirements)
        }
    }

    private fun trimStartAndEndChars(value: String): String {
        return value.substring(1, value.length - 1)
    }





//    override fun solvePartTwo(inputString: String): Any {
//        val machines: List<Machine> = parseInput(inputString)
//
//        return machines.sumOf { machine ->
//            val buttonCount: Int = machine.buttonIndexes.size
//            val counterCount: Int = machine.joltageRequirements.size
//            val freeVariables: Int = buttonCount - counterCount
//
//            val connections: MutableMap<Int, MutableList<Int>> = mutableMapOf()
//            machine.buttonIndexes.forEachIndexed { buttonIndex, button -> button.forEach { connections.compute(it) { _, value ->
//                val list: MutableList<Int> = (value ?: mutableListOf())
//                list.add(buttonIndex)
//                list
//            } }}
//
//            // Get counters sorted by connected button count, and then by remaining press count within that
//            val counters: List<Counter> = connections.entries.map { Counter(it.key, machine.joltageRequirements[it.key], it.value) }
//                .sortedBy { it.remainingCount }
//                .sortedBy { it.connectedButtons.size }
//
//
//
//            val machineState: MachineState = MachineState(counters, machine.buttonIndexes, listOf())
//            val res = iterate(machineState)
//            println(res)
//            res
//        }
//    }
//
//    private fun getBaseSolution(counters: List<Counter>, usedButtons: List<Int>): Int {
//        val nextCounter: Counter = counters.first { it.remainingCount > 0 }
//        val availableButtons: List<Int> = nextCounter.connectedButtons.filter { !usedButtons.contains(it) }
//
//        if (availableButtons.isEmpty()) {
//            return 100000
//        }
//
//        if (availableButtons.size == 1) {
//            val button: Int = availableButtons[0]
//            val buttonPressCount: Int = nextCounter.remainingCount
//
//            val nextState: MachineState = machineState.pressButton(button, buttonPressCount)
//            if (nextState.isCompletedState()) {
//                return buttonPressCount
//            }
//            if (!nextState.isValidState()) {
//                return 100000
//            }
//            return iterate(nextState) + buttonPressCount
//        } else {
//            for (button in availableButtons) {
//                for (pressCount in 0..nextCounter.remainingCount) {
//                    val nextState: MachineState = machineState.pressButton(button, pressCount)
//                    if (nextState.isCompletedState()) {
//                        bestResult = min(bestResult, pressCount)
//                        continue
//                    }
//                    if (!nextState.isValidState()) {
//                        if (nextState.anyCountersNegative()) {
//                            break
//                        }
//                        continue
//                    }
//                    bestResult = min(bestResult, iterate(nextState) + pressCount)
//                }
//            }
//        }
//
//        return bestResult
//    }



    private data class Counter(val index: Int, val remainingCount: Int, val connectedButtons: List<Int>)

    private class MachineState(val counters: List<Counter>, val buttons: List<ButtonConnections>, val usedButtons: List<Int>) {

        fun pressButton(buttonIndex: Int, pressCount: Int): MachineState {
            val button: ButtonConnections = buttons[buttonIndex]

            val newCounters: MutableList<Counter> = counters.map { counter ->
                val updatedRemaining: Int = counter.remainingCount - if (button.contains(counter.index)) pressCount else 0
                Counter(counter.index, updatedRemaining, counter.connectedButtons)
            }.toMutableList()

            val newUsedButtons: MutableSet<Int> = usedButtons.toMutableSet()
            newUsedButtons.add(buttonIndex)
            newCounters.filter { it.remainingCount == 0 }.forEach { newUsedButtons.addAll(it.connectedButtons) }

            newCounters.sortBy { it.remainingCount }
            newCounters.sortBy { it.connectedButtons.count { b -> !usedButtons.contains(b) } }

            return MachineState(newCounters, buttons, newUsedButtons.toList())
        }

        fun isCompletedState(): Boolean {
            return counters.all { it.remainingCount == 0 }
        }

        fun anyCountersNegative(): Boolean {
            return counters.any { it.remainingCount < 0 }
        }

        fun isValidState(): Boolean {
            if (anyCountersNegative()) {
                return false
            }
            val allCountersReachable = counters.all { counter -> counter.remainingCount == 0 || counter.connectedButtons.any { !usedButtons.contains(it) } }
            if (!allCountersReachable) {
                return false
            }
            val remainingTargets: MutableMap<List<Int>, Int> = mutableMapOf()
            counters.forEach { counter ->
                val remButtons: List<Int> = counter.connectedButtons.filterNot { usedButtons.contains(it) }
                if (remainingTargets.containsKey(remButtons) && remainingTargets[remButtons] != counter.remainingCount) {
                    return false
                }
            }
            return true
        }
    }

    override fun solvePartTwo(inputString: String): Any {
        val machines: List<Machine> = parseInput(inputString)

        val sizes: Set<Pair<Int, Int>> = machines.map { Pair(it.joltageRequirements.size, it.buttonIndexes.size) }.toSet()

        return machines.sumOf { machine ->
            val connections: MutableMap<Int, MutableList<Int>> = mutableMapOf()
            machine.buttonIndexes.forEachIndexed { buttonIndex, button -> button.forEach { connections.compute(it) { _, value ->
                val list: MutableList<Int> = (value ?: mutableListOf())
                list.add(buttonIndex)
                list
            } }}

            // Get counters sorted by connected button count, and then by remaining press count within that
            val counters: List<Counter> = connections.entries.map { Counter(it.key, machine.joltageRequirements[it.key], it.value) }
                .sortedBy { it.remainingCount }
                .sortedBy { it.connectedButtons.size }

            val machineState: MachineState = MachineState(counters, machine.buttonIndexes, listOf())
            val res = iterate(machineState)
            println(res)
            res
        }

    }

    private fun iterate(machineState: MachineState): Int {
        val nextCounter: Counter = machineState.counters.first { it.remainingCount > 0 }
        val availableButtons: List<Int> = nextCounter.connectedButtons.filter { !machineState.usedButtons.contains(it) }
        var bestResult: Int = 100000

        if (availableButtons.isEmpty()) {
            return bestResult
        }

        if (availableButtons.size == 1) {
            val button: Int = availableButtons[0]
            val buttonPressCount: Int = nextCounter.remainingCount

            val nextState: MachineState = machineState.pressButton(button, buttonPressCount)
            if (nextState.isCompletedState()) {
                return buttonPressCount
            }
            if (!nextState.isValidState()) {
                return 100000
            }
            return iterate(nextState) + buttonPressCount
        } else {
            for (button in availableButtons) {
                for (pressCount in 0..nextCounter.remainingCount) {
                    val nextState: MachineState = machineState.pressButton(button, pressCount)
                    if (nextState.isCompletedState()) {
                        bestResult = min(bestResult, pressCount)
                        continue
                    }
                    if (!nextState.isValidState()) {
                        if (nextState.anyCountersNegative()) {
                            break
                        }
                        continue
                    }
                    bestResult = min(bestResult, iterate(nextState) + pressCount)
                }
            }
        }

        return bestResult
    }

//        return machines.sumOf { machine ->
//            val connections: MutableMap<Int, MutableList<Int>> = mutableMapOf()
//            machine.buttonIndexes.forEachIndexed { buttonIndex, button -> button.forEach { connections.compute(it) { _, value ->
//                val list: MutableList<Int> = (value ?: mutableListOf())
//                list.add(buttonIndex)
//                list
//            } }}
//
//            // Get counters sorted by connected button count, and then by remaining press count within that
//            val counters: List<Counter> = connections.entries.map { Counter(it.key, machine.joltageRequirements[it.key], it.value) }
//                .sortedBy { it.remainingCount }
//                .sortedBy { it.connectedButtons.size }
//
//            iterate(counters, listOf())
//        }

    private fun tryButtonPress(machine: Machine, buttonIndex: Int, remainingCounts: List<Int>, previousBestCountRemaining: Int): Int {
        if (remainingCounts.all { it == 0 }) {
            // If all counts are accurate, no further presses needed
            return 0
        }
        if (buttonIndex >= machine.buttonIndexes.size) {
            // If all buttons have been pressed, return failed value
            return 100000
        }
        if (remainingCounts.max() > previousBestCountRemaining) {
            // If the remaining presses to beat the previous best is less than the remaining counts, we cannot beat it and so return
            return 100000
        }
        val remainingButtonCounters: List<Int> = machine.buttonIndexes.subList(buttonIndex, machine.buttonIndexes.size).flatten()
        if (remainingCounts.mapIndexed { index, count -> count > 0 && !remainingButtonCounters.contains(index) }.any{ it }) {
            return 100000
        }

        var bestPressCount: Int = 100000
        val button: List<Int> = machine.buttonIndexes[buttonIndex]

        for (buttonPressAmount in remainingCounts.max() downTo 0) {
            val nextRemainingCounts: List<Int> = remainingCounts.mapIndexed { index, count -> count - if (button.contains(index)) buttonPressAmount else 0 }
            if (nextRemainingCounts.any { it < 0 }) {
                // If any counts are below 0, then continue
                continue
            }
            val pressCount: Int = tryButtonPress(machine, buttonIndex + 1, nextRemainingCounts, previousBestCountRemaining - buttonPressAmount)
            bestPressCount = min(bestPressCount, pressCount + buttonPressAmount)
        }
        return bestPressCount
    }

//        var currentOptions: List<List<Int>> = listOf(List(machine.joltageRequirements.size) { 0 })
//        var buttonCount: Int = 0
//
//        while (currentOptions.isNotEmpty()) {
//            buttonCount++
//
//            val nextOptions: MutableList<List<Int>> = mutableListOf()
//            currentOptions.forEach { option ->
//                machine.buttonIndexes.forEach { button ->
//                    val nextValue: List<Int> =
//                        option.mapIndexed { index, i -> i + if (button.contains(index)) 1 else 0 }
//
//                    if (nextValue == machine.joltageRequirements) {
//                        return@sumOf buttonCount
//                    }
//                    if (!allButtonOptions.containsKey(nextValue)
//                        && !nextOptions.contains(nextValue)
//                        && nextValue.mapIndexed { index, v -> v <= machine.joltageRequirements[index] }.all { it }
//                    ) {
//                        allButtonOptions[nextValue] = buttonCount
//                        nextOptions.add(nextValue)
//                    }
//                }
//            }
//            currentOptions = nextOptions //.filter { option -> !nextOptions.any { other -> other.mapIndexed { index, v -> v > option[index] }.all { it } } }
//        }
//
//        return 1000000
}

fun main() {
    val day = Day10()
    println(day.solvePartOneFromFile())
    println(day.solvePartTwoFromFile())
}
