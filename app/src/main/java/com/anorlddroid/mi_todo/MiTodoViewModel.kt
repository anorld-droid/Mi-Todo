package com.anorlddroid.mi_todo

import android.app.Application
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.anorlddroid.mi_todo.data.database.*
import com.anorlddroid.mi_todo.data.repository.Repository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.LocalDate

class MiTodoViewModel(application: Application) : AndroidViewModel(application) {
    // Holds our currently selected category
    private val _selectedCategory = MutableStateFlow("All")
    private var repository: Repository
    private val _categories = MutableStateFlow<List<String>>(emptyList())

    private val _todosHashMap =
        MutableStateFlow<MutableMap<String, MutableList<TodoMinimal>>>(mutableMapOf())

    // Holds our view state which the UI collects via [state]
//    private val _state = MutableStateFlow(HomeViewState())

    private val refreshing = MutableStateFlow(false)

    private val _themeState = MutableStateFlow("")
    val themeState: StateFlow<String>
        get() = _themeState

    private val _hideState = MutableStateFlow("")
    val hideState: StateFlow<String>
        get() = _hideState

    val selectedCategory: StateFlow<String>
        get() = _selectedCategory

    val categories: StateFlow<List<String>>
        get() = _categories

    val todos: StateFlow<Map<String, List<TodoMinimal>>>
        get() = _todosHashMap


    init {
        val db = MiTodoDatabase.getDatabase(application)
        repository = Repository(db)
        viewModelScope.launch {
            _themeState.value = repository.getSetting("Theme")
            _hideState.value = repository.getSetting("Hide")
            //get all categories
            repository.getAllCategories().buffer().collect {
                _categories.value = it
                Log.d("VIEWMODEL", " Categories from repo :${it}")
            }
        }
        viewModelScope.launch {
            updateTodos(todolist = repository.getAllTodos())
        }
    }


    fun onFilterSelected(category: String) {
        Log.d("ONFILTERSELECTED", category)
        _selectedCategory.value = category
    }

    fun updateSetting(name: String, setting: String) {
        viewModelScope.launch {
            if (name == "Theme") {
                _themeState.value = setting
            } else {
                _hideState.value = setting
            }
            val settings = SettingsEntity(name = name, setting = setting)
            repository.insertSetting(settings)
        }
    }

    fun insertTodo(
        categoryName: String,
        todo: String,
        date: String,
        time: String,
        repeat: String,
        hide: Boolean,
        delete: Boolean,
        context: Context
    ) {
        viewModelScope.launch {
            val todoEntity = TodoEntity(
                category = categoryName,
                name = todo,
                date = date,
                time = time,
                repeat = repeat,
                hide = hide,
                delete = delete
            )
            repository.insertTodo(todoEntity)
            Toast.makeText(context, "Added task", Toast.LENGTH_SHORT).show()
        }
    }

    fun insertCategory(entity: String, context: Context) {
        viewModelScope.launch {
            val categoryEntity = CategoryEntity(name = entity)
            categoryEntity.name = entity
            repository.insertCategory(categoryEntity)
            Toast.makeText(context, "Added category", Toast.LENGTH_SHORT).show()

        }
    }

    fun deleteTodo(todo: String) {
        viewModelScope.launch {
            repository.deleteTodo(todo)
        }
    }

    private suspend fun updateTodos(todolist: Flow<List<TodoMinimal>>) {
        todolist.collect {
            if (it.isNotEmpty()) {
                _todosHashMap.value = mutableMapOf(
                    "Today" to mutableListOf(),
                    "Tomorrow" to mutableListOf(),
                    "Monday" to mutableListOf(),
                    "Tuesday" to mutableListOf(),
                    "Wednesday" to mutableListOf(),
                    "Thursday" to mutableListOf(),
                    "Friday" to mutableListOf(),
                    "Saturday" to mutableListOf(),
                    "Today" to mutableListOf()
                )
                it.forEach { todoMinimal ->
                    Log.d("REPO", " Collecting  :${todoMinimal}")
                    val today = LocalDate.now()
                    val tomorrow = today.plusDays(1)
                    if (todoMinimal.date.let { it1 ->
                            DateTimeTypeConverters.toLocalDate(
                                it1
                            )
                        } == today) {
                        if (_todosHashMap.value["Today"]?.isEmpty() == true) {
                            _todosHashMap.value["Today"] = mutableListOf(todoMinimal)
                        } else {
                            _todosHashMap.value["Today"]?.add(todoMinimal)
                        }
                    }
                    if (todoMinimal.date.let { it1 ->
                            DateTimeTypeConverters.toLocalDate(
                                it1
                            )
                        } == tomorrow) {
                        if (_todosHashMap.value["Tomorrow"]?.isEmpty() == true) {
                            _todosHashMap.value["Tomorrow"] = mutableListOf(todoMinimal)
                        } else {
                            _todosHashMap.value["Tomorrow"]?.add(todoMinimal)
                        }
                    }
                    if (todoMinimal.date.let { it1 ->
                            DateTimeTypeConverters.toLocalDate(
                                it1
                            ).dayOfWeek
                        } == DayOfWeek.MONDAY
                        && todoMinimal.date.let { it1 ->
                            DateTimeTypeConverters.toLocalDate(
                                it1
                            )
                        } != today
                        && todoMinimal.date.let { it1 ->
                            DateTimeTypeConverters.toLocalDate(
                                it1
                            )
                        } != tomorrow
                    ) {
                        if (_todosHashMap.value["Monday"]?.isEmpty() == true) {
                            _todosHashMap.value["Monday"] = mutableListOf(todoMinimal)
                        } else {
                            _todosHashMap.value["Monday"]?.add(todoMinimal)
                        }
                    }
                    if (todoMinimal.date.let { it1 ->
                            DateTimeTypeConverters.toLocalDate(
                                it1
                            ).dayOfWeek
                        } == DayOfWeek.TUESDAY
                        && todoMinimal.date.let { it1 ->
                            DateTimeTypeConverters.toLocalDate(
                                it1
                            )
                        } != today
                        && todoMinimal.date.let { it1 ->
                            DateTimeTypeConverters.toLocalDate(
                                it1
                            )
                        } != tomorrow
                    ) {
                        if (_todosHashMap.value["Tuesday"]?.isEmpty() == true) {
                            _todosHashMap.value["Tuesday"] = mutableListOf(todoMinimal)
                        } else {
                            _todosHashMap.value["Tuesday"]?.add(todoMinimal)
                        }
                    }
                    if (todoMinimal.date.let { it1 ->
                            DateTimeTypeConverters.toLocalDate(
                                it1
                            ).dayOfWeek
                        } == DayOfWeek.WEDNESDAY
                        && todoMinimal.date.let { it1 ->
                            DateTimeTypeConverters.toLocalDate(
                                it1
                            )
                        } != today
                        && todoMinimal.date.let { it1 ->
                            DateTimeTypeConverters.toLocalDate(
                                it1
                            )
                        } != tomorrow
                    ) {
                        if (_todosHashMap.value["Wednesday"]?.isEmpty() == true) {
                            _todosHashMap.value["Wednesday"] = mutableListOf(todoMinimal)
                        } else {
                            _todosHashMap.value["Wednesday"]?.add(todoMinimal)
                        }
                    }
                    if (todoMinimal.date.let { it1 ->
                            DateTimeTypeConverters.toLocalDate(
                                it1
                            ).dayOfWeek
                        } == DayOfWeek.THURSDAY
                        && todoMinimal.date.let { it1 ->
                            DateTimeTypeConverters.toLocalDate(
                                it1
                            )
                        } != today
                        && todoMinimal.date.let { it1 ->
                            DateTimeTypeConverters.toLocalDate(
                                it1
                            )
                        } != tomorrow
                    ) {
                        if (_todosHashMap.value["Thursday"]?.isEmpty() == true) {
                            _todosHashMap.value["Thursday"] = mutableListOf(todoMinimal)
                        } else {
                            _todosHashMap.value["Thursday"]?.add(todoMinimal)
                        }
                    }
                    if (todoMinimal.date.let { it1 ->
                            DateTimeTypeConverters.toLocalDate(
                                it1
                            ).dayOfWeek
                        } == DayOfWeek.FRIDAY
                        && todoMinimal.date.let { it1 ->
                            DateTimeTypeConverters.toLocalDate(
                                it1
                            )
                        } != today
                        && todoMinimal.date.let { it1 ->
                            DateTimeTypeConverters.toLocalDate(
                                it1
                            )
                        } != tomorrow
                    ) {
                        if (_todosHashMap.value["Friday"]?.isEmpty() == true) {
                            _todosHashMap.value["Friday"] = mutableListOf(todoMinimal)
                        } else {
                            _todosHashMap.value["Friday"]?.add(todoMinimal)
                        }
                    }
                    if (todoMinimal.date.let { it1 ->
                            DateTimeTypeConverters.toLocalDate(
                                it1
                            ).dayOfWeek
                        } == DayOfWeek.SATURDAY
                        && todoMinimal.date.let { it1 ->
                            DateTimeTypeConverters.toLocalDate(
                                it1
                            )
                        } != today
                        && todoMinimal.date.let { it1 ->
                            DateTimeTypeConverters.toLocalDate(
                                it1
                            )
                        } != tomorrow
                    ) {
                        if (_todosHashMap.value["Saturday"]?.isEmpty() == true) {
                            _todosHashMap.value["Saturday"] = mutableListOf(todoMinimal)
                        } else {
                            _todosHashMap.value["Saturday"]?.add(todoMinimal)
                        }
                    }
                    if (todoMinimal.date.let { it1 ->
                            DateTimeTypeConverters.toLocalDate(
                                it1
                            ).dayOfWeek
                        } == DayOfWeek.SUNDAY
                        && todoMinimal.date.let { it1 ->
                            DateTimeTypeConverters.toLocalDate(
                                it1
                            )
                        } != today
                        && todoMinimal.date.let { it1 ->
                            DateTimeTypeConverters.toLocalDate(
                                it1
                            )
                        } != tomorrow
                    ) {
                        if (_todosHashMap.value["Sunday"]?.isEmpty() == true) {
                            _todosHashMap.value["Sunday"] = mutableListOf(todoMinimal)
                        } else {
                            _todosHashMap.value["Sunday"]?.add(todoMinimal)
                        }
                    }
                }
            }
        }
    }
}


