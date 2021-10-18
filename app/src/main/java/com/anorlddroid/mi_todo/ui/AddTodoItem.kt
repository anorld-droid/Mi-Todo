package com.anorlddroid.mi_todo.ui

import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.anorlddroid.mi_todo.ui.components.MiTodoDivider
import com.anorlddroid.mi_todo.ui.components.MiTodoScaffold
import com.anorlddroid.mi_todo.ui.theme.AlphaNearOpaque
import com.anorlddroid.mi_todo.ui.theme.NotoSerifDisplay
import com.anorlddroid.mi_todo.ui.theme.brand
import com.anorlddroid.mi_todo.ui.theme.primaryLightColor
import com.google.accompanist.insets.statusBarsPadding
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.datetime.date.DatePickerDefaults
import com.vanpra.composematerialdialogs.datetime.date.datepicker
import com.vanpra.composematerialdialogs.datetime.time.TimePickerDefaults
import com.vanpra.composematerialdialogs.datetime.time.timepicker
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AddTodoItem(upPress: () -> Unit, navController: NavController) {
    MiTodoScaffold(
        topBar = {
            AddTodoItemBar(modifier = Modifier, upPress = upPress)
        },
        content = {
            LazyColumn(
                modifier = Modifier.padding(horizontal = 12.dp)
            ) {
                item {
                    AddTodoItemContent()
                    Spacer(modifier = Modifier.width(40.dp))
                }
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navController.navigate("ui/Home") {
                        launchSingleTop = true
                        restoreState = true
                        popUpTo("ui/AddTodoItem") {
                            inclusive = true
                        }
                    }
                },
                backgroundColor = MaterialTheme.colors.primary,
                contentColor = Color.White
            ) {
                Icon(imageVector = Icons.Filled.Done, contentDescription = "Done")
            }
        }
    )
}


@Composable
fun AddTodoItemBar(modifier: Modifier = Modifier, upPress: () -> Unit) {
    Column(modifier = modifier.statusBarsPadding())
    {
        TopAppBar(
            modifier = modifier,
            backgroundColor = MaterialTheme.colors.background.copy(alpha = AlphaNearOpaque),
            contentColor = MaterialTheme.colors.secondary,
            elevation = 0.dp, // No shadow needed
            actions = {

            },
            title = {
                Text(
                    text = "New Todo Item",
                    style = MaterialTheme.typography.h5,
                    color = MaterialTheme.colors.secondary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .padding(5.dp)
                        .weight(1f)
                        .align(Alignment.CenterHorizontally)
                )
            },
            navigationIcon = {
                Up(upPress = upPress)
            }
        )
        MiTodoDivider()
    }
}

@Composable
fun Up(upPress: () -> Unit) {
    IconButton(
        onClick = upPress,
        modifier = Modifier
            .padding(start = 16.dp, top = 10.dp, bottom = 10.dp)
            .size(36.dp)
    ) {
        Icon(
            imageVector = Icons.Outlined.ArrowBack,
            tint = MaterialTheme.colors.primary,
            contentDescription = "Up button"
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AddTodoItemContent() {
    //time and date variable states
    val dateDialog = rememberMaterialDialogState()
    val timeDialog = rememberMaterialDialogState()
    val selectedDate = remember { mutableStateOf(LocalDate.now()) }
    val selectedTime = remember { mutableStateOf(LocalTime.now()) }

    var todo by remember { mutableStateOf("") }// todo string

    val context = LocalContext.current
    //Category
    var category by remember { mutableStateOf("Work") }
    var showCategory by remember { mutableStateOf(false) }
    val categories = mutableListOf("Work", "School", "Home", "Parties")

    //remainder
    var remainder by remember { mutableStateOf("Daily") }
    var showRemainders by remember { mutableStateOf(false) }
    val remainders = mutableListOf("Daily", "Weekly", "Monthly", "Yearly")


    //checkbox
    val hideTodo = remember { mutableStateOf(false) }
    val deleteTodoWhenDone = remember { mutableStateOf(false) }

    val maxLength by remember {
        mutableStateOf(70)
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(12.dp)
    )
    {
        Text(
            text = "New task?",
            style = TextStyle(
                fontFamily = NotoSerifDisplay,
                fontSize = 20.sp,
                fontWeight = FontWeight.Medium,
                lineHeight = 24.sp
            ),
            color = MaterialTheme.colors.secondary
        )
        TextField(
            value = todo,
            onValueChange = {
                if (it.length <= maxLength) todo = it
            },
            maxLines = 2,
            modifier = Modifier
                .padding(top = 8.dp, bottom = 2.dp)
                .fillMaxWidth(),
            textStyle = MaterialTheme.typography.h6,
            colors = TextFieldDefaults.textFieldColors(
                cursorColor = MaterialTheme.colors.primary,
                disabledLabelColor = MaterialTheme.colors.background,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                trailingIconColor = MaterialTheme.colors.primary
            ),
            trailingIcon = {
                if (todo.isNotEmpty()) {
                    Icon(
                        imageVector = Icons.Filled.Cancel,
                        tint = MaterialTheme.colors.primary,
                        contentDescription = "Clear Text",
                        modifier = Modifier.clickable {
                            todo = ""
                        }
                    )
                }
            },
            shape = RoundedCornerShape(16.dp),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
        )
        if (todo.isNotEmpty()) {
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
        Spacer(modifier = Modifier.height(40.dp))
        Text(
            text = "Date",
            style = TextStyle(
                fontFamily = NotoSerifDisplay,
                fontSize = 20.sp,
                fontWeight = FontWeight.Medium,
                lineHeight = 22.sp
            ),
            color = MaterialTheme.colors.secondary
        )
        TextField(
            value = selectedDate.value.format(DateTimeFormatter.ofPattern("EEEE, dd  MMMM yyyy")),
            onValueChange = {
                dateDialog.show()
            },
            maxLines = 2,
            modifier = Modifier
                .padding(top = 20.dp, bottom = 20.dp)
                .fillMaxWidth(),
            textStyle = MaterialTheme.typography.h6,
            colors = TextFieldDefaults.textFieldColors(
                cursorColor = MaterialTheme.colors.primary,
                disabledLabelColor = MaterialTheme.colors.background,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                trailingIconColor = MaterialTheme.colors.primary
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
            backgroundColor = primaryLightColor,
            shape = MaterialTheme.shapes.medium,
            buttons = {
                positiveButton("Ok")
                negativeButton("Cancel")
            }
        ) {
            datepicker(
                initialDate = selectedDate.value,
                colors = DatePickerDefaults.colors(
                    headerBackgroundColor = brand
                ),
            ) { date ->
                selectedDate.value = date
            }
        }
        TextField(
            value = selectedTime.value.format(DateTimeFormatter.ofPattern("hh:mm a")),
            onValueChange = {
                timeDialog.show()
            },
            maxLines = 2,
            modifier = Modifier
                .padding(top = 5.dp, bottom = 20.dp)
                .fillMaxWidth(),
            textStyle = MaterialTheme.typography.h6,
            colors = TextFieldDefaults.textFieldColors(
                cursorColor = MaterialTheme.colors.primary,
                disabledLabelColor = MaterialTheme.colors.background,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                trailingIconColor = MaterialTheme.colors.primary
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
            backgroundColor = primaryLightColor,
            shape = MaterialTheme.shapes.medium,
            buttons = {
                positiveButton("Ok")
                negativeButton("Cancel")
            }
        ) {
            timepicker(
                initialTime = selectedTime.value,
                colors = TimePickerDefaults.colors(
                    activeBackgroundColor = brand
                )
            ) { time ->
                selectedTime.value = time
            }
        }
        Spacer(modifier = Modifier.height(15.dp))
        BoxWithConstraints {
            val maxWidth = maxWidth
            Column {
                Text(
                    text = "Remainder",
                    style = TextStyle(
                        fontFamily = NotoSerifDisplay,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Medium,
                        lineHeight = 22.sp
                    ),
                    color = MaterialTheme.colors.secondary
                )
                TextField(
                    value = remainder,
                    onValueChange = {
                        showRemainders = true
                    },
                    maxLines = 2,
                    modifier = Modifier
                        .padding(top = 20.dp)
                        .width(maxWidth),
                    textStyle = MaterialTheme.typography.h6,
                    colors = TextFieldDefaults.textFieldColors(
                        cursorColor = MaterialTheme.colors.primary,
                        disabledLabelColor = MaterialTheme.colors.background,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        trailingIconColor = MaterialTheme.colors.primary
                    ),
                    trailingIcon = {
                        Icon(
                            imageVector = if (showRemainders) Icons.Filled.ArrowDropUp else Icons.Filled.ArrowDropDown,
                            tint = MaterialTheme.colors.primary,
                            contentDescription = null,
                            modifier = Modifier
                                .padding(end = 8.dp)
                                .clickable {
                                    showRemainders = true
                                }
                        )
                    },
                    shape = RoundedCornerShape(16.dp),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
                )
                MaterialTheme(
                    shapes = MaterialTheme.shapes.copy(medium = RoundedCornerShape(16.dp))
                ) {
                    DropdownMenu(
                        modifier = Modifier
                            .background(
                                color = MaterialTheme.colors.onPrimary
                            )
                            .width(maxWidth),
                        expanded = showRemainders,
                        onDismissRequest = { showRemainders = false },

                        ) {
                        remainders.forEach { listRemainder ->
                            DropdownMenuItem(onClick = {
                                showRemainders = false
                                remainder = listRemainder
                            }) {
                                Text(
                                    text = listRemainder,
                                    style = MaterialTheme.typography.h6,
                                    color = MaterialTheme.colors.secondary
                                )
                            }
                        }
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(25.dp))
        BoxWithConstraints {
            val maxWidth = maxWidth
            Column {
                Text(
                    text = "Categories",
                    style = TextStyle(
                        fontFamily = NotoSerifDisplay,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Medium,
                        lineHeight = 22.sp
                    ),
                    color = MaterialTheme.colors.secondary
                )

                TextField(
                    value = category,
                    onValueChange = {
                        category = it
                    },
                    maxLines = 2,
                    modifier = Modifier
                        .padding(top = 20.dp)
                        .width(maxWidth),
                    textStyle = MaterialTheme.typography.h6,
                    colors = TextFieldDefaults.textFieldColors(
                        cursorColor = MaterialTheme.colors.primary,
                        disabledLabelColor = MaterialTheme.colors.background,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        trailingIconColor = MaterialTheme.colors.primary
                    ),
                    trailingIcon = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Filled.LibraryAdd,
                                contentDescription = "Adding new Categories",
                                tint = MaterialTheme.colors.primary,
                                modifier = Modifier.clickable {
                                    Toast.makeText(
                                        context,
                                        "Added to categories",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    categories.add(category)
                                }
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Icon(
                                imageVector = if (showCategory) Icons.Filled.ArrowDropUp else Icons.Filled.ArrowDropDown,
                                tint = MaterialTheme.colors.primary,
                                contentDescription = null,
                                modifier = Modifier
                                    .padding(end = 8.dp)
                                    .clickable {
                                        showCategory = true
                                    }
                            )
                        }
                    },
                    shape = RoundedCornerShape(16.dp),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
                )
                MaterialTheme(
                    shapes = MaterialTheme.shapes.copy(medium = RoundedCornerShape(16.dp))
                ) {
                    DropdownMenu(
                        modifier = Modifier
                            .background(
                                color = MaterialTheme.colors.onPrimary
                            )
                            .width(maxWidth - 0.dp)
                            .height(120.dp),
                        expanded = showCategory,
                        onDismissRequest = { showCategory = false },

                        ) {
                        categories.forEach { listCategory ->
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
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .background(
                    color = MaterialTheme.colors.background
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
                    Color(0xFF0E3057)
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
                    Color(0xFF0E3057)
                )
            )
            Text(
                text = "Delete when done",
                style = MaterialTheme.typography.h6,
            )
        }
    }
}


