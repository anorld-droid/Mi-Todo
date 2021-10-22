package com.anorlddroid.mi_todo

import android.app.Application
import android.util.Log
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
    private val selectedCategory = MutableStateFlow<String?>(null)
    private var repository: Repository
    private lateinit var categories: Flow<List<String>>

    // Holds our view state which the UI collects via [state]
    private val _state = MutableStateFlow(MiTodoViewState())

    private val refreshing = MutableStateFlow(false)

    private val _themeState = MutableStateFlow("")
    val themeState: StateFlow<String>
        get() = _themeState

    private val _hideState = MutableStateFlow("")
    val hideState: StateFlow<String>
        get() = _hideState

    val state: StateFlow<MiTodoViewState>
        get() = _state

    init {
        val db = MiTodoDatabase.getDatabase(application)
        repository = Repository(db)
        viewModelScope.launch {
            //get categories and todos from the database
            _themeState.value = repository.getSetting("Theme")
            _hideState.value = repository.getSetting("Hide")
            Log.d("VIEWMODEL", " Theme state value :${_themeState.value}")
            categories = repository.getAllCategories()
            Log.d("VIEWMODEL", " Theme state value :${categories}")
            val todos = if (_hideState.value == "On") {
                repository.getUnHiddenTodos()
            } else {
                repository.getAllTodos()
            }
            val hashMap: MutableMap<String, MutableList<TodoMinimal>> =
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
            todos.collect {
                it.forEach { todoMinimal ->
                    val today = LocalDate.now()
                    val tomorrow = today.plusDays(1)
                    if (todoMinimal.date?.let { it1 -> DateTimeTypeConverters.toLocalDate(it1) } == today) {
                        if (hashMap["Today"]?.isEmpty() == true) {
                            hashMap["Today"] = mutableListOf(todoMinimal)
                        } else {
                            hashMap["Today"]?.add(todoMinimal)
                        }
                    }
                    if (todoMinimal.date?.let { it1 -> DateTimeTypeConverters.toLocalDate(it1) } == tomorrow) {
                        if (hashMap["Tomorrow"]?.isEmpty() == true) {
                            hashMap["Tomorrow"] = mutableListOf(todoMinimal)
                        } else {
                            hashMap["Tomorrow"]?.add(todoMinimal)
                        }
                    }
                    if (todoMinimal.date?.let { it1 -> DateTimeTypeConverters.toLocalDate(it1).dayOfWeek } == DayOfWeek.MONDAY
                        && todoMinimal.date.let { it1 -> DateTimeTypeConverters.toLocalDate(it1) } != today
                        && todoMinimal.date.let { it1 -> DateTimeTypeConverters.toLocalDate(it1) } != tomorrow
                    ) {
                        if (hashMap["Monday"]?.isEmpty() == true) {
                            hashMap["Monday"] = mutableListOf(todoMinimal)
                        } else {
                            hashMap["Monday"]?.add(todoMinimal)
                        }
                    }
                    if (todoMinimal.date?.let { it1 -> DateTimeTypeConverters.toLocalDate(it1).dayOfWeek } == DayOfWeek.TUESDAY
                        && todoMinimal.date.let { it1 -> DateTimeTypeConverters.toLocalDate(it1) } != today
                        && todoMinimal.date.let { it1 -> DateTimeTypeConverters.toLocalDate(it1) } != tomorrow
                    ) {
                        if (hashMap["Tuesday"]?.isEmpty() == true) {
                            hashMap["Tuesday"] = mutableListOf(todoMinimal)
                        } else {
                            hashMap["Tuesday"]?.add(todoMinimal)
                        }
                    }
                    if (todoMinimal.date?.let { it1 -> DateTimeTypeConverters.toLocalDate(it1).dayOfWeek } == DayOfWeek.WEDNESDAY
                        && todoMinimal.date.let { it1 -> DateTimeTypeConverters.toLocalDate(it1) } != today
                        && todoMinimal.date.let { it1 -> DateTimeTypeConverters.toLocalDate(it1) } != tomorrow
                    ) {
                        if (hashMap["Wednesday"]?.isEmpty() == true) {
                            hashMap["Wednesday"] = mutableListOf(todoMinimal)
                        } else {
                            hashMap["Wednesday"]?.add(todoMinimal)
                        }
                    }
                    if (todoMinimal.date?.let { it1 -> DateTimeTypeConverters.toLocalDate(it1).dayOfWeek } == DayOfWeek.THURSDAY
                        && todoMinimal.date.let { it1 -> DateTimeTypeConverters.toLocalDate(it1) } != today
                        && todoMinimal.date.let { it1 -> DateTimeTypeConverters.toLocalDate(it1) } != tomorrow
                    ) {
                        if (hashMap["Thursday"]?.isEmpty() == true) {
                            hashMap["Thursday"] = mutableListOf(todoMinimal)
                        } else {
                            hashMap["Thursday"]?.add(todoMinimal)
                        }
                    }
                    if (todoMinimal.date?.let { it1 -> DateTimeTypeConverters.toLocalDate(it1).dayOfWeek } == DayOfWeek.FRIDAY
                        && todoMinimal.date.let { it1 -> DateTimeTypeConverters.toLocalDate(it1) } != today
                        && todoMinimal.date.let { it1 -> DateTimeTypeConverters.toLocalDate(it1) } != tomorrow
                    ) {
                        if (hashMap["Friday"]?.isEmpty() == true) {
                            hashMap["Friday"] = mutableListOf(todoMinimal)
                        } else {
                            hashMap["Friday"]?.add(todoMinimal)
                        }
                    }
                    if (todoMinimal.date?.let { it1 -> DateTimeTypeConverters.toLocalDate(it1).dayOfWeek } == DayOfWeek.SATURDAY
                        && todoMinimal.date.let { it1 -> DateTimeTypeConverters.toLocalDate(it1) } != today
                        && todoMinimal.date.let { it1 -> DateTimeTypeConverters.toLocalDate(it1) } != tomorrow
                    ) {
                        if (hashMap["Saturday"]?.isEmpty() == true) {
                            hashMap["Saturday"] = mutableListOf(todoMinimal)
                        } else {
                            hashMap["Saturday"]?.add(todoMinimal)
                        }
                    }
                    if (todoMinimal.date?.let { it1 -> DateTimeTypeConverters.toLocalDate(it1).dayOfWeek } == DayOfWeek.SUNDAY
                        && todoMinimal.date.let { it1 -> DateTimeTypeConverters.toLocalDate(it1) } != today
                        && todoMinimal.date.let { it1 -> DateTimeTypeConverters.toLocalDate(it1) } != tomorrow
                    ) {
                        if (hashMap["Sunday"]?.isEmpty() == true) {
                            hashMap["Sunday"] = mutableListOf(todoMinimal)
                        } else {
                            hashMap["Sunday"]?.add(todoMinimal)
                        }
                    }
                }
            }
            // Combines the latest value from each of the flows, allowing us to generate a
            // view state instance which only contains the latest values.
            combine(
                categories,
                selectedCategory,
                refreshing
            ) { categorises, selectedCategory, refresh ->
                MiTodoViewState(
                    categories = categorises,
                    refreshing = refresh,
                    selectedCategory = selectedCategory,
                    todos = hashMap
                )
            }.catch { throwable ->
                throw throwable
            }.collect {
                _state.value = it
            }
        }
    }

    fun onCategorySelected(category: String) {
        selectedCategory.value = category
//        viewModelScope.launch {
//            refreshing.value = true
//            val id : Long = repository.getCategoryID(category) ?: -1L
//            if (id != -1L) {
//                val newTodos: Flow<List<TodoMinimal>> = repository.getTodoByCategory(id)
//                combine(
//                    categories,
//                    refreshing,
//                    selectedCategory,
//                    newTodos
//                ) { categorises, refresh, selectedCategory, newTodoList ->
//                    MiTodoViewState(
//                        categories = categorises,
//                        refreshing = refresh,
//                        selectedCategory = selectedCategory,
//                        todos = newTodoList
//                    )
//                }.collect {
//                    _state.value = it
//                }
//                refreshing.value = false
//            }
//        }
    }

    fun updateSetting(name: String, setting: String) {
        viewModelScope.launch {
            repository.updateSetting(name = name, setting = setting)
        }
    }

    fun insertTodo(
        categoryName: String,
        todo: String,
        date: String,
        time: String,
        repeat: String,
        hide: Boolean,
        delete: Boolean
    ): Boolean {
        val status = MutableStateFlow(false)
        viewModelScope.launch {
            val categoryID: Long = repository.getCategoryID(categoryName) ?: -1L
            if (categoryID != -1L) {
                val todoEntity = TodoEntity(
                    categoryId = categoryID,
                    name = todo,
                    date = date,
                    time = time,
                    repeat = repeat,
                    hide = hide,
                    delete = delete
                )
                repository.insertTodo(todoEntity)
                status.value = true
            }
        }
        return status.value
    }

    fun insertCategory(entity: String): Boolean {
        val status = MutableStateFlow(false)
        viewModelScope.launch {
            val categoryEntity = CategoryEntity(name = entity)
            categoryEntity.name = entity
            if (repository.insertCategory(categoryEntity) == 1L) {
                status.value = true
            }
        }
        return status.value
    }

    fun deleteTodo(todo: String) {
        viewModelScope.launch {
            repository.deleteTodo(todo)
        }
    }
}

data class MiTodoViewState(
    val categories: List<String> = emptyList(),
    val refreshing: Boolean = false,
    val selectedCategory: String? = null,
    val todos: MutableMap<String, MutableList<TodoMinimal>> = emptyMap<String, MutableList<TodoMinimal>>().toMutableMap()
)

