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


    // Holds our view state which the UI collects via [state]
    private val _state = MutableStateFlow(HomeViewState())

    private val refreshing = MutableStateFlow(false)

    private val _themeState = MutableStateFlow("")
    val themeState: StateFlow<String>
        get() = _themeState

    private val _hideState = MutableStateFlow("")
    val hideState: StateFlow<String>
        get() = _hideState

//    val selectedCategory: StateFlow<String>
//        get() = _selectedCategory

    val categories: StateFlow<List<String>>
        get() = _categories

//    val todos: StateFlow<Map<String, MutableList<TodoMinimal>>>
//        get() = _todosHashMap

    val state: StateFlow<HomeViewState>
        get() = _state

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
            updateTodos("All")
        }
    }


    fun onFilterSelected(category: String) {
        Log.d("ONFILTERSELECTED", category)
        viewModelScope.launch {
            updateTodos(category = category)
        }
//        viewModelScope.launch {
//            val todosList = if (_selectedCategory.value != "All") {
//                if (_hideState.value == "On") {
//                    repository.getTodoByCategory(_selectedCategory.value, "false")
//
//                } else {
//                    repository.getAllTodoByCategory(_selectedCategory.value)
//                }
//            } else {
//                if (_hideState.value == "On") {
//                    repository.getUnHiddenTodos("false")
//                } else {
//                    repository.getAllTodos()
//                }
//            }
//            _todosHashMap.value = mutableMapOf(
//                "Today" to mutableListOf(),
//                "Tomorrow" to mutableListOf(),
//                "Monday" to mutableListOf(),
//                "Tuesday" to mutableListOf(),
//                "Wednesday" to mutableListOf(),
//                "Thursday" to mutableListOf(),
//                "Friday" to mutableListOf(),
//                "Saturday" to mutableListOf(),
//                "Sunday" to mutableListOf()
//            )
//            updateTodos(todoList = todosList)
//        }
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

    private suspend fun updateTodos(category: String) {
        _selectedCategory.value = category
        combine(
            _selectedCategory,
            if (_selectedCategory.value != "All") {
                if (_hideState.value == "On") {
                    sortTodos(repository.getTodoByCategory(_selectedCategory.value, "false"))

                } else {
                    sortTodos(repository.getAllTodoByCategory(_selectedCategory.value))
                }
            } else {
                if (_hideState.value == "On") {
                    sortTodos(repository.getUnHiddenTodos("false"))
                } else {
                    sortTodos(repository.getAllTodos())
                }
            }
        ) { selectedCategory, todoMap ->
            HomeViewState(
                selectedCategory = selectedCategory,
                todos = todoMap
            )
        }.catch { throwable ->
            throw throwable
        }.collect {
            _state.value = it
        }
    }

    private suspend fun sortTodos(todolist: Flow<List<TodoMinimal>>): MutableStateFlow<MutableMap<String, MutableList<TodoMinimal>>> {
        val todosHashMap = MutableStateFlow<MutableMap<String, MutableList<TodoMinimal>>>(
            mutableMapOf(
                "Today" to mutableListOf(),
                "Tomorrow" to mutableListOf(),
                "Monday" to mutableListOf(),
                "Tuesday" to mutableListOf(),
                "Wednesday" to mutableListOf(),
                "Thursday" to mutableListOf(),
                "Friday" to mutableListOf(),
                "Saturday" to mutableListOf(),
                "Sunday" to mutableListOf()
            )
        )
        todolist.collect {
            it.forEach { todoMinimal ->
                Log.d("REPO", " Collecting  :${todoMinimal}")
                val today = LocalDate.now()
                val tomorrow = today.plusDays(1)
                if (todoMinimal.date.let { it1 ->
                        DateTimeTypeConverters.toLocalDate(
                            it1
                        )
                    } == today) {
                    if (todosHashMap.value["Today"]?.isEmpty() == true) {
                        todosHashMap.value["Today"] = mutableListOf(todoMinimal)
                    } else {
                        todosHashMap.value["Today"]?.add(todoMinimal)
                    }
                }
                if (todoMinimal.date.let { it1 ->
                        DateTimeTypeConverters.toLocalDate(
                            it1
                        )
                    } == tomorrow) {
                    if (todosHashMap.value["Tomorrow"]?.isEmpty() == true) {
                        todosHashMap.value["Tomorrow"] = mutableListOf(todoMinimal)
                    } else {
                        todosHashMap.value["Tomorrow"]?.add(todoMinimal)
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
                    if (todosHashMap.value["Monday"]?.isEmpty() == true) {
                        todosHashMap.value["Monday"] = mutableListOf(todoMinimal)
                    } else {
                        todosHashMap.value["Monday"]?.add(todoMinimal)
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
                    if (todosHashMap.value["Tuesday"]?.isEmpty() == true) {
                        todosHashMap.value["Tuesday"] = mutableListOf(todoMinimal)
                    } else {
                        todosHashMap.value["Tuesday"]?.add(todoMinimal)
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
                    if (todosHashMap.value["Wednesday"]?.isEmpty() == true) {
                        todosHashMap.value["Wednesday"] = mutableListOf(todoMinimal)
                    } else {
                        todosHashMap.value["Wednesday"]?.add(todoMinimal)
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
                    if (todosHashMap.value["Thursday"]?.isEmpty() == true) {
                        todosHashMap.value["Thursday"] = mutableListOf(todoMinimal)
                    } else {
                        todosHashMap.value["Thursday"]?.add(todoMinimal)
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
                    if (todosHashMap.value["Friday"]?.isEmpty() == true) {
                        todosHashMap.value["Friday"] = mutableListOf(todoMinimal)
                    } else {
                        todosHashMap.value["Friday"]?.add(todoMinimal)
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
                    if (todosHashMap.value["Saturday"]?.isEmpty() == true) {
                        todosHashMap.value["Saturday"] = mutableListOf(todoMinimal)
                    } else {
                        todosHashMap.value["Saturday"]?.add(todoMinimal)
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
                    if (todosHashMap.value["Sunday"]?.isEmpty() == true) {
                        todosHashMap.value["Sunday"] = mutableListOf(todoMinimal)
                    } else {
                        todosHashMap.value["Sunday"]?.add(todoMinimal)
                    }
                }
            }
        }
        Log.d("REPO", "Returning ==> ${todosHashMap.value}")
        return todosHashMap
    }
}

data class HomeViewState(
    val selectedCategory: String = "All",
    val todos: Map<String, MutableList<TodoMinimal>> = emptyMap()
)


