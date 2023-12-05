package day5

import util.Solution

sealed interface AlmanacLine

data class SeedsLine(val seeds: List<Long>) : AlmanacLine
enum class MapType : AlmanacLine {
    SEED_TO_SOIL,
    SOIL_TO_FERTILIZER,
    FERTILIZER_TO_WATER,
    WATER_TO_LIGHT,
    LIGHT_TO_TEMPERATURE,
    TEMPERATURE_TO_HUMIDITY,
    HUMIDITY_TO_LOCATION,
}

data class MapLine(val destinationStart: Long, val sourceStart: Long, val range: Long) : AlmanacLine
data object EMPTY : AlmanacLine

class IfYouGiveASeedAFertilizer(fileName: String?) : Solution<AlmanacLine, Long>(fileName) {
    override fun parse(line: String): AlmanacLine? {
        val numbersRegex = """(\d+ ?)+""".toRegex()
        return when {
            line.startsWith("seeds: ") -> {
                val seeds = line.drop("seeds: ".length).split(" ").map { it.toLong() }
                SeedsLine(seeds)
            }

            line.startsWith("seed-to-soil") -> MapType.SEED_TO_SOIL
            line.startsWith("soil-to-fertilizer") -> MapType.SOIL_TO_FERTILIZER
            line.startsWith("fertilizer-to-water") -> MapType.FERTILIZER_TO_WATER
            line.startsWith("water-to-light") -> MapType.WATER_TO_LIGHT
            line.startsWith("light-to-temperature") -> MapType.LIGHT_TO_TEMPERATURE
            line.startsWith("temperature-to-humidity") -> MapType.TEMPERATURE_TO_HUMIDITY
            line.startsWith("humidity-to-location") -> MapType.HUMIDITY_TO_LOCATION
            numbersRegex.matches(line) -> {
                val entry = line.split(" ").map { it.toLong() }
                MapLine(entry[0], entry[1], entry[2])
            }

            else -> EMPTY
        }
    }

    override fun solve1(data: List<AlmanacLine>): Long {
        val maps = buildMaps(emptyMap(), data.drop(2), null)
        return (data.first() as SeedsLine).seeds.minOf {
            locationForSeeds(it, maps)
        }
    }

    override fun solve2(data: List<AlmanacLine>): Long {
        val seeds = expandSeeds(data.first() as SeedsLine)
        val maps = buildMaps(emptyMap(), data.drop(2), null)
        return seeds.minOf {
            locationForSeeds(it, maps)
        }
    }

    private fun expandSeeds(seedsLine: SeedsLine): List<Long> {
        tailrec fun rec(remaining: List<Long>, acc: List<Long>): List<Long> {
            return if (remaining.isEmpty()) {
                acc
            } else {
                val start = remaining.first()
                val size = remaining.drop(1).first()

                rec(remaining.drop(2), acc + (start..<start + size).toList())
            }
        }

        return rec(seedsLine.seeds, emptyList())
    }

    private tailrec fun buildMaps(
        acc: Map<MapType, List<MapLine>>,
        remaining: List<AlmanacLine>,
        current: Pair<MapType, List<MapLine>>?,
    ): Map<MapType, List<MapLine>> {
        return if (remaining.isEmpty()) {
            acc + current!!
        } else {
            when (val nextLine = remaining.first()) {
                is MapLine -> buildMaps(acc, remaining.drop(1), Pair(current!!.first, current.second + nextLine))
                EMPTY -> buildMaps(acc + current!!, remaining.drop(1), current)
                is MapType -> buildMaps(acc, remaining.drop(1), Pair(nextLine, emptyList()))
                is SeedsLine -> error("Unexpected SeedsLine")
            }
        }
    }

    private fun locationForSeeds(seed: Long, maps: Map<MapType, List<MapLine>>): Long {
        val soil = maps[MapType.SEED_TO_SOIL]!!.lookup(seed)
        val fertilizer = maps[MapType.SOIL_TO_FERTILIZER]!!.lookup(soil)
        val water = maps[MapType.FERTILIZER_TO_WATER]!!.lookup(fertilizer)
        val light = maps[MapType.WATER_TO_LIGHT]!!.lookup(water)
        val temperature = maps[MapType.LIGHT_TO_TEMPERATURE]!!.lookup(light)
        val humidity = maps[MapType.TEMPERATURE_TO_HUMIDITY]!!.lookup(temperature)
        return maps[MapType.HUMIDITY_TO_LOCATION]!!.lookup(humidity)
    }
}

fun List<MapLine>.lookup(entry: Long): Long {
    fun lookupEntry(line: MapLine): Long? {
        val delta = line.destinationStart - line.sourceStart
        return if (entry >= line.sourceStart && entry < (line.sourceStart + line.range)) entry + delta else null
    }

    return this.firstNotNullOfOrNull { lookupEntry(it) } ?: entry
}
