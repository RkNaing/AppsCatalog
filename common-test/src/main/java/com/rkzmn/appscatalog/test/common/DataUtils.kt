package com.rkzmn.appscatalog.test.common

import kotlin.random.Random

/**
 * Generates a random timestamp in milliseconds within a specified range.
 *
 * @param minTimestamp The minimum timestamp (inclusive) in milliseconds since the Unix epoch
 *                     from which the random timestamp will be generated. Default is
 *                     Saturday, January 1, 2000, 00:00:00 UTC.
 * @param maxTimestamp The maximum timestamp (exclusive) in milliseconds since the Unix epoch
 *                     up to which the random timestamp will be generated. Default is the
 *                     current timestamp (the current time).
 *
 * @return A random timestamp within the specified range.
 */
fun generateRandomTimestamp(
    minTimestamp: Long = 946659600000L, // Sat Jan 01 2000 00:00:00 UTC
    maxTimestamp: Long = System.currentTimeMillis() // Now
): Long = Random.nextLong(from = minTimestamp, until = maxTimestamp)
