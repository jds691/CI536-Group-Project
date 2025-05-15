package com.example.pantryplan.core.designsystem.text

import android.text.format.DateUtils
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlin.math.absoluteValue

fun Instant.asRelativeFormattedDate(): String {
    val now = Clock.System.now()

    val minResolution = if ((this - now).inWholeDays.absoluteValue < 7) {
        DateUtils.DAY_IN_MILLIS
    } else {
        DateUtils.WEEK_IN_MILLIS
    }

    return DateUtils.getRelativeTimeSpanString(
        this.toEpochMilliseconds(),
        now.toEpochMilliseconds(),
        minResolution,
    ).toString().lowercase()
}