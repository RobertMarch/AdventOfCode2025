package days

import point.Point2D

private typealias ShapePoints = Set<Point2D>

private typealias Shape = List<ShapePoints>

private fun ShapePoints.rotateShape(): ShapePoints {
    return this.map { Point2D(it.y, -it.x + 2) }.toSet()
}

private fun ShapePoints.flipShape(): ShapePoints {
    return this.map { Point2D(it.x, 2 - it.y) }.toSet()
}

private fun Shape.pointCount(): Int {
    return this[0].size
}

class Day12 : Day(12) {

    private data class CurrentState(
        val remainingPoints: List<Point2D>,
        val remainingQuantities: Map<Int, Int>
    )

    private class Region(val points: List<Point2D>, val quantities: Map<Int, Int>, val maxX: Long, val maxY: Long)

    override fun solvePartOne(inputString: String): Int {
        val (shapes: List<Shape>, regions: List<Region>) = parseInput(inputString)

        return regions.count { region ->
            val spaceNeeded: Int = region.quantities.map { it.value * shapes[it.key].pointCount() }.sum()
            val spaceAvailable: Int = region.points.size

            if (spaceNeeded > spaceAvailable) {
                return@count false
            }

            val horizontalBoxes: Long = region.maxX / 3
            val verticalBoxes: Long = region.maxY / 3
            val totalBoxes = horizontalBoxes * verticalBoxes

            val totalBoxesNeeded: Int = region.quantities.values.sum()

            if (totalBoxesNeeded <= totalBoxes) {
                return@count true
            }

            val maximumEmptyCells: Int = spaceAvailable - spaceNeeded
            val res = hasSolution(CurrentState(region.points, region.quantities), region, shapes, mutableSetOf(), maximumEmptyCells)
            println("${region.maxX}, ${region.maxY}, $res")
            res
        }
    }

    private fun hasSolution(currentState: CurrentState, region: Region, shapes: List<Shape>, cache: MutableSet<CurrentState>, maximumEmptyCells: Int): Boolean {
        if (currentState.remainingQuantities.isEmpty() || currentState.remainingQuantities.values.all { it == 0 }) {
            return true
        }

        if (maximumEmptyCells < 0 || currentState.remainingPoints.isEmpty() || cache.contains(currentState)) {
            return false
        }

        val nextOrigin: Point2D = currentState.remainingPoints.first()

        if (nextOrigin.x >= region.maxX - 1) {
            val nextState = CurrentState(currentState.remainingPoints - nextOrigin, currentState.remainingQuantities)
            if (hasSolution(nextState, region, shapes, cache, maximumEmptyCells - 1)) {
                return true
            }
            cache.add(nextState)
        }
        if (nextOrigin.y >= region.maxY - 1) {
            // If on bottom two rows and not all shapes used, then this cannot be completed
            return false
        }

        for (remainingShape in currentState.remainingQuantities) {
            val shape = shapes[remainingShape.key]

            for (shapeOrientation in shape) {
                val placedShapePoints: Set<Point2D> = shapeOrientation.map { it + nextOrigin }.toSet()
                if (placedShapePoints.any { !currentState.remainingPoints.contains(it) }) {
                    continue
                }
                val nextState = CurrentState(
                    currentState.remainingPoints - placedShapePoints,
                    currentState.remainingQuantities
                        .mapValues { if (it.key == remainingShape.key) it.value - 1 else it.value }
                        .filterNot { it.value == 0 }
                )
                if (hasSolution(nextState, region, shapes, cache, maximumEmptyCells)) {
                    return true
                }
                cache.add(nextState)
            }
        }

        // Check state if adding no shape into the position
        val nextState = CurrentState(currentState.remainingPoints - nextOrigin, currentState.remainingQuantities)
        if (hasSolution(nextState, region, shapes, cache, maximumEmptyCells - 1)) {
            return true
        }
        cache.add(nextState)

        return false
    }

    private fun parseInput(inputString: String): Pair<List<Shape>, List<Region>> {
        val chunks: List<String> = inputString.replace("\r", "").split("\n\n")

        val shapes: List<Shape> = chunks.subList(0, chunks.size - 1).map { chunk ->
            val points: ShapePoints = chunk.lines().drop(1).flatMapIndexed { yIndex, line ->
                line.mapIndexed { xIndex, char ->
                    if (char == '#') {
                        Point2D(xIndex.toLong(), yIndex.toLong())
                    } else {
                        null
                    }
                }
            }.filterNotNull().toSet()

            getAllShapeRotations(points)
        }

        val regions: List<Region> = chunks.last().lines().map { line ->
            val lineParts: List<Int> = line.split("x", ": ", " ").map { it.toInt() }
            val points: List<Point2D> = (1..lineParts[0])
                .flatMap { y ->
                    (1..lineParts[1]).map { x -> Point2D(x.toLong(), y.toLong()) }
                }
            val quantities: Map<Int, Int> = lineParts.drop(2).mapIndexed { index, count -> Pair(index, count) }.filter { it.second > 0 }.toMap()
            Region(points, quantities, lineParts[0].toLong(), lineParts[1].toLong())
        }

        return Pair(shapes, regions)
    }

    private fun getAllShapeRotations(shape: ShapePoints): Shape {
        val allOptions: MutableSet<ShapePoints> = mutableSetOf()
        var latestShape: ShapePoints = shape
        for (i in 0..3) {
            allOptions.add(latestShape)
            allOptions.add(latestShape.flipShape())
            latestShape = latestShape.rotateShape()
        }
        return allOptions.toList()
    }

    override fun solvePartTwo(inputString: String): Any {
        return -1
    }
}

fun main() {
    val day = Day12()
    println(day.solvePartOneFromFile())
    println(day.solvePartTwoFromFile())
}
