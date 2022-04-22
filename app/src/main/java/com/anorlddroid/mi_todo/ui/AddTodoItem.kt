package com.anorlddroid.mi_todo.ui

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.anorlddroid.mi_todo.data.database.DateTimeTypeConverters
import com.anorlddroid.mi_todo.data.database.MealType
import com.anorlddroid.mi_todo.data.database.TodoMinimal
import com.anorlddroid.mi_todo.ui.components.MiTodoSurface
import com.anorlddroid.mi_todo.ui.theme.Gothic
import com.anorlddroid.mi_todo.ui.theme.brand
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.datetime.date.DatePickerDefaults
import com.vanpra.composematerialdialogs.datetime.date.datepicker
import com.vanpra.composematerialdialogs.datetime.time.TimePickerDefaults
import com.vanpra.composematerialdialogs.datetime.time.timepicker
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import java.time.LocalDate
import java.time.LocalTime


@Composable
fun AddTodoItemDialog(
    addTask: MutableState<Boolean>,
    viewModel: MiTodoViewModel,
    todoMinimal: TodoMinimal?
) {
    val showDialog = remember { mutableStateOf(true) }
    var todo by if (todoMinimal == null) remember { mutableStateOf("") } else remember {
        mutableStateOf(
            todoMinimal.name
        )
    }
    val selectedDate =
        if (todoMinimal == null) remember { mutableStateOf(LocalDate.now()) } else remember {
            mutableStateOf(DateTimeTypeConverters.toLocalDate(todoMinimal.date))
        }
    val selectedTime =
        if (todoMinimal == null) remember { mutableStateOf(LocalTime.now()) } else remember {
            mutableStateOf(DateTimeTypeConverters.toLocalTime(todoMinimal.time))
        }
    val maxLength by remember {
        mutableStateOf(100)
    }
    var nextDialog by remember { mutableStateOf(false) }
    var showNextDialog by remember { mutableStateOf(false) }
    if (showDialog.value) {
        Dialog(
            onDismissRequest = { },
            properties = DialogProperties(dismissOnBackPress = true, dismissOnClickOutside = false)
        ) {
            MiTodoSurface(modifier = Modifier.fillMaxWidth(), elevation = 4.dp) {
                LazyColumn {
                    item {
                        Column(modifier = Modifier.fillMaxWidth()) {
                            Text(
                                text = "Task",
                                color = MaterialTheme.colors.secondary,
                                style = MaterialTheme.typography.h6.copy(fontWeight = FontWeight.SemiBold),
                                modifier = Modifier
                                    .align(
                                        Alignment.Start
                                    )
                                    .padding(12.dp)
                            )

                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                TextField(
                                    value = todo,
                                    onValueChange = {
                                        if (it.length <= maxLength) todo = it
                                    },
                                    maxLines = 5,
                                    modifier = Modifier
                                        .padding(top = 2.dp, bottom = 2.dp)
                                        .fillMaxWidth(),
                                    textStyle = MaterialTheme.typography.h6,
                                    colors = TextFieldDefaults.textFieldColors(
                                        trailingIconColor = MaterialTheme.colors.primary,
                                        backgroundColor = MaterialTheme.colors.onBackground,
                                        cursorColor = MaterialTheme.colors.primary,
                                        disabledLabelColor = MaterialTheme.colors.background,
                                        focusedIndicatorColor = MaterialTheme.colors.primary,
                                        unfocusedIndicatorColor = MaterialTheme.colors.primary
                                    ),
                                    shape = CutCornerShape(4.dp),
                                    trailingIcon = {
                                        if (todo.isNotEmpty()) {
                                            Icon(
                                                imageVector = Icons.Filled.Cancel,
                                                tint = MaterialTheme.colors.primary,
                                                contentDescription = "Clear Text",
                                                modifier = Modifier.clickable { todo = "" }
                                            )
                                        }
                                    },
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
                                )
                                Row(
                                    modifier = Modifier
                                        .padding(end = 12.dp)
                                        .align(Alignment.End)
                                ) {
                                    Text(
                                        text = "${todo.length}/$maxLength",
                                        style = MaterialTheme.typography.caption,
                                        textAlign = TextAlign.End
                                    )
                                }
                            }
                            Row(
                                modifier = Modifier.align(Alignment.End),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                TextButton(onClick = { showDialog.value = false }) {
                                    Text(text = "Discard")
                                }
                                TextButton(onClick = {
                                    nextDialog = true
                                    showDialog.value = false
                                }) {
                                    Text(text = "Next")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    if (nextDialog) {
        //time and date variable states
        val dateDialog = rememberMaterialDialogState()
        val timeDialog = rememberMaterialDialogState()
        Dialog(onDismissRequest = { }) {
            MiTodoSurface(modifier = Modifier.fillMaxWidth(), elevation = 4.dp) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "Date and Time",
                        color = MaterialTheme.colors.secondary,
                        style = MaterialTheme.typography.h6.copy(
                            fontWeight = FontWeight.SemiBold
                        ),
                        modifier = Modifier
                            .align(
                                Alignment.Start
                            )
                            .padding(12.dp)
                    )
                    TextField(
                        value = DateTimeTypeConverters.fromLocalDate(selectedDate.value),
                        onValueChange = {
                            dateDialog.show()
                        },
                        maxLines = 2,
                        modifier = Modifier
                            .padding(top = 20.dp, bottom = 20.dp)
                            .fillMaxWidth(),
                        textStyle = MaterialTheme.typography.h6,
                        colors = TextFieldDefaults.textFieldColors(
                            trailingIconColor = MaterialTheme.colors.primary,
                            backgroundColor = MaterialTheme.colors.onSurface,
                            cursorColor = MaterialTheme.colors.primary,
                            disabledLabelColor = MaterialTheme.colors.background,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent
                        ),
                        trailingIcon = {
                            Icon(
                                imageVector = Icons.Filled.DateRange,
                                tint = MaterialTheme.colors.primary,
                                contentDescription = "Calender picker",
                                modifier = Modifier
                                    .padding(start = 4.dp, end = 8.dp)
                                    .clickable {
                                        dateDialog.show()
                                    }
                            )
                        },
                        shape = RoundedCornerShape(16.dp),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
                    )
                    MaterialDialog(
                        dialogState = dateDialog,
                        backgroundColor = MaterialTheme.colors.background,
                        shape = MaterialTheme.shapes.medium,
                        buttons = {
                            positiveButton("Ok")
                            negativeButton("Cancel")
                        }
                    ) {
                        datepicker(
                            initialDate = selectedDate.value,
                            colors = DatePickerDefaults.colors(
                                headerBackgroundColor = brand,
                                headerTextColor = MaterialTheme.colors.secondary,
                                calendarHeaderTextColor = MaterialTheme.colors.secondary,
                                dateInactiveTextColor = MaterialTheme.colors.secondary,
                                dateInactiveBackgroundColor = MaterialTheme.colors.onBackground,
                            ),
                        ) { date ->
                            selectedDate.value = date
                        }
                    }
                    TextField(
                        value = DateTimeTypeConverters.fromLocalTime(selectedTime.value),
                        onValueChange = {
                            timeDialog.show()
                        },
                        maxLines = 2,
                        modifier = Modifier
                            .padding(top = 5.dp, bottom = 20.dp)
                            .fillMaxWidth(),
                        textStyle = MaterialTheme.typography.h6,
                        colors = TextFieldDefaults.textFieldColors(
                            trailingIconColor = MaterialTheme.colors.primary,
                            backgroundColor = MaterialTheme.colors.onSurface,
                            cursorColor = MaterialTheme.colors.primary,
                            disabledLabelColor = MaterialTheme.colors.background,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent
                        ),
                        trailingIcon = {
                            Icon(
                                imageVector = Icons.Filled.HistoryToggleOff,
                                tint = MaterialTheme.colors.primary,
                                contentDescription = "Time picker",
                                modifier = Modifier
                                    .padding(end = 8.dp)
                                    .clickable {
                                        timeDialog.show()
                                    }
                            )
                        },
                        shape = RoundedCornerShape(16.dp),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
                    )
                    MaterialDialog(
                        dialogState = timeDialog,
                        backgroundColor = MaterialTheme.colors.background,
                        shape = MaterialTheme.shapes.medium,
                        buttons = {
                            positiveButton("Ok")
                            negativeButton("Cancel")
                        }
                    ) {
                        timepicker(
                            initialTime = selectedTime.value,
                            colors = TimePickerDefaults.colors(
                                activeBackgroundColor = brand,
                                inactiveTextColor = MaterialTheme.colors.secondary,
                                inactiveBackgroundColor = MaterialTheme.colors.onBackground,
                                inactivePeriodBackground = MaterialTheme.colors.onBackground,
                            )
                        ) { time ->
                            selectedTime.value = time
                        }
                    }
                    Row(
                        modifier = Modifier.align(Alignment.End),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        TextButton(onClick = { nextDialog = false }) {
                            Text(text = "Discard")
                        }
                        TextButton(onClick = {
                            showNextDialog = true
                            nextDialog = false
                        }) {
                            Text(text = "Next")
                        }
                    }
                }
            }
        }
    }
    if (showNextDialog) {
        val context = LocalContext.current
        //Category
        val type: String = todoMinimal?.type?.name ?: ""
        var category by if (todoMinimal == null) remember { mutableStateOf("") } else if (todoMinimal.category == "Meal")
            remember {
                mutableStateOf(
                    type
                )

            } else remember { mutableStateOf(todoMinimal.category) }

        var showCategory by remember { mutableStateOf(false) }
        val categories by viewModel.categories.collectAsState()

        //filters
        var filterType by if (todoMinimal == null) remember { mutableStateOf("") } else if (todoMinimal.category == "Meal") remember {
            mutableStateOf(
                "Meals"
            )
        }
        else remember { mutableStateOf("Categories") }
        var showFilterTypes by remember { mutableStateOf(false) }
        val filterTypes = mutableListOf("Meals", "Categories")


        //repeat
        var repeat by if (todoMinimal == null) remember { mutableStateOf("Daily") } else remember {
            mutableStateOf(
                todoMinimal.repeat
            )
        }
        var showRepeat by remember { mutableStateOf(false) }
        val listOfRepeats = mutableListOf("Never", "Daily", "Weekly", "Monthly", "Yearly")

        //checkbox
        val hideTodo = if (todoMinimal == null) remember { mutableStateOf(false) } else remember {
            mutableStateOf(todoMinimal.hide)
        }
        val deleteTodoWhenDone =
            if (todoMinimal == null) remember { mutableStateOf(false) } else remember {
                mutableStateOf(todoMinimal.delete)
            }
        Dialog(onDismissRequest = { /*TODO*/ }) {
            MiTodoSurface(modifier = Modifier.fillMaxWidth(), elevation = 4.dp) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "Configure your task",
                        color = MaterialTheme.colors.secondary,
                        style = MaterialTheme.typography.h6.copy(fontWeight = FontWeight.SemiBold),
                        modifier = Modifier
                            .align(
                                Alignment.Start
                            )
                            .padding(12.dp)
                    )
                    Spacer(modifier = Modifier.height(25.dp))
                    BoxWithConstraints {
                        val maxWidth = maxWidth
                        Column {
                            Text(
                                text = "Repeat",
                                style = TextStyle(
                                    fontFamily = Gothic,
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Medium,
                                    lineHeight = 22.sp
                                ),
                                color = MaterialTheme.colors.secondary,
                                modifier = Modifier.padding(horizontal = 16.dp)
                            )
                            Box {
                                TextField(
                                    value = repeat,
                                    onValueChange = {
                                        showRepeat = true
                                    },
                                    maxLines = 2,
                                    modifier = Modifier
                                        .padding(start = 16.dp, end = 8.dp, top = 12.dp)
                                        .width(maxWidth),
                                    textStyle = MaterialTheme.typography.h6,
                                    colors = TextFieldDefaults.textFieldColors(
                                        trailingIconColor = MaterialTheme.colors.primary,
                                        backgroundColor = MaterialTheme.colors.onBackground,
                                        cursorColor = MaterialTheme.colors.primary,
                                        disabledLabelColor = MaterialTheme.colors.background,
                                        focusedIndicatorColor = MaterialTheme.colors.primary,
                                        unfocusedIndicatorColor = Color.Transparent
                                    ),
                                    trailingIcon = {
                                        Icon(
                                            imageVector = if (showRepeat) Icons.Filled.ArrowDropUp else Icons.Filled.ArrowDropDown,
                                            tint = MaterialTheme.colors.primary,
                                            contentDescription = null,
                                            modifier = Modifier
                                                .padding(end = 8.dp)
                                                .clickable {
                                                    showRepeat = true
                                                }
                                        )
                                    },
                                    shape = CutCornerShape(4.dp),
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
                                )
                                MaterialTheme(
                                    shapes = MaterialTheme.shapes.copy(
                                        medium = RoundedCornerShape(
                                            16.dp
                                        )
                                    )
                                ) {
                                    DropdownMenu(
                                        modifier = Modifier
                                            .background(
                                                color = MaterialTheme.colors.onPrimary
                                            )
                                            .width(maxWidth)
                                            .padding(start = 16.dp, end = 8.dp)
                                            .height(220.dp),
                                        expanded = showRepeat,
                                        onDismissRequest = { showRepeat = false },

                                        ) {
                                        listOfRepeats.forEach { listRepeat ->
                                            DropdownMenuItem(onClick = {
                                                showRepeat = false
                                                repeat = listRepeat
                                            }) {
                                                Text(
                                                    text = listRepeat,
                                                    style = MaterialTheme.typography.h6,
                                                    color = MaterialTheme.colors.secondary
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(18.dp))
                    BoxWithConstraints {
                        val maxWidth = maxWidth
                        Column {
                            Text(
                                text = "Filter Type",
                                style = TextStyle(
                                    fontFamily = Gothic,
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Medium,
                                    lineHeight = 22.sp
                                ),
                                color = MaterialTheme.colors.secondary,
                                modifier = Modifier.padding(horizontal = 16.dp)
                            )
                            Box {
                                TextField(
                                    value = filterType,
                                    onValueChange = {
                                        filterType = it
                                    },
                                    maxLines = 2,
                                    modifier = Modifier
                                        .padding(start = 18.dp, end = 12.dp, top = 12.dp)
                                        .width(maxWidth),
                                    textStyle = MaterialTheme.typography.h6,
                                    colors = TextFieldDefaults.textFieldColors(
                                        trailingIconColor = MaterialTheme.colors.primary,
                                        backgroundColor = MaterialTheme.colors.onBackground,
                                        cursorColor = MaterialTheme.colors.primary,
                                        disabledLabelColor = MaterialTheme.colors.background,
                                        focusedIndicatorColor = MaterialTheme.colors.primary,
                                        unfocusedIndicatorColor = Color.Transparent
                                    ),
                                    trailingIcon = {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Icon(
                                                imageVector = if (showFilterTypes) Icons.Filled.ArrowDropUp else Icons.Filled.ArrowDropDown,
                                                tint = MaterialTheme.colors.primary,
                                                contentDescription = null,
                                                modifier = Modifier
                                                    .padding(end = 8.dp)
                                                    .clickable {
                                                        showFilterTypes =
                                                            filterTypes.isNotEmpty()
                                                    }
                                            )
                                        }
                                    },
                                    shape = CutCornerShape(4.dp),
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
                                )
                                MaterialTheme(
                                    shapes = MaterialTheme.shapes.copy(
                                        medium = RoundedCornerShape(
                                            16.dp
                                        )
                                    )
                                ) {
                                    DropdownMenu(
                                        modifier = Modifier
                                            .background(
                                                color = MaterialTheme.colors.onPrimary
                                            )
                                            .width(maxWidth)
                                            .padding(start = 16.dp, end = 8.dp)
                                            .height(120.dp),
                                        expanded = showFilterTypes,
                                        onDismissRequest = { showFilterTypes = false },

                                        ) {
                                        filterTypes.forEach { filter ->
                                            DropdownMenuItem(onClick = {
                                                showFilterTypes = false
                                                filterType = filter
                                            }) {
                                                Text(
                                                    text = filter,
                                                    style = MaterialTheme.typography.h6,
                                                    color = MaterialTheme.colors.secondary
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                    BoxWithConstraints {
                        val maxWidth = maxWidth
                        Column {
                            Box {
                                TextField(
                                    value = category,
                                    onValueChange = {
                                        category = it
                                    },
                                    maxLines = 2,
                                    modifier = Modifier
                                        .padding(start = 18.dp, end = 12.dp, top = 12.dp)
                                        .width(maxWidth),
                                    textStyle = MaterialTheme.typography.h6,
                                    colors = TextFieldDefaults.textFieldColors(
                                        trailingIconColor = MaterialTheme.colors.primary,
                                        backgroundColor = MaterialTheme.colors.onBackground,
                                        cursorColor = MaterialTheme.colors.primary,
                                        disabledLabelColor = MaterialTheme.colors.background,
                                        focusedIndicatorColor = MaterialTheme.colors.primary,
                                        unfocusedIndicatorColor = Color.Transparent
                                    ),
                                    trailingIcon = {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            if (filterType != "Meals") {
                                                Icon(
                                                    imageVector = Icons.Filled.LibraryAdd,
                                                    contentDescription = "Adding new Categories",
                                                    tint = MaterialTheme.colors.primary,
                                                    modifier = Modifier.clickable {
                                                        if (categories.isNotEmpty()) {
                                                            if (categories.contains(category)) {
                                                                Toast.makeText(
                                                                    context,
                                                                    "Category Exists",
                                                                    Toast.LENGTH_SHORT
                                                                ).show()
                                                            } else {
                                                                viewModel.insertCategory(
                                                                    category,
                                                                    context
                                                                )
                                                            }
                                                        }
                                                    }
                                                )
                                            }
                                            Spacer(modifier = Modifier.width(4.dp))
                                            Icon(
                                                imageVector = if (showCategory) Icons.Filled.ArrowDropUp else Icons.Filled.ArrowDropDown,
                                                tint = MaterialTheme.colors.primary,
                                                contentDescription = null,
                                                modifier = Modifier
                                                    .padding(end = 8.dp)
                                                    .clickable {
                                                        showCategory = categories.isNotEmpty()
                                                    }
                                            )
                                        }
                                    },
                                    shape = CutCornerShape(4.dp),
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
                                )
                                MaterialTheme(
                                    shapes = MaterialTheme.shapes.copy(
                                        medium = RoundedCornerShape(
                                            16.dp
                                        )
                                    )
                                ) {
                                    DropdownMenu(
                                        modifier = Modifier
                                            .background(
                                                color = MaterialTheme.colors.onPrimary
                                            )
                                            .width(maxWidth)
                                            .height(120.dp),
                                        expanded = showCategory,
                                        onDismissRequest = { showCategory = false },
                                    ) {
                                        if (filterType == "Meals") {
                                            DropdownMenuItem(onClick = {
                                                showCategory = false
                                                category = MealType.BREAKFAST.name
                                            }) {
                                                Text(
                                                    text = MealType.BREAKFAST.name,
                                                    style = MaterialTheme.typography.h6,
                                                    color = MaterialTheme.colors.secondary
                                                )
                                            }
                                            DropdownMenuItem(onClick = {
                                                showCategory = false
                                                category = MealType.LUNCH.name
                                            }) {
                                                Text(
                                                    text = MealType.LUNCH.name,
                                                    style = MaterialTheme.typography.h6,
                                                    color = MaterialTheme.colors.secondary
                                                )
                                            }
                                            DropdownMenuItem(onClick = {
                                                showCategory = false
                                                category = MealType.SUPPER.name
                                            }) {
                                                Text(
                                                    text = MealType.SUPPER.name,
                                                    style = MaterialTheme.typography.h6,
                                                    color = MaterialTheme.colors.secondary
                                                )
                                            }
                                        } else {
                                            categories.subList(1, categories.lastIndex + 1)
                                                .forEach { listCategory ->
                                                    DropdownMenuItem(onClick = {
                                                        showCategory = false
                                                        category = listCategory
                                                    }) {
                                                        Text(
                                                            text = listCategory,
                                                            style = MaterialTheme.typography.h6,
                                                            color = MaterialTheme.colors.secondary
                                                        )
                                                    }
                                                }
                                        }
                                    }
                                }
                            }
                        }
                    }
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .background(
                                color = MaterialTheme.colors.onSurface
                            )
                            .fillMaxWidth()
                            .padding(top = 20.dp, end = 30.dp)
                    ) {
                        Checkbox(
                            checked = hideTodo.value,
                            onCheckedChange = {
                                hideTodo.value = it
                            },
                            colors = CheckboxDefaults.colors(
                                checkedColor = Color(0xFF0E3057),
                                uncheckedColor = MaterialTheme.colors.secondary
                            )
                        )
                        Text(
                            text = "Hide",
                            style = MaterialTheme.typography.h6,
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Checkbox(
                            checked = deleteTodoWhenDone.value,
                            onCheckedChange = {
                                deleteTodoWhenDone.value = it
                            },
                            colors = CheckboxDefaults.colors(
                                checkedColor = Color(0xFF0E3057),
                                uncheckedColor = MaterialTheme.colors.secondary
                            )
                        )
                        Text(
                            text = "Delete when done",
                            style = MaterialTheme.typography.h6,
                        )
                    }
                    Row(
                        modifier = Modifier.align(Alignment.End),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        TextButton(onClick = {
                            showNextDialog = false
                            addTask.value = false
                        }) {
                            Text(text = "Discard")
                        }
                        TextButton(onClick = {
                            if (todoValidation(
                                    todo = todo,
                                    category = category,
                                    date = selectedDate.value.toString(),
                                    time = selectedTime.value.toString(),
                                    repeat = repeat,
                                    context = context
                                )
                            ) {
                                viewModel.insertTodo(
                                    categoryName = if (filterType == "Meals") "Meal" else category,
                                    todo = todo,
                                    date = DateTimeTypeConverters.fromLocalDate(selectedDate.value),
                                    time = DateTimeTypeConverters.fromLocalTime(selectedTime.value),
                                    repeat = repeat,
                                    hide = hideTodo.value,
                                    delete = deleteTodoWhenDone.value,
                                    context = context,
                                    type = if (filterType == "Meals") toMealType(category) else null,
                                    deleteTodo = todoMinimal
                                )
                            }
                            showNextDialog = false
                            addTask.value = false
                        }) {
                            Text(text = "Save")
                        }
                    }
                }
            }
        }
    }
}

fun todoValidation(
    todo: String,
    category: String,
    date: String,
    time: String,
    repeat: String,
    context: Context
): Boolean {
    if (todo.isEmpty()) {
        Toast.makeText(
            context,
            "Todo cannot be empty",
            Toast.LENGTH_SHORT
        ).show()
        return false
    }
    if (category.isEmpty()) {
        Toast.makeText(
            context,
            "Category cannot be empty",
            Toast.LENGTH_SHORT
        ).show()
        return false
    }
    if (date.isEmpty()) {
        Toast.makeText(
            context,
            "Date cannot be empty",
            Toast.LENGTH_SHORT
        ).show()
        return false
    }
    if (time.isEmpty()) {
        Toast.makeText(
            context,
            "Time cannot be empty",
            Toast.LENGTH_SHORT
        ).show()
        return false
    }
    if (repeat.isEmpty()) {
        Toast.makeText(
            context,
            "Repeat cannot be empty",
            Toast.LENGTH_SHORT
        ).show()
        return false
    }
    return true
}


fun toMealType(type: String): MealType {
    return if (type == MealType.BREAKFAST.name) MealType.BREAKFAST
    else if (type == MealType.LUNCH.name) MealType.LUNCH
    else MealType.SUPPER
}
