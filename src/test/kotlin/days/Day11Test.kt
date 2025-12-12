package days

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestFactory

class Day11Test {

    private var daySolver: Day11 = Day11()

    @TestFactory
    fun testPartOneExamples() = listOf(
        "aaa: you hhh\n" +
                "you: bbb ccc\n" +
                "bbb: ddd eee\n" +
                "ccc: ddd eee fff\n" +
                "ddd: ggg\n" +
                "eee: out\n" +
                "fff: out\n" +
                "ggg: out\n" +
                "hhh: ccc fff iii\n" +
                "iii: out" to 5L,
    ).map { (input, expected) ->
        DynamicTest.dynamicTest("when input is $input then answer should be $expected") {
            val result = daySolver.solvePartOne(input)

            assertEquals(expected, result)
        }
    }

    @Test
    fun solvePartOne() {
        val result = daySolver.solvePartOneFromFile()

        assertEquals(466L, result)
    }

    @TestFactory
    fun testPartTwoExamples() = listOf(
        "svr: aaa bbb\n" +
                "aaa: fft\n" +
                "fft: ccc\n" +
                "bbb: tty\n" +
                "tty: ccc\n" +
                "ccc: ddd eee\n" +
                "ddd: hub\n" +
                "hub: fff\n" +
                "eee: dac\n" +
                "dac: fff\n" +
                "fff: ggg hhh\n" +
                "ggg: out\n" +
                "hhh: out" to 2L,
    ).map { (input, expected) ->
        DynamicTest.dynamicTest("when input is $input then answer should be $expected") {
            val result = daySolver.solvePartTwo(input)

            assertEquals(expected, result)
        }
    }

    @Test
    fun solvePartTwo() {
        val result = daySolver.solvePartTwoFromFile()

        assertEquals(549705036748518L, result)
    }
}