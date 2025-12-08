package days

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestFactory

class Day08Test {

    private var daySolver: Day08 = Day08()

    @TestFactory
    fun testPartOneExamples() = listOf(
        Pair("162,817,812\n" +
                "57,618,57\n" +
                "906,360,560\n" +
                "592,479,940\n" +
                "352,342,300\n" +
                "466,668,158\n" +
                "542,29,236\n" +
                "431,825,988\n" +
                "739,650,466\n" +
                "52,470,668\n" +
                "216,146,977\n" +
                "819,987,18\n" +
                "117,168,530\n" +
                "805,96,715\n" +
                "346,949,466\n" +
                "970,615,88\n" +
                "941,993,340\n" +
                "862,61,35\n" +
                "984,92,344\n" +
                "425,690,689", 10) to 40L,
    ).map { (input, expected) ->
        DynamicTest.dynamicTest("when input is $input then answer should be $expected") {
            val result = daySolver.solvePartOneForNConnections(input.first, input.second)

            assertEquals(expected, result)
        }
    }

    @Test
    fun solvePartOne() {
        val result = daySolver.solvePartOneFromFile()

        assertEquals(63920L, result)
    }

    @TestFactory
    fun testPartTwoExamples() = listOf(
        "162,817,812\n" +
                "57,618,57\n" +
                "906,360,560\n" +
                "592,479,940\n" +
                "352,342,300\n" +
                "466,668,158\n" +
                "542,29,236\n" +
                "431,825,988\n" +
                "739,650,466\n" +
                "52,470,668\n" +
                "216,146,977\n" +
                "819,987,18\n" +
                "117,168,530\n" +
                "805,96,715\n" +
                "346,949,466\n" +
                "970,615,88\n" +
                "941,993,340\n" +
                "862,61,35\n" +
                "984,92,344\n" +
                "425,690,689" to 25272L,
    ).map { (input, expected) ->
        DynamicTest.dynamicTest("when input is $input then answer should be $expected") {
            val result = daySolver.solvePartTwo(input)

            assertEquals(expected, result)
        }
    }

    @Test
    fun solvePartTwo() {
        val result = daySolver.solvePartTwoFromFile()

        assertEquals(1026594680L, result)
    }
}