package com.rkzmn.appscatalog.navigation.destination

import com.google.common.truth.Truth.assertThat
import org.junit.Test

class DestinationTest {
    @Test
    fun buildRouteStringTest() {
        val output = Destination.buildRoute(
            label = "sample_route",
            arguments = arrayOf("arg1", "arg2", "arg3"),
            optionalArguments = arrayOf("opt_arg1", "opt_arg2", "opt_arg3"),
        )
        val expected =
            "sample_route/{arg1}/{arg2}/{arg3}?opt_arg1={opt_arg1}&opt_arg2={opt_arg2}&opt_arg3={opt_arg3}"
        assertThat(output).isEqualTo(expected)
    }
}
