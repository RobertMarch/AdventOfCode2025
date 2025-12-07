package days

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestFactory

class Day07Test {

    private var daySolver: Day07 = Day07()

    @TestFactory
    fun testPartOneExamples() = listOf(
        ".......S.......\n" +
                "...............\n" +
                ".......^.......\n" +
                "...............\n" +
                "......^.^......\n" +
                "...............\n" +
                ".....^.^.^.....\n" +
                "...............\n" +
                "....^.^...^....\n" +
                "...............\n" +
                "...^.^...^.^...\n" +
                "...............\n" +
                "..^...^.....^..\n" +
                "...............\n" +
                ".^.^.^.^.^...^.\n" +
                "..............." to 21L,
    ).map { (input, expected) ->
        DynamicTest.dynamicTest("when input is $input then answer should be $expected") {
            val result = daySolver.solvePartOne(input)

            assertEquals(expected, result)
        }
    }

    @Test
    fun solvePartOne() {
        val result = daySolver.solvePartOneFromFile()

        assertEquals(1717L, result)
    }

    @TestFactory
    fun testPartTwoExamples() = listOf(
        ".......S.......\n" +
                "...............\n" +
                ".......^.......\n" +
                "...............\n" +
                "......^.^......\n" +
                "...............\n" +
                ".....^.^.^.....\n" +
                "...............\n" +
                "....^.^...^....\n" +
                "...............\n" +
                "...^.^...^.^...\n" +
                "...............\n" +
                "..^...^.....^..\n" +
                "...............\n" +
                ".^.^.^.^.^...^.\n" +
                "..............." to 40L,
    ).map { (input, expected) ->
        DynamicTest.dynamicTest("when input is $input then answer should be $expected") {
            val result = daySolver.solvePartTwo(input)

            assertEquals(expected, result)
        }
    }

    @Test
    fun solvePartTwo() {
        val result = daySolver.solvePartTwoFromFile()

        assertEquals(231507396180012L, result)
    }
}