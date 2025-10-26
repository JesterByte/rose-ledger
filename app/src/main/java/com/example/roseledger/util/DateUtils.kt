package com.example.roseledger.util

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

enum class DateFilter {
    DAY,
    WEEK,
    MONTH,
    YEAR,
    ALL // A filter for showing all expenses
}

// A helper object to calculate date ranges
object DateUtils {
    fun getFormattedDateRange(filter: DateFilter): String {
        val calendar = Calendar.getInstance()
        val monthFormat = SimpleDateFormat("MMMM yyyy", Locale.getDefault())
        val yearFormat = SimpleDateFormat("yyyy", Locale.getDefault())
        val dayFormat = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())

        return when (filter) {
            DateFilter.DAY -> dayFormat.format(calendar.time)
            DateFilter.MONTH -> monthFormat.format(calendar.time)
            DateFilter.YEAR -> yearFormat.format(calendar.time)
            DateFilter.WEEK -> {
                val startOfWeek = calendar.clone() as Calendar
                startOfWeek.set(Calendar.DAY_OF_WEEK, startOfWeek.firstDayOfWeek)
                val endOfWeek = startOfWeek.clone() as Calendar
                endOfWeek.add(Calendar.DAY_OF_WEEK, 6)
                "${dayFormat.format(startOfWeek.time)} - ${dayFormat.format(endOfWeek.time)}"
            }
            DateFilter.ALL ->  "All Time"
        }
    }

    fun getDateRange(filter: DateFilter): Pair<Date, Date> {
        val calendar = Calendar.getInstance()

        // Set to the end of the current day
        calendar.set(Calendar.HOUR_OF_DAY, 23)
        calendar.set(Calendar.MINUTE, 59)
        calendar.set(Calendar.SECOND, 59)
        val endDate = calendar.time

        when (filter) {
            DateFilter.DAY -> {
                calendar.set(Calendar.HOUR_OF_DAY, 0)
                calendar.set(Calendar.MINUTE, 0)
                calendar.set(Calendar.SECOND, 0)
            }
            DateFilter.WEEK -> {
                calendar.set(Calendar.DAY_OF_WEEK, calendar.firstDayOfWeek)
                calendar.set(Calendar.HOUR_OF_DAY, 0)
                calendar.set(Calendar.MINUTE, 0)
                calendar.set(Calendar.SECOND, 0)
            }
            DateFilter.MONTH -> {
                calendar.set(Calendar.DAY_OF_MONTH, 1)
                calendar.set(Calendar.HOUR_OF_DAY, 0)
                calendar.set(Calendar.MINUTE, 0)
                calendar.set(Calendar.SECOND, 0)
            }
            DateFilter.YEAR -> {
                calendar.set(Calendar.DAY_OF_YEAR, 1)
                calendar.set(Calendar.HOUR_OF_DAY, 0)
                calendar.set(Calendar.MINUTE, 0)
                calendar.set(Calendar.SECOND, 0)
            }
            // For ALL, we use a very old date to a very future date to include everything
            DateFilter.ALL -> return Pair(Date(0), Date(Long.MAX_VALUE))
        }
        val startDate = calendar.time
        return Pair(startDate, endDate)
    }
}