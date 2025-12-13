package days

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestFactory

class Day12Test {

    private var daySolver: Day12 = Day12()

    @TestFactory
    fun testPartOneExamples() = listOf(
        "0:\n" +
                "###\n" +
                "##.\n" +
                "##.\n" +
                "\n" +
                "1:\n" +
                "###\n" +
                "##.\n" +
                ".##\n" +
                "\n" +
                "2:\n" +
                ".##\n" +
                "###\n" +
                "##.\n" +
                "\n" +
                "3:\n" +
                "##.\n" +
                "###\n" +
                "##.\n" +
                "\n" +
                "4:\n" +
                "###\n" +
                "#..\n" +
                "###\n" +
                "\n" +
                "5:\n" +
                "###\n" +
                ".#.\n" +
                "###\n" +
                "\n" +
                "4x4: 0 0 0 0 2 0\n" +
                "12x5: 1 0 1 0 2 2\n" +
                "12x5: 1 0 1 0 3 2" to 2,
    ).map { (input, expected) ->
        DynamicTest.dynamicTest("when input is $input then answer should be $expected") {
            val result = daySolver.solvePartOne(input)

            assertEquals(expected, result)
        }
    }

    @Test
    fun solvePartOne() {
        val result = daySolver.solvePartOneFromFile()

        assertEquals(593, result)
    }

    @TestFactory
    fun testPartTwoExamples() = listOf(
        "input" to 1,
    ).map { (input, expected) ->
        DynamicTest.dynamicTest("when input is $input then answer should be $expected") {
            val result = daySolver.solvePartTwo(input)

            assertEquals(expected, result)
        }
    }

    @Test
    fun solvePartTwo() {
        val result = daySolver.solvePartTwoFromFile()

        assertEquals(1, result)
    }
}