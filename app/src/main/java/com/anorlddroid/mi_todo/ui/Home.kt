package com.anorlddroid.mi_todo.ui

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DragHandle
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.NavigateNext
import androidx.compose.material.icons.outlined.AutoAwesome
import androidx.compose.material.icons.outlined.Snooze
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.anorlddroid.mi_todo.R
import com.anorlddroid.mi_todo.data.database.DateTimeTypeConverters
import com.anorlddroid.mi_todo.data.database.MealType
import com.anorlddroid.mi_todo.data.database.TodoMinimal
import com.anorlddroid.mi_todo.ui.components.*
import com.anorlddroid.mi_todo.ui.theme.*
import com.anorlddroid.mi_todo.ui.utils.AlarmReceiver
import com.anorlddroid.mi_todo.ui.utils.makePhoneCall
import com.anorlddroid.mi_todo.ui.utils.openTwitter
import com.anorlddroid.mi_todo.ui.utils.openWhatsApp
import com.google.accompanist.insets.statusBarsPadding
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.SwipeRefreshIndicator
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.time.*

const val CHANNEL_ID = "channel 1"

@OptIn(ExperimentalAnimationApi::class, androidx.compose.material.ExperimentalMaterialApi::class)
@Composable
fun HomeScreen(
    coroutineScope: CoroutineScope,
    scaffoldState: ScaffoldState,
) {
    val viewModel: MiTodoViewModel = viewModel()
    val isRefreshing by viewModel.refreshing.collectAsState()
    var refresh = rememberSwipeRefreshState(isRefreshing)
    Box(modifier = Modifier.fillMaxSize()) {
        SwipeRefresh(
            state = refresh,
            onRefresh = {
                viewModel.refresh()
            },
            indicator = { state, trigger ->
                SwipeRefreshIndicator(
                    state = state,
                    refreshTriggerDistance = trigger,
                    scale = true,
                    backgroundColor = MaterialTheme.colors.secondary,
                    contentColor = MaterialTheme.colors.background,
                    arrowEnabled = true,
                    modifier = Modifier.padding(top = 80.dp),
                    elevation = 4.dp
                )

            }
        ) {
            refresh = rememberSwipeRefreshState(isRefreshing = true)
            Home(coroutineScope, scaffoldState, viewModel)
            refresh =
                rememberSwipeRefreshState(isRefreshing = false)
        }
    }
}

@ExperimentalAnimationApi
@ExperimentalMaterialApi
@Composable
fun Home(
    coroutineScope: CoroutineScope,
    scaffoldState: ScaffoldState,
    viewModel: MiTodoViewModel
) {
    val hideState = viewModel.hideState.collectAsState().value
    val selectedTime by viewModel.snoozeTime.collectAsState()
    val bottomSheetState =
        rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)
    val addTask = remember { mutableStateOf(false) }
    val context = LocalContext.current
    ModalBottomSheetLayout(
        sheetState = bottomSheetState,
        sheetShape = RoundedCornerShape(22.dp),
        scrimColor = MaterialTheme.colors.background,
        sheetBackgroundColor = MaterialTheme.colors.background,
        sheetContent = {
            Column {
                Icon(
                    imageVector = Icons.Filled.DragHandle,
                    contentDescription = "Drag the bottom sheet",
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
                Text(
                    text = "Dark Mode",
                    style = TextStyle(
                        fontFamily = Gothic,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Medium,
                        lineHeight = 24.sp
                    ),
                    modifier = Modifier.padding(
                        top = 2.dp,
                        bottom = 2.dp,
                        start = 10.dp,
                        end = 3.dp
                    ),
                    color = MaterialTheme.colors.secondary
                )
                MiTodoDivider()
                MiTodoRadioThemeGroup(
                    radioOptions = listOf("On", "Off", "Auto"),
                    viewModel.themeState.collectAsState().value,
                    viewModel = viewModel
                )
                ThemeState.selectedTheme = viewModel.themeState.collectAsState().value
                Text(
                    text = "Hide",
                    style = TextStyle(
                        fontFamily = Gothic,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Medium,
                        lineHeight = 24.sp
                    ),
                    modifier = Modifier.padding(
                        top = 8.dp,
                        bottom = 2.dp,
                        start = 10.dp,
                        end = 3.dp
                    ),
                    color = MaterialTheme.colors.secondary
                )
                MiTodoDivider()
                MiTodoRadioHideGroup(
                    radioOptions = listOf("On", "Off"),
                    hideState,
                    viewModel = viewModel
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "Request a feature/Report a bug",
                    style = TextStyle(
                        fontFamily = Gothic,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Medium,
                        lineHeight = 24.sp
                    ),
                    modifier = Modifier.padding(
                        top = 8.dp,
                        bottom = 8.dp,
                        start = 10.dp,
                        end = 3.dp
                    ),
                    softWrap = true,
                    overflow = TextOverflow.Visible,
                    color = MaterialTheme.colors.secondary
                )
                MiTodoDivider()
                Text(
                    text = "Twitter",
                    style = MaterialTheme.typography.h6.copy(
                        fontWeight = FontWeight.Medium,
                        fontFamily = NotoSerif
                    ),
                    color = MaterialTheme.colors.secondary,
                    modifier = Modifier
                        .padding(start = 24.dp, bottom = 10.dp, top = 8.dp)
                        .clickable {
                            openTwitter(context)
                        }
                )
                Text(
                    text = "WhatsApp",
                    style = MaterialTheme.typography.h6.copy(
                        fontWeight = FontWeight.Medium,
                        fontFamily = NotoSerif
                    ),
                    color = MaterialTheme.colors.secondary,
                    modifier = Modifier
                        .padding(start = 24.dp, bottom = 10.dp)
                        .clickable {
                            openWhatsApp(context)
                        }
                )
                Text(
                    text = "Make a phone call",
                    style = MaterialTheme.typography.h6.copy(
                        fontWeight = FontWeight.Medium,
                        fontFamily = NotoSerif
                    ),
                    color = MaterialTheme.colors.secondary,
                    modifier = Modifier
                        .padding(start = 24.dp, bottom = 15.dp)
                        .clickable {
                            makePhoneCall(context)
                        }
                )
            }
        }
    ) {
        MiTodoScaffold(
            topBar = {
                HomeBar(
                    modifier = Modifier,
                    coroutineScope = coroutineScope,
                    bottomSheetState = bottomSheetState,
                    selectedTime = selectedTime,
                    viewModel = viewModel
                )
            },
            content = {
                HomeContent(
                    hideState,
                    scaffoldState = scaffoldState,
                    coroutineScope = coroutineScope
                )
            },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = {
                        addTask.value = !addTask.value
                    },
                    backgroundColor = MaterialTheme.colors.primary,
                    contentColor = Color.White
                ) {
                    Icon(imageVector = Icons.Filled.Add, contentDescription = "Add new Todo")
                    if (addTask.value) {
                        AddTodoItemDialog(addTask, viewModel, null)
                    }
                }
            }
        )
    }
}


@ExperimentalMaterialApi
@Composable
fun HomeBar(
    modifier: Modifier = Modifier,
    coroutineScope: CoroutineScope,
    bottomSheetState: ModalBottomSheetState,
    selectedTime: Int,
    viewModel: MiTodoViewModel
) {
    val showMenu = remember { mutableStateOf(false) }
    Column(
        modifier = modifier
            .statusBarsPadding()
            .fillMaxWidth()
    )
    {
        BoxWithConstraints(modifier = Modifier.fillMaxWidth()) {
            TopAppBar(
                modifier = modifier,
                backgroundColor = MaterialTheme.colors.background,
                contentColor = MaterialTheme.colors.secondary,
                elevation = 0.dp,
                actions = {
                    IconButton(onClick = {
                        coroutineScope.launch { bottomSheetState.show() }
                    }) {
                        Icon(
                            Icons.Outlined.AutoAwesome,
                            tint = MaterialTheme.colors.primary,
                            contentDescription = "Bottom Sheet"
                        )
                    }
                },
                title = {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Text(
                            text = stringResource(id = R.string.app_name),
                            style = MaterialTheme.typography.h5.copy(
                                fontWeight = FontWeight.Medium,
                                fontFamily = NotoSerif,
                                fontSize = 24.sp
                            ),
                            color = MaterialTheme.colors.secondary,
                            textAlign = TextAlign.Center,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier
                                .align(Alignment.CenterHorizontally)
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = {
                        showMenu.value = !showMenu.value
                    }) {
                        Icon(
                            Icons.Outlined.Snooze,
                            tint = MaterialTheme.colors.primary,
                            contentDescription = "Snooze Time Icon",
                        )
                    }
                }
            )
            DropdownNavBar(showMenu, maxWidth, viewModel = viewModel, selectedTime)
        }
        MiTodoDivider()
    }
}

@Composable
fun DropdownNavBar(
    showMenu: MutableState<Boolean>,
    maxWidth: Dp,
    viewModel: MiTodoViewModel,
    selectedTime: Int
) {
    val (_, onOptionSelected) = remember {
        mutableStateOf(selectedTime)
    }
    MaterialTheme(
        shapes = MaterialTheme.shapes.copy(
            medium = RoundedCornerShape(
                6.dp
            )
        )
    ) {
        DropdownMenu(
            modifier = Modifier
                .background(
                    color = MaterialTheme.colors.onPrimary
                )
                .width((maxWidth * 0.55f)),
            expanded = showMenu.value,
            onDismissRequest = { showMenu.value = false },
        ) {
            for (min in 5..50 step 5) {
                DropdownMenuItem(onClick = {
                    viewModel.updateSnoozeTime(min)
                    showMenu.value = false
                }) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .padding(horizontal = 4.dp)
                            .fillMaxWidth()
                            .selectable(
                                selected = (min == selectedTime),
                                onClick = {
                                    onOptionSelected(min)
                                    viewModel.updateSnoozeTime(min)
                                }
                            ),
                    ) {
                        RadioButton(
                            selected = (min == selectedTime),
                            onClick = {
                                onOptionSelected(min)
                                viewModel.updateSnoozeTime(min)
                            },
                            colors = RadioButtonDefaults.colors(
                                selectedColor = MaterialTheme.colors.primary,
                                unselectedColor = MaterialTheme.colors.secondary
                            )
                        )
                        Text(
                            text = "$min minutes",
                            style = MaterialTheme.typography.h6,
                            modifier = Modifier.padding(start = 2.dp),
                            color = MaterialTheme.colors.secondary
                        )
                    }
                }
            }
        }
    }
}

@ExperimentalAnimationApi
@Composable
fun HomeContent(
    hide: String,
    scaffoldState: ScaffoldState,
    coroutineScope: CoroutineScope
) {
    val viewModel: MiTodoViewModel = viewModel()
    val categories by viewModel.categories.collectAsState()
    val todos by viewModel.todos.collectAsState()
    val selectedCategory by viewModel.selectedCategory.collectAsState()
    val context = LocalContext.current
    val taskList: MutableState<Map<MealType?, List<TodoMinimal>>>? = null
    Column(
        modifier = Modifier
            .padding(2.dp)
            .fillMaxSize()
    )
    {
        if (todos.isNotEmpty()) {
            FilterBar(
                categoriesFilters = categories,
                onFilterSelected = viewModel::onFilterSelected,
                selectedFilter = selectedCategory
            )
            Spacer(modifier = Modifier.height(4.dp))
            val lazyListState = rememberLazyListState()
            LazyColumn(
                state = lazyListState,
                modifier = Modifier
                    .padding(
                        top = dimensionResource(id = R.dimen.list_top_padding)
                    )
            ) {
                item {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        viewModel.todaysTaskList?.let {
                            TasksPieChart(
                                todaysTaskList = it
                            )
                        }
                    }
                    todos.forEach { (title, todosList) ->
                        if (todosList.isNotEmpty()) {
                            Text(
                                text = title,
                                style = TextStyle(
                                    fontFamily = Gothic,
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    lineHeight = 24.sp
                                ),
                                modifier = Modifier.padding(
                                    top = 2.dp,
                                    bottom = 1.dp,
                                    start = 4.dp,
                                    end = 3.dp
                                )
                            )
                            if (selectedCategory == "Meals") {
                                formatMeal(
                                    todoList = todosList,
                                    selectedCategory = selectedCategory,
                                    hide = hide,
                                    coroutineScope = coroutineScope,
                                    context = context,
                                    scaffoldState = scaffoldState,
                                    viewModel = viewModel
                                )
                            } else {
                                TasksItem(
                                    todosList = todosList,
                                    selectedCategory = selectedCategory,
                                    hide = hide,
                                    coroutineScope = coroutineScope,
                                    context = context,
                                    scaffoldState = scaffoldState,
                                    viewModel = viewModel
                                )
                            }
                        }
                    }
                }
            }
        } else {
            MiTodoSurface(modifier = Modifier.fillMaxSize()) {
                Box {
                    Column(
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier
                            .fillMaxSize()
                            .wrapContentSize()
                            .padding(24.dp)
                    ) {
                        Text(
                            text = "No Task yet",
                            style = TextStyle(
                                fontFamily = Gothic,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Medium,
                                lineHeight = 24.sp
                            ),
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun formatMeal(
    todoList: List<TodoMinimal>,
    selectedCategory: String,
    hide: String,
    coroutineScope: CoroutineScope,
    context: Context,
    scaffoldState: ScaffoldState,
    viewModel: MiTodoViewModel
) {
    val taskList = todoList.groupBy {
        it.type
    }
    taskList.forEach { (mealType, meal) ->
        mealType?.let {
            Text(
                text = MealTypeConvertor(it),
                style = TextStyle(
                    fontFamily = Gothic,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Medium,
                    lineHeight = 24.sp
                ),
                modifier = Modifier.padding(
                    top = 2.dp,
                    bottom = 1.dp,
                    start = 8.dp,
                    end = 3.dp
                )
            )
        }
        TasksItem(
            todosList = meal,
            selectedCategory = selectedCategory,
            hide = hide,
            coroutineScope = coroutineScope,
            context = context,
            scaffoldState = scaffoldState,
            viewModel = viewModel,
        )
    }
}

fun MealTypeConvertor(
    mealType: MealType
) =
    if (mealType.name == MealType.BREAKFAST.name) "Breakfast" else if (mealType.name == MealType.LUNCH.name) "Lunch" else "Supper"

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun TasksItem(
    todosList: List<TodoMinimal>,
    selectedCategory: String,
    hide: String,
    coroutineScope: CoroutineScope,
    context: Context,
    scaffoldState: ScaffoldState,
    viewModel: MiTodoViewModel,
) {
    MiTodoSurface(
        shape = CutCornerShape(4.dp),
        elevation = 8.dp,
        contentColor = MaterialTheme.colors.secondary,
        color = MaterialTheme.colors.onBackground,
        modifier = Modifier
            .padding(
                top = 2.dp,
                start = 6.dp,
                end = 3.dp,
                bottom = 10.dp
            )
            .fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = MaterialTheme.colors.onBackground),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            todosList.forEachIndexed { index, todo ->
                key(todo) {
                    if (selectedCategory != "All") {
                        if (hide == "On") {
                            if (!todo.hide) {
                                if (selectedCategory == todo.category || selectedCategory == "Meals") {
                                    TodoCard(
                                        todo = todo,
                                        onDeleted = {
                                            deleteTodo(
                                                todo = todo,
                                                viewModel = viewModel,
                                                coroutineScope = coroutineScope,
                                                context = context,
                                                scaffoldState = scaffoldState
                                            )
                                        },
                                        index = index,
                                        viewModel = viewModel,
                                        context = context
                                    )
                                }
                            }
                        } else {
                            if (selectedCategory == todo.category || selectedCategory == "Meals") {
                                TodoCard(
                                    todo = todo,
                                    onDeleted = {
                                        deleteTodo(
                                            todo = todo,
                                            viewModel = viewModel,
                                            coroutineScope = coroutineScope,
                                            context = context,
                                            scaffoldState = scaffoldState
                                        )
                                    },
                                    index = index,
                                    viewModel = viewModel,
                                    context = context
                                )
                            }
                        }
                    } else {
                        if (hide == "On") {
                            if (!todo.hide) {
                                TodoCard(
                                    todo = todo,
                                    onDeleted = {
                                        deleteTodo(
                                            todo = todo,
                                            viewModel = viewModel,
                                            coroutineScope = coroutineScope,
                                            context = context,
                                            scaffoldState = scaffoldState
                                        )
                                    },
                                    index = index,
                                    viewModel = viewModel,
                                    context = context
                                )
                            }
                        } else {
                            TodoCard(
                                todo = todo,
                                onDeleted = {
                                    deleteTodo(
                                        todo = todo,
                                        viewModel = viewModel,
                                        coroutineScope = coroutineScope,
                                        context = context,
                                        scaffoldState = scaffoldState
                                    )
                                },
                                index = index,
                                viewModel = viewModel,
                                context = context
                            )
                        }
                    }
                }
                if (index != todosList.lastIndex) {
                    MiTodoDivider()
                }
            }
        }
    }
}

@ExperimentalAnimationApi
@Composable
fun TodoCard(
    todo: TodoMinimal,
    onDeleted: () -> Unit,
    index: Int,
    viewModel: MiTodoViewModel,
    context: Context
) {
    val showMore = remember { mutableStateOf(false) }
    val circleColor =
        if (index % 2 == 0) brand else if (index % 2 == 1) twitterBlue else if (index % 3 == 2) lBlue else lRed
    BoxWithConstraints {
        val maxWidth = maxWidth
        Column(
            modifier = Modifier
                .background(
                    color = MaterialTheme.colors.onBackground,
                )
                .wrapContentHeight()
                .fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .background(
                        color = MaterialTheme.colors.onBackground,
                    )
                    .fillMaxWidth()
                    .padding(start = 4.dp, top = 2.dp, end = 2.dp, bottom = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .width(maxWidth * 0.75f)
                        .heightIn(max = 60.dp)
                ) {
                    RadioButton(
                        selected = todo.completed,
                        onClick = {
                            viewModel.completedTodo(true, todo.id)
                        },
                        colors = RadioButtonDefaults.colors(
                            selectedColor = circleColor,
                            unselectedColor = circleColor
                        ),
                    )
                    LazyColumn {
                        item {
                            Text(
                                text = todo.name,
                                softWrap = true,
                                overflow = TextOverflow.Ellipsis,
                                maxLines = if (showMore.value) Int.MAX_VALUE else 2,
                                style = MaterialTheme.typography.h6,
                                textDecoration = if (todo.completed) TextDecoration.LineThrough else TextDecoration.None,
                            )
                        }
                    }
                }
                Column {
                    Row(
                        modifier = Modifier.align(
                            Alignment.End
                        ),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = todo.time,
                            style = MaterialTheme.typography.subtitle1,
                            modifier = Modifier
                                .padding(start = 5.dp),
                            textDecoration = if (todo.completed) TextDecoration.LineThrough else TextDecoration.None,
                        )
                        Icon(
                            if (showMore.value) Icons.Filled.KeyboardArrowDown else Icons.Filled.NavigateNext,
                            contentDescription = null,
                            tint = MaterialTheme.colors.secondary,
                            modifier = Modifier
                                .clickable {
                                    showMore.value = !showMore.value
                                }
                                .size(28.dp)
                        )
                    }
                }
            }
            if (showMore.value) {
                val edit = remember {
                    mutableStateOf(false)
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextButton(onClick = {
                        edit.value = !edit.value
                    }) {
                        Text(text = "Edit", color = textButtonColor)
                        if (edit.value) {
                            AddTodoItemDialog(edit, viewModel, todo)
                        }
                    }
                    MiTodoDivider(
                        modifier = Modifier
                            .height(40.dp)
                            .width(1.dp)
                            .padding(vertical = 4.dp)
                    )
                    TextButton(onClick = { viewModel.snoozeTodo(todo, context = context) }) {
                        Text(text = "Snooze", color = textButtonColor)
                    }
                    MiTodoDivider(
                        modifier = Modifier
                            .height(40.dp)
                            .width(1.dp)
                            .padding(vertical = 4.dp)
                    )
                    TextButton(onClick = onDeleted) {
                        Text(text = "Delete", color = textButtonColor)
                    }
                }
            }
        }
    }
}


@Composable
fun MiTodoRadioHideGroup(
    radioOptions: List<String>,
    selected: String,
    viewModel: MiTodoViewModel
) {
    val (_, onOptionSelected) = remember {
        mutableStateOf(selected)
    }
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column {
            radioOptions.forEach { text ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .padding(horizontal = 10.dp)
                        .fillMaxWidth()
                        .selectable(
                            selected = (text == selected),
                            onClick = {
                                onOptionSelected(text)
                                viewModel.updateSetting("Hide", text)
                            }
                        ),
                ) {
                    RadioButton(
                        selected = (text == selected),
                        onClick = {
                            onOptionSelected(text)
                            viewModel.updateSetting("Hide", text)
                        },
                        colors = RadioButtonDefaults.colors(
                            selectedColor = MaterialTheme.colors.primary,
                            unselectedColor = MaterialTheme.colors.primaryVariant
                        )
                    )
                    Text(
                        text = text,
                        style = MaterialTheme.typography.h6,
                        modifier = Modifier.padding(start = 2.dp),
                        color = MaterialTheme.colors.secondary
                    )
                }
            }
        }
    }
}

@Composable
fun MiTodoRadioThemeGroup(
    radioOptions: List<String>,
    selected: String,
    viewModel: MiTodoViewModel
) {
    val (_, onOptionSelected) = remember {
        mutableStateOf(selected)
    }
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column {
            radioOptions.forEach { text ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .padding(horizontal = 10.dp)
                        .fillMaxWidth()
                        .selectable(
                            selected = (text == selected),
                            onClick = {
                                onOptionSelected(text)
                                viewModel.updateSetting("Theme", text)
                            }
                        ),
                ) {
                    RadioButton(
                        selected = (text == selected),
                        onClick = {
                            onOptionSelected(text)
                            viewModel.updateSetting("Theme", text)
                        },
                        colors = RadioButtonDefaults.colors(
                            selectedColor = MaterialTheme.colors.primary,
                            unselectedColor = MaterialTheme.colors.primaryVariant
                        )
                    )
                    Text(
                        text = text,
                        style = MaterialTheme.typography.h6,
                        modifier = Modifier.padding(start = 2.dp),
                        color = MaterialTheme.colors.secondary
                    )
                }
            }
        }
    }
}

fun setAlarm(
    date: LocalDate,
    time: LocalTime,
    context: Context,
    category: String,
    todo: String,
    notificationID: Int
) {
    val alarm = LocalDateTime.of(date, time)
    val zoneId = ZoneId.systemDefault()
    val zoneDateTime = ZonedDateTime.of(alarm, zoneId)
    val intent = Intent(context, AlarmReceiver::class.java)
    intent.data = Uri.parse("timer:$notificationID")
    intent.putExtra("Time", DateTimeTypeConverters.fromLocalTime(time))
    intent.putExtra("Date", DateTimeTypeConverters.fromLocalDate(date))
    intent.putExtra("Todo", todo)
    intent.putExtra("Category", category)
    intent.putExtra("NotificationID", notificationID)
    val pendingIntent: PendingIntent = PendingIntent.getBroadcast(
        context, 0, intent,
        0
    )
    val alarmManager: AlarmManager =
        context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    if (LocalDateTime.now() <= alarm) {
        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            zoneDateTime.toInstant().toEpochMilli() - 1200000,
            300000,
            pendingIntent
        )
    } else {
        alarmManager.cancel(pendingIntent)
    }
}

fun deleteTodo(
    todo: TodoMinimal,
    viewModel: MiTodoViewModel,
    coroutineScope: CoroutineScope,
    scaffoldState: ScaffoldState,
    context: Context
) {
    viewModel.deleteTodo(todo.id)
    coroutineScope.launch {
        val snackbarResult =
            scaffoldState.snackbarHostState.showSnackbar(
                message = "Task deleted ",
                actionLabel = "Undo"
            )
        if (snackbarResult == SnackbarResult.ActionPerformed) {
            viewModel.insertTodo(
                categoryName = todo.category,
                todo = todo.name,
                date = todo.date,
                time = todo.time,
                repeat = todo.repeat,
                hide = todo.hide,
                delete = todo.delete,
                context = context,
                deleteTodo = null
            )
        }
    }

}  

