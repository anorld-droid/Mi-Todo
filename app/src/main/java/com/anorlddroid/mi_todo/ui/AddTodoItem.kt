package com.anorlddroid.mi_todo.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.anorlddroid.mi_todo.ui.components.MiTodoDivider
import com.anorlddroid.mi_todo.ui.components.MiTodoScaffold
import com.anorlddroid.mi_todo.ui.theme.AlphaNearOpaque
import com.anorlddroid.mi_todo.ui.theme.NotoSerifDisplay
import com.google.accompanist.insets.statusBarsPadding


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
                }
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navController.navigate("ui/Home") {
                        launchSingleTop = true
                        restoreState = true
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

@Composable
fun AddTodoItemContent() {
    var todo by remember { mutableStateOf("") }
    var date by remember { mutableStateOf("Date") }
    var time by remember { mutableStateOf("") }
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
                todo = it
            },
            maxLines = 2,
            modifier = Modifier
                .padding(top = 8.dp, end = 6.dp, bottom = 20.dp)
                .fillMaxWidth(),
            textStyle = MaterialTheme.typography.h6,
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = MaterialTheme.colors.background,
                cursorColor = MaterialTheme.colors.primary,
                disabledLabelColor = MaterialTheme.colors.background,
                focusedIndicatorColor = MaterialTheme.colors.primary,
                unfocusedIndicatorColor = MaterialTheme.colors.primary.copy(alpha = 0.55F),
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
            shape = RoundedCornerShape(0.dp),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
        )
        Spacer(modifier = Modifier.height(50.dp))
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
        Row(
            modifier = Modifier
                .padding(top = 4.dp, end = 6.dp)
                .fillMaxWidth(),
        ) {
            BasicTextField(
                enabled = false,
                value = date,
                onValueChange = {
                    date = it
                },
                maxLines = 2,
                modifier = Modifier
                    .padding(top = 8.dp),
                textStyle = MaterialTheme.typography.h6,
                decorationBox ={ innerTextField ->

                },
//                colors = TextFieldDefaults.textFieldColors(
//                    backgroundColor = MaterialTheme.colors.background,
//                    disabledTextColor = MaterialTheme.colors.secondary,
//                    cursorColor = MaterialTheme.colors.primary,
//                    disabledLabelColor = MaterialTheme.colors.background,
//                    disabledIndicatorColor = MaterialTheme.colors.primary,
//                    unfocusedIndicatorColor = MaterialTheme.colors.primary.copy(alpha = 0.55F),
//                    trailingIconColor = MaterialTheme.colors.primary
//                ),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
            )
            Icon(
                imageVector = Icons.Filled.DateRange,
                tint = MaterialTheme.colors.primary,
                contentDescription = "Calender picker",
                modifier = Modifier.clickable {
                    date = "You clicked me ):"
                }
            )
            Icon(
                imageVector = Icons.Filled.Cancel,
                tint = MaterialTheme.colors.primary,
                contentDescription = "Up button",
                modifier = Modifier.clickable {
                    date = ""

                }
            )
        }
        BoxWithConstraints {
            val maxwidth = maxWidth
            Row(
                modifier = Modifier
                    .padding(top = 18.dp, end = 6.dp, bottom = 40.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = time,
                    textAlign = TextAlign.Start,
                    modifier = Modifier
                        .padding(end = 10.dp)
                        .width(maxwidth - 30.dp),
                    style = MaterialTheme.typography.h6,
                    textDecoration = TextDecoration.Underline
                )
                Icon(
                    imageVector = Icons.Filled.HistoryToggleOff,
                    tint = MaterialTheme.colors.primary,
                    contentDescription = "Calender picker",
                    modifier = Modifier
                        .padding(end = 6.dp)
                        .clickable {
                            time = "You clicked me ):"
                        }
                )
                Icon(
                    imageVector = Icons.Filled.Cancel,
                    tint = MaterialTheme.colors.primary,
                    contentDescription = "Clear Text",
                    modifier = Modifier.clickable {
                        time = ""
                    }
                )
            }
        }
    }
}