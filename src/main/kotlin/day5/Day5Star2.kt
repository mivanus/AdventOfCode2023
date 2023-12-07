package day5

/**
 * --- Part Two ---
 *
 * Everyone will starve if you only plant such a small number of seeds. Re-reading the almanac, it looks like the seeds: line actually describes ranges of seed numbers.
 *
 * The values on the initial seeds: line come in pairs. Within each pair, the first value is the start of the range and the second value is the length of the range. So, in the first line of the example above:
 *
 * seeds: 79 14 55 13
 * This line describes two ranges of seed numbers to be planted in the garden. The first range starts with seed number 79 and contains 14 values: 79, 80, ..., 91, 92. The second range starts with seed number 55 and contains 13 values: 55, 56, ..., 66, 67.
 *
 * Now, rather than considering four seed numbers, you need to consider a total of 27 seed numbers.
 *
 * In the above example, the lowest location number can be obtained from seed number 82, which corresponds to soil 84, fertilizer 84, water 84, light 77, temperature 45, humidity 46, and location 46. So, the lowest location number is 46.
 *
 * Consider all of the initial seed numbers listed in the ranges on the first line of the almanac. What is the lowest location number that corresponds to any of the initial seed numbers?
 *
 * @see Day5Star1
 */
class Day5Star2 {

    private val numberRegex = """(\d+)""".toRegex()
    private val numberRangeRegex = """(\d+) (\d+)""".toRegex()

    fun solve() {
        val inputFile = this.javaClass.getResource("/day5/input.txt")?.readText()

        val seeds = mutableListOf<LongRange>()
        val seedToSoil = mutableListOf<Instruction>()
        val soilToFertilizer = mutableListOf<Instruction>()
        val fertilizerToWater = mutableListOf<Instruction>()
        val waterToLight = mutableListOf<Instruction>()
        val lightToTemperature = mutableListOf<Instruction>()
        val temperatureToHumidity = mutableListOf<Instruction>()
        val humidityToLocation = mutableListOf<Instruction>()

        var currentAlmanacList = seedToSoil;
        inputFile!!.lines().forEach { almanacLine ->

            if (almanacLine.startsWith("seeds:")) {
                numberRangeRegex.findAll(almanacLine.substringAfter(':')).forEach {
                    val rangeStart = it.groups[1]!!.value.toLong()
                    seeds.add(LongRange(rangeStart, rangeStart + it.groups[2]!!.value.toLong()))
                }
            } else if (almanacLine.startsWith("seed-to-soil map:")) {
                currentAlmanacList = seedToSoil;
            } else if (almanacLine.startsWith("soil-to-fertilizer map:")) {
                currentAlmanacList = soilToFertilizer;
            } else if (almanacLine.startsWith("fertilizer-to-water map:")) {
                currentAlmanacList = fertilizerToWater;
            } else if (almanacLine.startsWith("water-to-light map:")) {
                currentAlmanacList = waterToLight;
            } else if (almanacLine.startsWith("light-to-temperature map:")) {
                currentAlmanacList = lightToTemperature;
            } else if (almanacLine.startsWith("temperature-to-humidity map:")) {
                currentAlmanacList = temperatureToHumidity;
            } else if (almanacLine.startsWith("humidity-to-location map:")) {
                currentAlmanacList = humidityToLocation;
            } else if (almanacLine.isNotBlank()) {
                currentAlmanacList.add(mapToInstruction(almanacLine))
            }
        }

        seeds.sortBy { it.first }

        val lowestLocation = seeds.sortedBy { it.first }.flatMap {
            mapToDestinationRanges(it, seedToSoil)
        }.distinct().flatMap {
            mapToDestinationRanges(it, soilToFertilizer)
        }.distinct().flatMap {
            mapToDestinationRanges(it, fertilizerToWater)
        }.distinct().flatMap {
            mapToDestinationRanges(it, waterToLight)
        }.distinct().flatMap {
            mapToDestinationRanges(it, lightToTemperature)
        }.distinct().flatMap {
            mapToDestinationRanges(it, temperatureToHumidity)
        }.distinct().flatMap {
            mapToDestinationRanges(it, humidityToLocation)
        }.distinct().minBy { it.first }

        println("[Day5Star2] Lowest location: ${lowestLocation.first}")
    }

    private fun mapToDestinationRanges(src: LongRange, instruction: List<Instruction>): Set<LongRange> {
        val soilRange = mutableSetOf<LongRange>()

        val instructionSorted = instruction.sortedBy { it.sourceRangeStart }

        // assume instructions have no gaps
        val instructionRange = instructionSorted[0].sourceRangeStart..instructionSorted.last().sourceRangeEnd

        // is before instruction range
        if (src.last < instructionRange.first) {
            soilRange.add(src)
            return soilRange
        }

        // is after instruction range
        if (src.first > instructionRange.last) {
            soilRange.add(src)
            return soilRange
        }

        // add portion before instruction range
        if (src.first < instructionRange.first) {
            soilRange.add(LongRange(src.first, instructionRange.first - 1))
        }

        // add portion after instruction range
        if (src.last > instructionRange.last) {
            soilRange.add(LongRange(instructionRange.last + 1, src.last))
        }

        // find portions inside instruction range
        instructionSorted.forEach {
            if (src.first >= it.sourceRangeStart && src.first <= it.sourceRangeEnd) {
                // start is inside this instruction range

                if (src.last <= it.sourceRangeEnd) {
                    // end is inside instruction range
                    soilRange.add(it.convertToDestination(src.first)..it.convertToDestination(src.last))
                } else {
                    // end is not in this instruction range
                    soilRange.add(it.convertToDestination(src.first)..it.convertToDestination(it.sourceRangeEnd))
                }
            } else if (src.last >= it.sourceRangeStart && src.last <= it.sourceRangeEnd) {
                // end is inside this instruction range

                if (src.first >= it.sourceRangeStart) {
                    // start is inside this instruction range
                    soilRange.add(it.convertToDestination(src.first)..it.convertToDestination(src.last))
                } else {
                    // start is not in this instruction range
                    soilRange.add(it.convertToDestination(it.sourceRangeStart)..it.convertToDestination(src.last))
                }
            }
        }

        return soilRange
    }

    private fun mapToInstruction(almanacLine: String): Instruction {
        val numbers = numberRegex.findAll(almanacLine).map { it.value.toLong() }.toList()
        return Instruction(numbers[0], numbers[1], numbers[0] + numbers[2] - 1, numbers[1] + numbers[2] - 1)
    }

    data class Instruction(
        val destinationRangeStart: Long,
        val sourceRangeStart: Long,
        val destinationRangeEnd: Long,
        val sourceRangeEnd: Long
    ) {

        fun toSourceRange(): LongRange = sourceRangeStart..sourceRangeEnd

        fun toDestinationRange(): LongRange = destinationRangeStart..destinationRangeEnd

        fun convertToDestination(src: Long): Long =
            if (src in toSourceRange()) destinationRangeStart + (src - sourceRangeStart) else src

    }

}