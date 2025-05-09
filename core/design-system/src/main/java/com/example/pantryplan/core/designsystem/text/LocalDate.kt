package com.example.pantryplan.core.designsystem.text

import kotlinx.datetime.LocalDate
import kotlinx.datetime.format
import kotlinx.datetime.format.MonthNames
import kotlinx.datetime.format.char

fun LocalDate.pantryPlanExactFormat(): String {
    // Example output: May 07, 2025
    val format = LocalDate.Format {
        monthName(MonthNames.ENGLISH_ABBREVIATED); char(' '); dayOfMonth(); chars(
        ", "
    ); year()
    }

    return this.format(format)
}