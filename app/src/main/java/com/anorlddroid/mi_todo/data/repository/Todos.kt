package com.anorlddroid.mi_todo.data.repository

import androidx.compose.runtime.Stable
import com.anorlddroid.mi_todo.data.database.DateTimeTypeConverters
import com.anorlddroid.mi_todo.data.database.TodoMinimal
import java.time.LocalDate
import java.time.LocalTime

@Stable
class ToDos(
    val name: String,
    val date: String?,
    val time: String?,
    val repeat: String,
    val hide: Boolean,
    val delete: Boolean
)

val TodosList = arrayOf(
    TodoMinimal(
        "Clean the house the first thing in the morning before even taking tea ",
        DateTimeTypeConverters.fromLocalDate(LocalDate.now()),
        DateTimeTypeConverters.fromLocalTime(LocalTime.now()),
        "Never",
        true,
        false
    ),
    TodoMinimal(
        "Clean the house", DateTimeTypeConverters.fromLocalDate(LocalDate.now()),
        DateTimeTypeConverters.fromLocalTime(LocalTime.now()),
        "Never",
        true,
        false
    ),
    TodoMinimal(
        "Clean the house",
        DateTimeTypeConverters.fromLocalDate(LocalDate.now()),
        DateTimeTypeConverters.fromLocalTime(LocalTime.now()),
        "Never",
        true,
        false
    ),
    TodoMinimal(
        "Clean the house the first thing in the morning before even taking tea ",
        DateTimeTypeConverters.fromLocalDate(LocalDate.now()),
        DateTimeTypeConverters.fromLocalTime(LocalTime.now()),
        "Never",
        true,
        false
    ),
    TodoMinimal(
        "Clean the house", DateTimeTypeConverters.fromLocalDate(LocalDate.now()),
        DateTimeTypeConverters.fromLocalTime(LocalTime.now()),
        "Never",
        true,
        false
    ),
    TodoMinimal(
        "Clean the house",
        DateTimeTypeConverters.fromLocalDate(LocalDate.now()),
        DateTimeTypeConverters.fromLocalTime(LocalTime.now()),
        "Never",
        true,
        false
    ),
    TodoMinimal(
        "Clean the house the first thing in the morning before even taking tea ",
        DateTimeTypeConverters.fromLocalDate(LocalDate.now()),
        DateTimeTypeConverters.fromLocalTime(LocalTime.now()),
        "Never",
        true,
        false
    ),
    TodoMinimal(
        "Clean the house", DateTimeTypeConverters.fromLocalDate(LocalDate.now()),
        DateTimeTypeConverters.fromLocalTime(LocalTime.now()),
        "Never",
        true,
        false
    ),
    TodoMinimal(
        "Clean the house",
        DateTimeTypeConverters.fromLocalDate(LocalDate.now()),
        DateTimeTypeConverters.fromLocalTime(LocalTime.now()),
        "Never",
        true,
        false
    ),
    TodoMinimal(
        "Clean the house the first thing in the morning before even taking tea ",
        DateTimeTypeConverters.fromLocalDate(LocalDate.now()),
        DateTimeTypeConverters.fromLocalTime(LocalTime.now()),
        "Never",
        true,
        false
    ),
    TodoMinimal(
        "Clean the house", DateTimeTypeConverters.fromLocalDate(LocalDate.now()),
        DateTimeTypeConverters.fromLocalTime(LocalTime.now()),
        "Never",
        true,
        false
    ),
    TodoMinimal(
        "Clean the house",
        DateTimeTypeConverters.fromLocalDate(LocalDate.now()),
        DateTimeTypeConverters.fromLocalTime(LocalTime.now()),
        "Never",
        true,
        false
    ),

    TodoMinimal(
        "Clean the house the first thing in the morning before even taking tea ",
        DateTimeTypeConverters.fromLocalDate(LocalDate.now()),
        DateTimeTypeConverters.fromLocalTime(LocalTime.now()),
        "Never",
        true,
        false
    ),
    TodoMinimal(
        "Clean the house", DateTimeTypeConverters.fromLocalDate(LocalDate.now()),
        DateTimeTypeConverters.fromLocalTime(LocalTime.now()),
        "Never",
        true,
        false
    ),
    TodoMinimal(
        "Clean the house",
        DateTimeTypeConverters.fromLocalDate(LocalDate.now()),
        DateTimeTypeConverters.fromLocalTime(LocalTime.now()),
        "Never",
        true,
        false
    ),
)

fun getTodos(): Array<TodoMinimal> = TodosList