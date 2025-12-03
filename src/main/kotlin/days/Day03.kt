package days

class Day03 : Day(3) {

    override fun solvePartOne(inputString: String): Any {
        val batteryBanks: List<List<Int>> = inputString.lines().map { line -> line.map { it.digitToInt() } }

        var total: Long = 0

        batteryBanks.forEach { bank ->
            val maxFirstBattery: Int = bank.subList(0, bank.size - 1).max()
            val firstBatteryIndex: Int = bank.indexOf(maxFirstBattery)

            val maxSecondBattery: Int = bank.subList(firstBatteryIndex + 1, bank.size).max()

            total += maxFirstBattery * 10 + maxSecondBattery
        }

        return total
    }

    override fun solvePartTwo(inputString: String): Any {
        val batteryBanks: List<List<Int>> = inputString.lines().map { line -> line.map { it.digitToInt() } }

        var total: Long = 0

        batteryBanks.forEach { bank ->
            var previousBatteryIndex: Int = -1
            var maxBankValue: Long = 0
            for (remainingBatteries in 11 downTo 0) {
                val (batteryIndex, batteryValue) = getNextBestBattery(bank, previousBatteryIndex, remainingBatteries)
                previousBatteryIndex = batteryIndex
                maxBankValue = maxBankValue * 10 + batteryValue
            }
            total += maxBankValue
        }

        return total
    }

    private fun getNextBestBattery(bank: List<Int>, previousBatteryIndex: Int, remainingBatteries: Int): Pair<Int, Int> {
        val bankSubList: List<Int> = bank.subList(previousBatteryIndex + 1, bank.size - remainingBatteries)
        val maxNextBattery: Int = bankSubList.max()
        val nextBatteryIndex: Int = bankSubList.indexOf(maxNextBattery) + previousBatteryIndex + 1
        return Pair(nextBatteryIndex, maxNextBattery)
    }
}

fun main() {
    val day = Day03()
    println(day.solvePartOneFromFile())
    println(day.solvePartTwoFromFile())
}
