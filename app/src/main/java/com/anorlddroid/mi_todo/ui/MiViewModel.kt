package com.anorlddroid.mi_todo.ui


import android.app.Application
import android.content.Context
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.anorlddroid.mi_todo.data.database.*
import com.anorlddroid.mi_todo.data.repository.Repository
import com.anorlddroid.mi_todo.ui.utils.DataStoreManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalTime

class MiTodoViewModel(application: Application) : AndroidViewModel(application) {
    // Holds our currently selected category
    var dataStoreManager: DataStoreManager = DataStoreManager(application)
    private val _selectedCategory = MutableStateFlow("All")
    private var repository: Repository
    private val _categories = MutableStateFlow<List<String>>(emptyList())
    private val _todosHashMap =
        MutableStateFlow<MutableMap<String, MutableList<TodoMinimal>>>(mutableMapOf())

    private val _refreshing = MutableStateFlow(false)
    val refreshing: MutableStateFlow<Boolean>
        get() = _refreshing

    private val _themeState = MutableStateFlow("")
    val themeState: StateFlow<String>
        get() = _themeState

    private val _snoozeTime = MutableStateFlow(5)
    val snoozeTime: StateFlow<Int>
        get() = _snoozeTime

    private val _hideState = MutableStateFlow("")
    val hideState: StateFlow<String>
        get() = _hideState

    val todaysTaskList: MutableList<TodoMinimal>?
        get() = _todosHashMap.value["Today"]

    val selectedCategory: StateFlow<String>
        get() = _selectedCategory

    val categories: StateFlow<List<String>>
        get() = _categories

    val todos: StateFlow<Map<String, List<TodoMinimal>>>
        get() = _todosHashMap


    init {
        viewModelScope.launch {
            dataStoreManager.getThemeFromDataStore.collect {
                it?.let { theme ->
                    _themeState.value = theme
                }
            }
        }
        viewModelScope.launch {
            dataStoreManager.getHideFromDataStore.collect {
                it?.let { hide ->
                    _hideState.value = hide
                }
            }
        }
        viewModelScope.launch {
            dataStoreManager.getSnoozeFromDataStore.collect {
                it?.let { time ->
                    _snoozeTime.value = time
                }
            }
        }
        val db = MiTodoDatabase.getDatabase(application)
        repository = Repository(db)
        viewModelScope.launch {
            //get all categories
            repository.getAllCategories().buffer().collect {
                _categories.value = it
            }
        }
        viewModelScope.launch(Dispatchers.IO) {
            formatTodos(todolist = repository.getAllTodos())
        }
        viewModelScope.launch(Dispatchers.IO) {
            SetAlarms(application)
        }
    }


    fun onFilterSelected(category: String) {
        _selectedCategory.value = category
        val todoList = if (category == "Meals") repository.getAllMeals()
        else if (category == "All") repository.getAllTodos()
        else repository.getAllTodosByCategory(
            category
        )
        viewModelScope.launch {
            formatTodos(todolist = todoList)
        }
        refresh()
    }

    fun updateSetting(name: String, setting: String) {
        viewModelScope.launch {
            if (name == "Theme") {
                _themeState.value = setting
                dataStoreManager.saveThemeToDataStore(
                    option = setting
                )
            } else {
                _hideState.value = setting
                dataStoreManager.saveHideToDataStore(
                    hide = setting
                )
            }
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
        context: Context,
        completed: Boolean = false,
        type: MealType? = null,
        deleteTodo: TodoMinimal?
    ) {
        if (deleteTodo != null) {
            deleteTodo(deleteTodo.id)
        }
        val todoEntity = TodoEntity(
            category = categoryName,
            name = todo,
            date = date,
            time = time,
            repeat = repeat,
            hide = hide,
            delete = delete,
            completed = completed,
            type = type
        )
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertTodo(todoEntity)
            withContext(Dispatchers.Main) {
                Toast.makeText(context, "Task added", Toast.LENGTH_SHORT).show()
                refresh()
            }
        }
    }


    fun insertCategory(entity: String, context: Context) {
        viewModelScope.launch(Dispatchers.IO) {
            val categoryEntity = CategoryEntity(name = entity)
            categoryEntity.name = entity
            repository.insertCategory(categoryEntity)
            withContext(Dispatchers.Main) {
                Toast.makeText(context, "Added category", Toast.LENGTH_SHORT).show()
            }
        }
    }


    fun deleteTodo(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteTodo(id)
            refresh()
        }
    }

    fun completedTodo(completed: Boolean, id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.completedTodo(completed, id)
        }
    }

    fun snoozeTodo(todoMinimal: TodoMinimal, context: Context) {
        viewModelScope.launch {
            repository.snoozeTodo(todoMinimal, snoozeTime.value.toLong(), context)
        }
    }

    fun refresh() {
        _refreshing.value = true
        val todoList = if (_selectedCategory.value == "Meals") repository.getAllMeals()
        else if (_selectedCategory.value == "All") repository.getAllTodos()
        else repository.getAllTodosByCategory(
            _selectedCategory.value
        )
        viewModelScope.launch {
            formatTodos(todolist = todoList)
        }
        viewModelScope.launch {
            delay(2000)
            _refreshing.value = false
        }
    }

    fun updateSnoozeTime(time: Int) {
        _snoozeTime.value = time
        viewModelScope.launch {
            dataStoreManager.saveSnoozeToDataStore(
                snooze = time
            )
        }
    }

    private suspend fun formatTodos(todolist: Flow<List<TodoMinimal>>) {
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
                    "Sunday" to mutableListOf()
                )
                it.forEach { todoMinimal ->
                    sanitizeTasks(
                        date = DateTimeTypeConverters.toLocalDate(todoMinimal.date),
                        time = DateTimeTypeConverters.toLocalTime(todoMinimal.time),
                        delete = todoMinimal.delete,
                        todo = todoMinimal.name,
                        repeat = todoMinimal.repeat,
                        id = todoMinimal.id
                    )
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

    fun sanitizeTasks(
        date: LocalDate,
        time: LocalTime,
        delete: Boolean,
        todo: String,
        id: Int,
        repeat: String
    ) {
        if (date < LocalDate.now() && delete && repeat == "Never") {
            viewModelScope.launch {
                repository.deleteTodo(id)
            }
        } else if (date == LocalDate.now() && time < LocalTime.now() && delete && repeat == "Never") {
            viewModelScope.launch {
                repository.deleteTodo(id)
            }
        } else if (date < LocalDate.now() && repeat == "Daily") {
            viewModelScope.launch {
                val ndate = date.plusDays(1)
                repository.updateTodo(DateTimeTypeConverters.fromLocalDate(ndate), id)
                repository.completedTodo(false, id)
            }
        } else if (date < LocalDate.now() && repeat == "Weekly") {
            viewModelScope.launch {
                val ndate = date.plusWeeks(1)
                repository.updateTodo(DateTimeTypeConverters.fromLocalDate(ndate), id)
                repository.completedTodo(false, id)

            }
        } else if (date < LocalDate.now() && repeat == "Monthly") {
            viewModelScope.launch {
                val ndate = date.plusMonths(1)
                repository.updateTodo(DateTimeTypeConverters.fromLocalDate(ndate), id)
                repository.completedTodo(false, id)
            }
        } else if (date < LocalDate.now() && repeat == "Yearly") {
            viewModelScope.launch {
                val ndate = date.plusYears(1)
                repository.updateTodo(DateTimeTypeConverters.fromLocalDate(ndate), id)
                repository.completedTodo(false, id)
            }
        }
    }

    suspend fun SetAlarms(context: Context) {
        todos.collect { todosMap ->
            todosMap.forEach { (title, todosList) ->
                todosList.forEach { todo ->
                    setAlarm(
                        date = DateTimeTypeConverters.toLocalDate(todo.date),
                        time = DateTimeTypeConverters.toLocalTime(todo.time),
                        context = context,
                        category = todo.category,
                        todo = todo.name,
                        notificationID = todo.id
                    )
                }
            }
        }
    }
}



