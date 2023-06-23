package com.bpavuk.kyt.types.music.song

import kotlinx.serialization.Serializable

@JvmInline
@Serializable
public value class Duration(public val duration: String) {
    public val durationInSeconds: Int get() = duration.parse()

    private fun String.parse(): Int {
        val timeAsList = split(":").map { it.toInt() }.reversed()
        var timeAsInt = 0
        for (i in timeAsList.indices) {
            timeAsInt += when (i) {
                0 -> timeAsList[i]
                1 -> timeAsList[i] * 60
                2 -> timeAsList[i] * 3600
                else -> throw IllegalArgumentException("Time is invalid or too long to parse")
            }
        }
        return timeAsInt
    }

    public companion object {
        public fun isDuration(string: String): Boolean =
            string.matches(Regex("([0-9]?[0-9]):([0-5]?[0-9])(:([0-5]?[0-9]))?"))

    }
}
