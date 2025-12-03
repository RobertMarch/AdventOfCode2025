package days

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestFactory

class Day03Test {

    private var daySolver: Day03 = Day03()

    @TestFactory
    fun testPartOneExamples() = listOf(
        "987654321111111\n" +
                "811111111111119\n" +
                "234234234234278\n" +
                "818181911112111" to 357L,
    ).map { (input, expected) ->
        DynamicTest.dynamicTest("when input is $input then answer should be $expected") {
            val result = daySolver.solvePartOne(input)

            assertEquals(expected, result)
        }
    }

    @Test
    fun solvePartOne() {
        val result = daySolver.solvePartOneFromFile()

        assertEquals(17100L, result)
    }

    @TestFactory
    fun testPartTwoExamples() = listOf(
        "987654321111111\n" +
                "811111111111119\n" +
                "234234234234278\n" +
                "818181911112111" to 3121910778619L,
    ).map { (input, expected) ->
        DynamicTest.dynamicTest("when input is $input then answer should be $expected") {
            val result = daySolver.solvePartTwo(input)

            assertEquals(expected, result)
        }
    }

    @Test
    fun solvePartTwo() {
        val result = daySolver.solvePartTwoFromFile()

        assertEquals(170418192256861L, result)
    }
}