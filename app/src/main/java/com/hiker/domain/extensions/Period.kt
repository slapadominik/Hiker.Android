package com.hiker.domain.extensions

import java.util.*


class Period {
    companion object{
        fun between(date1: Date, date2: Date) : Int{
            val a = getCalendar(date1)
            val b = getCalendar(date2)
            var diff = b.get(Calendar.YEAR) - a.get(Calendar.YEAR)
            if (a.get(Calendar.DAY_OF_YEAR) > b.get(Calendar.DAY_OF_YEAR)) {
                diff--
            }
            return diff
        }

        fun getCalendar(date: Date): Calendar {
            val cal = Calendar.getInstance(Locale.US)
            cal.time = date
            return cal
        }
    }
}