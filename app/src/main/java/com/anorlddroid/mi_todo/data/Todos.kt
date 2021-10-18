package com.anorlddroid.mi_todo.data

import androidx.compose.runtime.Stable

@Stable
class ToDos(
    val name: String,
    val time : String
)

val TodosList = arrayOf(
    ToDos("Clean the house the first thing in the morning before even taking tea ", "07:30am"),
    ToDos("Clean the house", "07:30am"),
    ToDos("Clean the house", "07:30am"),
    ToDos("Clean the house the first thing in the morning before even taking tea", "07:30am"),
    ToDos("Clean the house", "07:30am"),
    ToDos("Clean the house", "07:30am"),
    ToDos("Clean the house the first thing in the morning before even taking tea", "07:30am"),
    ToDos("Clean the house", "07:30am"),
    ToDos("Clean the house the first thing in the morning before even taking tea", "07:30am"),

    )
fun getTodos() = TodosList
