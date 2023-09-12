package com.rkzmn.appscatalog.utils.kotlin

import com.google.common.truth.Truth.assertThat
import com.rkzmn.appscatalog.test.common.TimeZoneRule
import org.junit.Rule
import org.junit.Test
import java.util.Date
import java.util.Locale

class DateUtilsTest {

    @get:Rule
    val timeZoneRule = TimeZoneRule("Asia/Bangkok")

    @Test
    fun testDateFormatting() {
        val date = Date(1694511356000) // September 15, 2022 10:13:09 AM UTC

        val formattedDate = date.format(DateTimeFormat.display_date_full_time, Locale.US)
        assertThat(formattedDate).isEqualTo("12 September 2023, 04:35:56 PM")

        // Test with a different locale
        val formattedDateFrench = date.format(DateTimeFormat.display_date_full_time, Locale.FRENCH)
        assertThat(formattedDateFrench).isEqualTo("12 septembre 2023, 04:35:56 PM")
    }

    @Test
    fun testStringToDateConversion() {
        val dateString = "12 September 2023, 04:35:56 PM"
        val testTimestamp = 1694511356000

        val date = dateString.asDate(DateTimeFormat.display_date_full_time, Locale.US)
        assertThat(testTimestamp).isEqualTo(date?.time)

        // Test with a different locale
        val dateFrench = "12 septembre 2023, 04:35:56 PM"
        val dateInFrenchLocale = dateFrench.asDate(DateTimeFormat.display_date_full_time, Locale.FRENCH)
        assertThat(testTimestamp).isEqualTo(dateInFrenchLocale?.time)

        // Test with an empty string, should return null
        val emptyStringDate = "".asDate(DateTimeFormat.display_date_full_time, Locale.US)
        assertThat(emptyStringDate).isNull()

        // Test with an invalid date string, should return null
        val invalidDateString = "Invalid Date"
        val invalidDate = invalidDateString.asDate(DateTimeFormat.display_date_full_time, Locale.US)
        assertThat(invalidDate).isNull()
    }

    @Test
    fun testLongToDateConversion() {
        val timestamp = 1694511356000

        val formattedDate = timestamp.asFormattedDate(DateTimeFormat.display_date_full_time, Locale.US)
        assertThat("12 September 2023, 04:35:56 PM").isEqualTo(formattedDate)

        // Test with a different locale
        val formattedDateFrench = timestamp.asFormattedDate(DateTimeFormat.display_date_full_time, Locale.FRENCH)
        assertThat("12 septembre 2023, 04:35:56 PM").isEqualTo(formattedDateFrench)

        // Test with a negative timestamp, should return null
        val negativeTimestamp = (-1631703989000L).asFormattedDate(DateTimeFormat.display_date_full_time, Locale.US)
        assertThat(negativeTimestamp).isNull()

        // Test with a zero timestamp, should return null
        val zeroTimestamp = 0L.asFormattedDate(DateTimeFormat.display_date_full_time, Locale.US)
        assertThat(zeroTimestamp).isNull()
    }
}
