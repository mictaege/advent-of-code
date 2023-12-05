package io.github.mictaege.aoc.y2023.d05.p2

import io.github.mictaege.aoc.y2023.d05.example
import io.github.mictaege.aoc.y2023.d05.input
import kotlin.math.min

class SeedRange(val start: Long, val length: Long) {
    val values: LongRange = start ..< start + length
    var lowestLocation: Long = Long.MAX_VALUE
        set(value) {
            field = min(field, value )
        }
}

class MappingRange(val original: String) {
    val rangeLength = original.split(Regex("\\s+"))[2].toLong()
    val srcStart = original.split(Regex("\\s+"))[1].toLong()
    val srcEnd = srcStart + rangeLength
    val destStart = original.split(Regex("\\s+"))[0].toLong()
    val destEnd = srcStart + rangeLength
    val diff = srcStart - destStart

    fun matchingDest(start: Long) = start - diff
}

class Mapping(val original: String) {
    val source: String = original.split(":")[0].split(Regex("\\s+"))[0].split("-")[0]
    val destination: String = original.split(":")[0].split(Regex("\\s+"))[0].split("-")[2]
    val ranges: List<MappingRange> = original.split(":")[1].trim().lines().map { l -> MappingRange(l) }

    fun mapSrcToDest(src: Long): Long {
        return ranges.find { src in it.srcStart .. it.srcEnd  }?.matchingDest(src) ?: src
    }
}

class Conversion(val original: String) {
    val seedRanges: List<SeedRange>
    val lowestLocation: Long
        get() = seedRanges.minOf { it.lowestLocation }
    val mappings: Map<String, Mapping>

    init {
        val sections = original.split(Regex("[\r\n][\r\n]"))

        seedRanges = mutableListOf()
        val seedSplit: List<Long> = sections[0].split(":")[1].trim().split(Regex("\\s+")).map { it.toLong() }
        seedSplit.forEachIndexed { i, s ->
            if (i % 2 != 0) {
                val start: Long = seedSplit[i - 1]
                seedRanges.add(SeedRange(start, s))
            }
        }

        mappings = mutableMapOf()
        for (i in 1..< sections.size) {
            val mapping = Mapping(sections[i])
            mappings.put(mapping.source, mapping)
        }

        seedRanges.forEach {
            it.values.forEach { v ->
                var srcType = "seed"
                var src: Long = v
                while (mappings.containsKey(srcType)) {
                    val mapping = mappings[srcType]
                    src = mapping!!.mapSrcToDest(src)
                    srcType = mapping.destination
                    if (srcType == "location") {
                        it.lowestLocation = src
                        srcType = "x"
                    }
                }
            }
        }
    }

}

fun main() {
    println(Conversion(input).lowestLocation)

}