package com.rkzmn.appscatalog.test.common

/**
 * Checks if the elements in the list are sorted in ascending order based on their natural order.
 *
 * The function returns `true` if the list is sorted in ascending order according to the natural
 * ordering of elements (i.e., elements implement [Comparable]). It returns `false` otherwise.
 * If `skipNulls` is `true`, `null` values are ignored when checking for sorted order.
 *
 * @param ascending Whether to check for ascending order (default: `true`).
 * @param skipNulls Whether to skip `null` values when checking for sorted order (default: `true`).
 * @return `true` if the list is sorted in ascending order, `false` otherwise.
 */
fun <T : Comparable<T>> List<T?>.isSorted(
    ascending: Boolean = true,
    skipNulls: Boolean = true,
): Boolean = isSortedBy(
    ascending = ascending,
    skipNulls = skipNulls,
    component = { this }
)

/**
 * Checks if the elements in the list are sorted according to a custom comparator.
 *
 * The function returns `true` if the list is sorted according to the provided comparator.
 * Elements can be sorted in either ascending or descending order based on the `ascending` parameter.
 * If `skipNulls` is `true`, `null` values are ignored when checking for sorted order.
 *
 * @param ascending Whether to check for ascending order (default: `true`).
 * @param skipNulls Whether to skip `null` values when checking for sorted order (default: `true`).
 * @return `true` if the list is sorted based on the specified conditions, `false` otherwise.
 */
fun <T, C : Comparable<C>> List<T?>.isSortedBy(
    ascending: Boolean = true,
    skipNulls: Boolean = true,
    component: T.() -> C?
): Boolean {
    val order = if (ascending) 1 else -1
    for (i in 0 until lastIndex) {
        val current = this[i]?.component()
        val next = this[i + 1]?.component()

        when {
            current == null || next == null -> {
                if (!skipNulls) {
                    println("Comparison broke due to $current & $next ")
                    return false
                }
            }
            current.compareTo(next) * order > 0 -> {
                println("First unsorted pair => $current & $next ")
                return false
            }
        }
    }
    return true
}
