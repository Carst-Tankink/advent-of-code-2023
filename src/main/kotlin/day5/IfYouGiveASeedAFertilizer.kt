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

data class MapLine(val sourceRange: Range, val offset: Long) : AlmanacLine
data object EMPTY : AlmanacLine
typealias Range = Pair<Long, Long>

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
                MapLine(entry[1] to entry[1] + entry[2], entry[0] - entry[1])
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
        val seeds = expandSeedsToRanges(data.first() as SeedsLine)

        val maps = buildMaps(emptyMap(), data.drop(2), null)
        val locations = seeds.flatMap { seed ->
            maps[MapType.SEED_TO_SOIL]!!.lookupRange(seed)
                .flatMap { maps[MapType.SOIL_TO_FERTILIZER]!!.lookupRange(it) }
                .flatMap { maps[MapType.FERTILIZER_TO_WATER]!!.lookupRange(it) }
                .flatMap { maps[MapType.WATER_TO_LIGHT]!!.lookupRange(it) }
                .flatMap { maps[MapType.LIGHT_TO_TEMPERATURE]!!.lookupRange(it) }
                .flatMap { maps[MapType.TEMPERATURE_TO_HUMIDITY]!!.lookupRange(it) }
                .flatMap { maps[MapType.HUMIDITY_TO_LOCATION]!!.lookupRange(it) }
        }.sortedBy { it.first }
        return seeds.minOf { seed ->
            val ranges = maps[MapType.SEED_TO_SOIL]!!.lookupRange(seed)
                .flatMap { maps[MapType.SOIL_TO_FERTILIZER]!!.lookupRange(it) }
                .flatMap { maps[MapType.FERTILIZER_TO_WATER]!!.lookupRange(it) }
                .flatMap { maps[MapType.WATER_TO_LIGHT]!!.lookupRange(it) }
                .flatMap { maps[MapType.LIGHT_TO_TEMPERATURE]!!.lookupRange(it) }
                .flatMap { maps[MapType.TEMPERATURE_TO_HUMIDITY]!!.lookupRange(it) }
                .flatMap { maps[MapType.HUMIDITY_TO_LOCATION]!!.lookupRange(it) }
            ranges.minOf { it.first }
        }
    }

    private fun expandSeedsToRanges(seedsLine: SeedsLine): List<Range> {
        return seedsLine.seeds.windowed(2, 2).map { Pair(it[0], it[0] + it[1]) }
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
                is MapLine -> buildMaps(
                    acc,
                    remaining.drop(1),
                    Pair(current!!.first, (current.second + nextLine).sortedBy { it.sourceRange.first }),
                )

                EMPTY -> buildMaps(acc + current!!, remaining.drop(1), current)
                is MapType -> buildMaps(acc, remaining.drop(1), Pair(nextLine, emptyList()))
                is SeedsLine -> error("Unexpected SeedsLine")
            }
        }
    }

    private fun locationForSeeds(seed: Long, maps: Map<MapType, List<MapLine>>): Long {
        return maps[MapType.SEED_TO_SOIL]!!.lookupRange(Pair(seed, seed))
            .flatMap { maps[MapType.SOIL_TO_FERTILIZER]!!.lookupRange(it) }
            .flatMap { maps[MapType.FERTILIZER_TO_WATER]!!.lookupRange(it) }
            .flatMap { maps[MapType.WATER_TO_LIGHT]!!.lookupRange(it) }
            .flatMap { maps[MapType.LIGHT_TO_TEMPERATURE]!!.lookupRange(it) }
            .flatMap { maps[MapType.TEMPERATURE_TO_HUMIDITY]!!.lookupRange(it) }
            .flatMap { maps[MapType.HUMIDITY_TO_LOCATION]!!.lookupRange(it) }
            .first().first
    }
}

fun List<MapLine>.lookup(entry: Long): Long {
    fun lookupEntry(line: MapLine): Long? {
        val delta = line.offset
        return if (entry >= line.sourceRange.first && entry < line.sourceRange.second) entry + delta else null
    }

    return this.firstNotNullOfOrNull { lookupEntry(it) } ?: entry
}

fun List<MapLine>.lookupRange(entry: Range): List<Range> {
    tailrec fun rec(todo: List<MapLine>, acc: List<Range>, e: Range?): List<Range> {
        return if (todo.isEmpty() || e == null) {
            acc + listOfNotNull(e)
        } else {
            val firstLine = todo.first()
            val (f, m, l) = mapSingleLine(e, firstLine)
            rec(todo.drop(1), acc + listOfNotNull(f, m), l)
        }
    }

    return rec(this, emptyList(), entry)
}

fun mapSingleLine(entry: Range, line: MapLine): Triple<Range?, Range?, Range?> {
    val source = line.sourceRange
    return when {
        entry.second <= source.first -> Triple(entry, null, null)
        entry.first >= source.second -> Triple(null, null, entry)
        entry.first >= source.first && entry.second <= source.second -> Triple(
            null,
            entry + line.offset,
            null,
        )

        entry.first >= source.first && entry.second > source.second -> Triple(
            null,
            Pair(entry.first, source.second) + line.offset,
            Pair(source.second, entry.second),
        )

        entry.second > source.first && entry.second <= source.second -> {
            Triple(
                entry.first to source.first,
                (source.first to entry.second) + line.offset,
                null,
            )
        }

        else -> TODO()
    }
}

operator fun Pair<Long, Long>.plus(offset: Long): Pair<Long, Long> = Pair(this.first + offset, this.second + offset)
