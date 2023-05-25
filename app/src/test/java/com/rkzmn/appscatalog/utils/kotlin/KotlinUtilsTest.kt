package com.rkzmn.appscatalog.utils.kotlin

import junit.framework.Assert.assertEquals
import org.junit.Test

class KotlinUtilsTest {

    @Test
    fun byteCountToDisplaySizeTest() {
        val inputToExpectedMap = mapOf(
            0L to "0 B",
            27L to "27 B",
            999L to "999 B",
            1000L to "1.0 kB",
            1023L to "1.0 kB",
            1024L to "1.0 kB",
            1728L to "1.7 kB",
            110592L to "110.6 kB",
            7077888L to "7.1 MB",
            452984832L to "453.0 MB",
            28991029248L to "29.0 GB",
            1855425871872L to "1.9 TB",
            9223372036854775807L to "9.2 EB",
        )

        inputToExpectedMap.forEach { (input, expected) ->
            val result = byteCountToDisplaySize(input)
            assertEquals(
                "byteCountToDisplaySize($input) | Expected : $expected | Returned: $result",
                result,
                expected
            )
        }
    }
}
