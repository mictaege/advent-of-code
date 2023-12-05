package io.github.mictaege.aoc.y2023.d05.p1

import io.github.mictaege.aoc.y2023.d05.input

class Seed(val value: Long) {
    var location = 0L
    val visited = mutableListOf<String>()
}

class MappingRange(val original: String) {
    val rangeLength = original.split(Regex("\\s+"))[2].toLong()
    val srcStart = original.split(Regex("\\s+"))[1].toLong()
    val destStart = original.split(Regex("\\s+"))[0].toLong()
    val srcToDestMap: Map<Long, Long>

    init {
        srcToDestMap = mutableMapOf()
        for (i in 0.. rangeLength) {
            srcToDestMap.put(srcStart + i, destStart + i)
        }
    }

}

class Mapping(val original: String) {
    val source: String = original.split(":")[0].split(Regex("\\s+"))[0].split("-")[0]
    val destination: String = original.split(":")[0].split(Regex("\\s+"))[0].split("-")[2]
    val ranges: List<MappingRange> = original.split(":")[1].trim().lines().map { l -> MappingRange(l) }


    fun mapSrcToDest(src: Long): Long {
        return ranges.find { it.srcToDestMap.containsKey(src) }?.srcToDestMap?.get(src) ?: src
    }

}

class Conversion(val original: String) {
    val seeds: List<Seed>
    val lowestLocation: Long
        get() = seeds.minOf { it.location }
    val mappings: Map<String, Mapping>

    init {
        val sections = original.split(Regex("[\r\n][\r\n]"))
        seeds = sections[0].split(":")[1].trim().split(Regex("\\s+")).map { Seed(it.toLong()) }

        mappings = mutableMapOf()
        for (i in 1..< sections.size) {
            val mapping = Mapping(sections[i])
            mappings.put(mapping.source, mapping)
        }
        
        seeds.forEach {
            var srcType = "seed"
            var src = it.value
            while (!it.visited.contains(srcType) && mappings.containsKey(srcType)) {
                it.visited.add(srcType)
                val mapping = mappings[srcType]
                src = mapping!!.mapSrcToDest(src)
                srcType = mapping.destination
                if (srcType == "location") {
                   it.location = src
                }
            }   
        }
    }
}

fun main() {
    println(Conversion(input).lowestLocation)
    //Conversion(example).seeds.forEach {
    //    println("${it.value} -> ${it.location}")
    //}
    //println(Conversion(example).lowestLocation)
}