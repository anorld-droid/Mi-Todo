package com.anorlddroid.mi_todo.data.database

import androidx.room.TypeConverter
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

object DateTimeTypeConverters {

    @TypeConverter
    @JvmStatic
    fun toLocalDate(value: String): LocalDate {
        return LocalDate.parse(value, DateTimeFormatter.ofPattern("EEEE, dd  MMMM yyyy"))
    }

    @TypeConverter
    @JvmStatic
    fun fromLocalDate(value: LocalDate): String {
        return value.format(DateTimeFormatter.ofPattern("EEEE, dd  MMMM yyyy"))
    }

    @TypeConverter
    @JvmStatic
    fun toLocalTime(value: String): LocalTime {
        return value.let { LocalTime.parse(value, DateTimeFormatter.ofPattern("hh:mm a")) }
    }

    @TypeConverter
    @JvmStatic
    fun fromLocalTime(value: LocalTime): String {
        return value.format(DateTimeFormatter.ofPattern("hh:mm a"))
    }

}