package com.rkzmn.appscatalog.utils

fun <T : Comparable<T>> List<T>.isSorted(): Boolean {
    for (i in 0 until this.size - 1) {
        if (this[i] > this[i + 1]) {
            return false
        }
    }
    return true
}

inline fun <T, C : Comparable<C>> List<T>.isSorted(
    crossinline component: T.() -> C
): Boolean {
    for (i in 0 until this.size - 1) {
        val currentComponent = this[i].component()
        val nextComponent = this[i + 1].component()
        if (currentComponent > nextComponent) {
            return false
        }
    }
    return true
}

fun <T : Comparable<T>> List<T>.isSortedDescending(): Boolean {
    for (i in 0 until this.size - 1) {
        if (this[i] < this[i + 1]) {
            return false
        }
    }
    return true
}

inline fun <T, C : Comparable<C>> List<T>.isSortedDescending(
    crossinline component: T.() -> C
): Boolean {
    for (i in 0 until this.size - 1) {
        val currentComponent = this[i].component()
        val nextComponent = this[i + 1].component()
        if (currentComponent < nextComponent) {
            return false
        }
    }
    return true
}
