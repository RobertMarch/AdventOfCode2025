package days

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestFactory

class Day10Test {

    private var daySolver: Day10 = Day10()

    @TestFactory
    fun testPartOneExamples() = listOf(
        "[.##.] (3) (1,3) (2) (2,3) (0,2) (0,1) {3,5,4,7}\n" +
                "[...#.] (0,2,3,4) (2,3) (0,4) (0,1,2) (1,2,3,4) {7,5,12,7,2}\n" +
                "[.###.#] (0,1,2,3,4) (0,3,4) (0,1,2,4,5) (1,2) {10,11,11,5,10,5}" to 7,
    ).map { (input, expected) ->
        DynamicTest.dynamicTest("when input is $input then answer should be $expected") {
            val result = daySolver.solvePartOne(input)

            assertEquals(expected, result)
        }
    }

    @Test
    fun solvePartOne() {
        val result = daySolver.solvePartOneFromFile()

        assertEquals(498, result)
    }

    @TestFactory
    fun testPartTwoExamples() = listOf(
        "[.##.] (3) (1,3) (2) (2,3) (0,2) (0,1) {3,5,4,7}\n" +
                "[...#.] (0,2,3,4) (2,3) (0,4) (0,1,2) (1,2,3,4) {7,5,12,7,2}\n" +
                "[.###.#] (0,1,2,3,4) (0,3,4) (0,1,2,4,5) (1,2) {10,11,11,5,10,5}" to 33,
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