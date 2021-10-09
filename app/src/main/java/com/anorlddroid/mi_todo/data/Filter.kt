package com.anorlddroid.mi_todo.data

import androidx.compose.runtime.Stable
import androidx.compose.runtime.mutableStateOf

@Stable
class Filter(
    val name: String,
    enabled: Boolean = false
) {
    val enabled = mutableStateOf(enabled)
}
val weekdaysFilter = listOf(
    Filter("Monday"),
    Filter("Tuesday"),
    Filter("Wednesday"),
    Filter("Thursday"),
    Filter("Friday"),
    Filter("Saturday"),
    Filter("Sunday"),
    )
fun getWeekdayFilters() = weekdaysFilter

val categoriesFilter = listOf(
    Filter("Work"),
    Filter("School"),
    Filter("Home"),
)
fun getCategoriesFilters() = categoriesFilter