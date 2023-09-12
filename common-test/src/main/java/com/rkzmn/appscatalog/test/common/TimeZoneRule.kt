package com.rkzmn.appscatalog.test.common

import org.junit.rules.TestWatcher
import org.junit.runner.Description
import java.util.TimeZone

class TimeZoneRule(val timeZoneId: String) : TestWatcher() {

    val defaultTimeZone: TimeZone = TimeZone.getDefault()
    val testTimeZone: TimeZone = TimeZone.getTimeZone(timeZoneId)
    override fun starting(description: Description) {
        TimeZone.setDefault(testTimeZone)
    }

    override fun finished(description: Description) {
        TimeZone.setDefault(defaultTimeZone)
    }
}
