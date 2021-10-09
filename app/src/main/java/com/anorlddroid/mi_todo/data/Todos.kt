package com.anorlddroid.mi_todo.data

import androidx.compose.runtime.Stable
import androidx.compose.runtime.mutableStateOf

@Stable
class ToDos(
    val name: String,
    val time : String
)

val TodosList = arrayOf(
    ToDos("Clean the house", "07:30am"),
    ToDos("Clean the house", "07:30am"),
    ToDos("Clean the house", "07:30am"),
    ToDos("Clean the house", "07:30am"),
    ToDos("Clean the house", "07:30am"),
    ToDos("Clean the house", "07:30am"),
    ToDos("Clean the house", "07:30am"),
    ToDos("Clean the house", "07:30am"),
    ToDos("Clean the house", "07:30am"),

)
fun getTodos() = TodosList
