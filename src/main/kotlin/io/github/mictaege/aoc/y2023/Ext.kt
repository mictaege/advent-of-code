package io.github.mictaege.aoc.y2023

fun <T> List<T>.cartesianProduct(): List<Pair<T, T>> {
    return this.indices.flatMap { i ->
        (i + 1 until this.size).map { j -> Pair(this[i], this[j]) }
    }
}