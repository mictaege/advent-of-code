package io.github.mictaege.aoc.y2023

fun <T> List<T>.cartesianProduct(): List<Pair<T, T>> {
    return this.indices.flatMap { i ->
        (i + 1 until this.size).map { j -> Pair(this[i], this[j]) }
    }
}

fun <T> List<T>.splitBy(element: T): List<List<T>> = this.splitBy{ it == element}

fun <T> List<T>.splitBy(predicate: (T) -> Boolean): List<List<T>> {
    val result = mutableListOf<List<T>>()
    val currentGroup = mutableListOf<T>()

    for (element in this) {
        if (predicate.invoke(element)) {
            if (currentGroup.isNotEmpty()) {
                result.add(currentGroup.toList())
                currentGroup.clear()
            }
        } else {
            currentGroup.add(element)
        }
    }

    if (currentGroup.isNotEmpty()) {
        result.add(currentGroup.toList())
    }
    return result
}
