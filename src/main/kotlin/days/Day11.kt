package days

class Day11 : Day(11) {

    override fun solvePartOne(inputString: String): Any {
        val connections: Map<String, List<String>> = inputString.lines().map { line ->
            val parts = line.split(": ", " ")
            Pair(parts[0], parts.subList(1, parts.size))
        }.toMap()

        return findPathCount("you", "out", connections)
    }

    private fun findPathCount(node: String, targetNode: String, connections: Map<String, List<String>>): Long {
        if (node == targetNode) {
            return 1L
        }
        if (!connections.containsKey(node)) {
            return 0L
        }
        return connections[node]!!.sumOf { findPathCount(it, targetNode, connections) }
    }

    override fun solvePartTwo(inputString: String): Long {
        val forwardConnections: Map<String, List<String>> = inputString.lines().map { line ->
            val parts = line.split(": ", " ")
            Pair(parts[0], parts.subList(1, parts.size))
        }.toMap()
        val backwardConnections: Map<String, List<String>> = getBackwardsConnections(forwardConnections)

        val fftForwardReachableNodes: Set<String> = getAllReachableNodes("fft", forwardConnections)
        val dacForwardReachableNodes: Set<String> = getAllReachableNodes("dac", forwardConnections)

        if (dacForwardReachableNodes.contains("fft")) {
            val svrToDac: Long = findPathCount("svr", "dac", forwardConnections)
            val dacToFft: Long = findPathCount("dac", "fft", forwardConnections)
            val fftToOut: Long = findPathCount("fft", "out", forwardConnections)
            return svrToDac * dacToFft * fftToOut
        }
        if (fftForwardReachableNodes.contains("dac")) {
            val dacBackwardReachableNodes: Set<String> = getAllReachableNodes("dac", backwardConnections)
            val fftBackwardReachableNodes: Set<String> = getAllReachableNodes("fft", backwardConnections)

            val svrToFftConnections: Map<String, List<String>> = forwardConnections.filterKeys { fftBackwardReachableNodes.contains(it) }

            val fftToDacNodes: Set<String> = fftForwardReachableNodes.intersect(dacBackwardReachableNodes)
            val fftToDacConnections: Map<String, List<String>> = forwardConnections.filterKeys { fftToDacNodes.contains(it) }

            val dacToOutConnections: Map<String, List<String>> = forwardConnections.filterKeys { dacForwardReachableNodes.contains(it) }

            val svrToFft: Long = findPathCount("svr", "fft", svrToFftConnections)
            val fftToDac: Long = findPathCount("fft", "dac", fftToDacConnections)
            val dacToOut: Long = findPathCount("dac", "out", dacToOutConnections)
            return svrToFft * fftToDac * dacToOut
        }

        return -1L
    }

    private fun getBackwardsConnections(connections: Map<String, List<String>>): Map<String, List<String>> {
        val backwardsConnections: MutableMap<String, MutableList<String>> = mutableMapOf()
        connections.forEach { conn ->
            conn.value.forEach { output ->
                backwardsConnections.compute(output) { _, value ->
                    val list: MutableList<String> = (value ?: mutableListOf())
                    list.add(conn.key)
                    list
                }
            }
        }
        return backwardsConnections
    }

    private fun getAllReachableNodes(start: String, connections: Map<String, List<String>>): Set<String> {
        val queue: MutableList<String> = mutableListOf(start)
        val visitedPoints: MutableSet<String> = mutableSetOf()
        while (queue.isNotEmpty()) {
            val next: String = queue.removeFirst()
            if (visitedPoints.contains(next)) {
                continue
            }
            visitedPoints.add(next)
            queue.addAll(connections[next] ?: listOf())
        }
        return visitedPoints
    }
}

fun main() {
    val day = Day11()
    println(day.solvePartOneFromFile())
    println(day.solvePartTwoFromFile())
}
