package com.rkzmn.appscatalog.test.common

import junit.framework.TestCase.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class CollectionUtilsTest {
    @Test
    fun testAscendingOrder() {
        val ascendingList = listOf(1, 2, 3, 4, 5)
        val ascendingListNullable = listOf(1, 2, null, 4, 5)

        assertTrue(ascendingList.isSorted())
        assertTrue(ascendingListNullable.isSorted())
        assertFalse(ascendingListNullable.isSorted(skipNulls = false))
    }

    @Test
    fun testDescendingOrder() {
        val descendingList = listOf(5, 4, 3, 2, 1)
        val descendingListNullable = listOf(5, 4, null, 2, 1)

        assertTrue(descendingList.isSorted(ascending = false))
        assertTrue(descendingListNullable.isSorted(ascending = false))
        assertFalse(descendingListNullable.isSorted(ascending = false, skipNulls = false))
    }

    @Test
    fun testUnsortedList() {
        val unsortedList = listOf(3, 1, 2, 4, 5)
        val unsortedListNullable = listOf(3, 1, null, 4, 5)

        assertFalse(unsortedList.isSorted())
        assertFalse(unsortedListNullable.isSorted())
        assertFalse(unsortedListNullable.isSorted(skipNulls = false))
    }

    @Test
    fun testCustomComparator() {
        data class Person(val name: String, val age: Int)

        val persons = listOf(
            Person("Alice", 25),
            Person("Bob", 30),
            Person("Carol", 20)
        )

        assertTrue(persons.isSortedBy(component = Person::name))
        assertFalse(persons.isSortedBy(component = Person::age))
    }

    @Test
    fun testEmptyList() {
        val emptyList = emptyList<Int>()

        assertTrue(emptyList.isSorted())
    }

    @Test
    fun testSingleElementList() {
        val singleElementList = listOf(42)

        assertTrue(singleElementList.isSorted())
    }
}
