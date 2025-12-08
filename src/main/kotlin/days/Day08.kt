package days

import point.Point3D

private typealias Connection = Pair<Point3D, Point3D>

class Day08 : Day(8) {

    override fun solvePartOne(inputString: String): Long {
        return solvePartOneForNConnections(inputString, 1000)
    }

    fun solvePartOneForNConnections(inputString: String, connectionCount: Int): Long {
        val (_, connections: List<Connection>) = parseInput(inputString)

        val connectionGroups: MutableList<MutableSet<Point3D>> = mutableListOf()
        connections.subList(0, connectionCount).forEach { addConnection(connectionGroups, it) }

        connectionGroups.sortByDescending { it.size }

        return connectionGroups.subList(0, 3).map { it.size.toLong() }.reduce { acc, l -> acc * l }
    }

    override fun solvePartTwo(inputString: String): Long {
        val (boxCount: Int, connections: List<Connection>) = parseInput(inputString)

        val connectionGroups: MutableList<MutableSet<Point3D>> = mutableListOf()
        var connectionIndex: Int = 0
        while (connectionGroups.size != 1 || connectionGroups[0].size != boxCount) {
            addConnection(connectionGroups, connections[connectionIndex])
            connectionIndex++
        }

        val lastConnection: Connection = connections[connectionIndex - 1]
        return lastConnection.first.x * lastConnection.second.x
    }

    private fun parseInput(inputString: String): Pair<Int, List<Connection>> {
        val boxes: List<Point3D> = inputString.lines().map {
            val coords: List<Long> = it.split(",").map { coord -> coord.toLong() }
            Point3D(coords[0], coords[1], coords[2])
        }

        val connections: List<Connection> = boxes.flatMapIndexed { index1, box1 ->
            boxes.subList(index1 + 1, boxes.size).map { box2 ->
                Pair((box1 - box2).lengthSquared(), Pair(box1, box2))
            }
        }.sortedBy { it.first }.map { it.second }

        return Pair(boxes.size, connections)
    }

    private fun addConnection(connectionGroups: MutableList<MutableSet<Point3D>>, con: Connection) {
        val existingFirstGroup: MutableSet<Point3D>? = connectionGroups.find { group -> group.contains(con.first) }
        val existingSecondGroup: MutableSet<Point3D>? = connectionGroups.find { group -> group.contains(con.second) }
        if (existingFirstGroup != null && existingSecondGroup != null) {
            if (existingFirstGroup != existingSecondGroup) {
                existingFirstGroup.addAll(existingSecondGroup)
                connectionGroups.remove(existingSecondGroup)
            }
        } else if (existingFirstGroup != null) {
            existingFirstGroup.add(con.second)
        } else if (existingSecondGroup != null) {
            existingSecondGroup.add(con.first)
        } else {
            connectionGroups.add(mutableSetOf(con.first, con.second))
        }
    }
}

fun main() {
    val day = Day08()
    println(day.solvePartOneFromFile())
    println(day.solvePartTwoFromFile())
}
