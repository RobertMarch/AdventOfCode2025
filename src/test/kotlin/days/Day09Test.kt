package days

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestFactory

class Day09Test {

    private var daySolver: Day09 = Day09()

    @TestFactory
    fun testPartOneExamples() = listOf(
        "7,1\n" +
                "11,1\n" +
                "11,7\n" +
                "9,7\n" +
                "9,5\n" +
                "2,5\n" +
                "2,3\n" +
                "7,3" to 50L,
    ).map { (input, expected) ->
        DynamicTest.dynamicTest("when input is $input then answer should be $expected") {
            val result = daySolver.solvePartOne(input)

            assertEquals(expected, result)
        }
    }

    @Test
    fun solvePartOne() {
        val result = daySolver.solvePartOneFromFile()

        assertEquals(4782268188L, result)
    }

    @TestFactory
    fun testPartTwoExamples() = listOf(
        "7,1\n" +
                "11,1\n" +
                "11,7\n" +
                "9,7\n" +
                "9,5\n" +
                "2,5\n" +
                "2,3\n" +
                "7,3" to 24L,
    ).map { (input, expected) ->
        DynamicTest.dynamicTest("when input is $input then answer should be $expected") {
            val result = daySolver.solvePartTwo(input)

            assertEquals(expected, result)
        }
    }

    @Test
    fun solvePartTwo() {
        val result = daySolver.solvePartTwoFromFile()

        assertEquals(1574717268L, result)
    }
}