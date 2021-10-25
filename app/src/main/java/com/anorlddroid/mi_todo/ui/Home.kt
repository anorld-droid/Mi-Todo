package com.anorlddroid.mi_todo.ui

import android.util.Log
import androidx.compose.animation.*
import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DragHandle
import androidx.compose.material.icons.outlined.AutoAwesome
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.anorlddroid.mi_todo.MiTodoViewModel
import com.anorlddroid.mi_todo.R
import com.anorlddroid.mi_todo.data.database.TodoMinimal
import com.anorlddroid.mi_todo.ui.components.*
import com.anorlddroid.mi_todo.ui.theme.MiTodoTheme
import com.anorlddroid.mi_todo.ui.theme.NotoSerifDisplay
import com.anorlddroid.mi_todo.ui.theme.ThemeState
import com.google.accompanist.insets.statusBarsPadding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlin.math.cos
import kotlin.math.roundToInt
import kotlin.math.sin

@ExperimentalMaterialApi
@Composable
fun Home(
    navController: NavController,
    coroutineScope: CoroutineScope,
    scaffoldState: ScaffoldState
) {
    val viewModel: MiTodoViewModel = viewModel()
    val bottomSheetState =
        rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)
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
                        fontFamily = NotoSerifDisplay,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Medium,
                        lineHeight = 24.sp
                    ),
                    modifier = Modifier.padding(
                        top = 2.dp,
                        bottom = 2.dp,
                        start = 10.dp,
                        end = 3.dp
                    )
                )
                MiTodoDivider()
                MiTodoRadioThemeGroup(
                    radioOptions = listOf("On", "Off", "Auto"),
                    viewModel.themeState.collectAsState().value,
                    viewModel = viewModel
                )
                ThemeState.selectedTheme = viewModel.themeState.collectAsState().value
                Log.d("HOME", viewModel.themeState.collectAsState().value)
                Text(
                    text = "Hide",
                    style = TextStyle(
                        fontFamily = NotoSerifDisplay,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Medium,
                        lineHeight = 24.sp
                    ),
                    modifier = Modifier.padding(
                        top = 8.dp,
                        bottom = 2.dp,
                        start = 10.dp,
                        end = 3.dp
                    )
                )
                MiTodoDivider()
                MiTodoRadioHideGroup(
                    radioOptions = listOf("On", "Off"),
                    viewModel.hideState.collectAsState().value,
                    viewModel = viewModel
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "Follow us on twitter",
                    style = MaterialTheme.typography.h5,
                    modifier = Modifier.padding(
                        top = 2.dp,
                        bottom = 2.dp,
                        start = 10.dp,
                        end = 3.dp
                    )
                )
                MiTodoDivider()
                Text(
                    text = "Link coming soon ):",
                    style = MaterialTheme.typography.h6,
                    modifier = Modifier.padding(
                        top = 2.dp,
                        bottom = 20.dp,
                        start = 14.dp,
                        end = 3.dp
                    )
                )
            }
        }
    ) {
        MiTodoScaffold(
            topBar = {
                HomeBar(modifier = Modifier, coroutineScope, bottomSheetState)
            },
            content = {
                HomeContent(onDeleted = { todo, delete ->
                    if (delete) {
                        viewModel.deleteTodo(todo.name)
                    }
                }, scaffoldState = scaffoldState, coroutineScope = coroutineScope)


            },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = {
                        navController.navigate("ui/AddTodoItem") {
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    backgroundColor = MaterialTheme.colors.primary,
                    contentColor = Color.White
                ) {
                    Icon(imageVector = Icons.Filled.Add, contentDescription = "Add new Todo")
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
    bottomSheetState: ModalBottomSheetState
) {
    Column(modifier = modifier.statusBarsPadding())
    {
        TopAppBar(
            modifier = modifier,
            backgroundColor = MaterialTheme.colors.background,
            contentColor = MaterialTheme.colors.secondary,
            elevation = 0.dp, // No shadow needed
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
                Text(
                    text = "Mi-Todo",
                    style = MaterialTheme.typography.h5,
                    color = MaterialTheme.colors.secondary,
                    textAlign = TextAlign.Center,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .padding(5.dp)
                        .weight(1f)
                        .align(Alignment.CenterHorizontally)
                )
            }
        )
        MiTodoDivider()
    }
}

@Composable
fun HomeContent(
    onDeleted: (todo: TodoMinimal, delete: Boolean) -> Unit,
    scaffoldState: ScaffoldState,
    coroutineScope: CoroutineScope
) {
    val viewModel: MiTodoViewModel = viewModel()
    val categories by viewModel.categories.collectAsState()
    val todos by viewModel.todos.collectAsState()
    val selectedCategory by viewModel.selectedCategory.collectAsState()
    Log.d("HOME", "Categories ==> $categories")
    Log.d("HOME", "todos ==> $todos")
    Log.d("HOME", "Selected Categories ==> $selectedCategory")

    Column(
        modifier = Modifier
            .padding(2.dp)
            .fillMaxSize()
    )
    {
        if (categories.isNotEmpty()) {
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
                    todos.forEach { (title, todosList) ->
                        if (todosList.isNotEmpty()) {
                            Text(
                                text = title,
                                style = TextStyle(
                                    fontFamily = NotoSerifDisplay,
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Medium,
                                    lineHeight = 24.sp
                                ),
                                modifier = Modifier.padding(
                                    top = 2.dp,
                                    bottom = 22.dp,
                                    start = 12.dp,
                                    end = 3.dp
                                )
                            )
                            todosList.forEach { todo ->
                                key(todo) {
                                    TodoCard(
                                        todo = todo,
                                        onDeleted = {
                                            todosList.remove(todo)
                                            coroutineScope.launch {
                                                val snackbarResult =
                                                    scaffoldState.snackbarHostState.showSnackbar(
                                                        message = "${todo.name} deleted ",
                                                        actionLabel = "Undo"
                                                    )
                                                when (snackbarResult) {
                                                    SnackbarResult.Dismissed -> onDeleted(
                                                        todo,
                                                        true
                                                    )
                                                    SnackbarResult.ActionPerformed -> todosList.add(
                                                        todo
                                                    )
                                                }
                                            }
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        } else {

        }
    }
}


@Composable
fun TodoCard(todo: TodoMinimal, onDeleted: () -> Unit) {
    val particleRadiusDp = dimensionResource(id = R.dimen.particle_radius)
    val particleRadius: Float
    val itemHeightDp = dimensionResource(id = R.dimen.image_size)
    val itemHeight: Float
    val explosionParticleRadius: Float
    val explosionRadius: Float
    val density = LocalDensity.current

    with(density) {
        particleRadius = particleRadiusDp.toPx()
        itemHeight = itemHeightDp.toPx()
        explosionParticleRadius = dimensionResource(id = R.dimen.explosion_particle_radius).toPx()
        explosionRadius = dimensionResource(id = R.dimen.explosion_radius).toPx()
    }
    val screenWidth: Int
    with(LocalConfiguration.current) {
        screenWidth = this.screenWidthDp
    }
    val radius = itemHeight * 0.5f
    val funnelWidth = radius * 3

    val offsetX = remember { Animatable(0f) }
    val checked = remember { mutableStateOf(false) }


    val explosionPercentage = remember { mutableStateOf(0f) }

    val funnelInitialTranslation = -funnelWidth - particleRadius
    val funnelTranslation = remember { mutableStateOf(funnelInitialTranslation) }
    funnelTranslation.value = (offsetX.value + funnelInitialTranslation).negateIfPositive {
        explosionPercentage.value = (offsetX.value + funnelInitialTranslation) / screenWidth
    }

    BoxWithConstraints {
        Canvas(
            modifier = Modifier
                .height(itemHeightDp)
                .padding(end = 10.dp)
        ) {
            translate(funnelTranslation.value) {
                drawPath(
                    path = drawFunnel(
                        upperRadius = radius,
                        lowerRadius = particleRadius * 3 / 4f,
                        width = funnelWidth
                    ),
                    color = Color(0xFF0E3057)
                )
            }
            translate(offsetX.value - 26) {
                drawCircle(
                    color = Color(0xFF0E3057),
                    radius = particleRadius
                )
            }
        }
        Canvas(modifier = Modifier
            .height(itemHeightDp)
            .offset {
                IntOffset(
                    (offsetX.value.roundToInt() - 2 * particleRadius.toInt()).coerceAtMost(
                        funnelWidth.toInt()
                    ), 0
                )
            })
        {
            val numberOfExplosionParticles = 10
            val particleAngle = Math.PI * 2 / numberOfExplosionParticles
            var angle = 0.0
            repeat(numberOfExplosionParticles / 2 + 1) {
                val hTranslation =
                    (cos(angle).toFloat() * explosionRadius) * explosionPercentage.value
                val vTranslation =
                    (sin(angle).toFloat() * explosionRadius) * explosionPercentage.value

                translate(hTranslation, vTranslation) {
                    drawCircle(
                        color = Color(0xFF0E3057),
                        radius = explosionParticleRadius,
                        alpha = explosionPercentage.value / 2
                    )
                }
                if (angle != 0.0 && angle != Math.PI) {
                    translate(hTranslation, -vTranslation) {
                        drawCircle(
                            color = Color(0xFF0E3057),
                            radius = explosionParticleRadius,
                            alpha = explosionPercentage.value / 2
                        )
                    }
                }
                angle += particleAngle
            }
        }
        AnimatedVisibility(
            visible = true,
            enter = slideInVertically(
                //slide in from 40dp from the top.
                initialOffsetY = { with(density) { -40.dp.roundToPx() } }
            ) + expandVertically(
                //Expand from the top
                expandFrom = Alignment.Top
            ) + fadeIn(
                //Fade in with the initial alpha of 0.3f
                initialAlpha = 0.3f
            ),
        ) {
            MiTodoSurface(
                modifier = if (checked.value) {
                    Modifier
                        .padding(start = 18.dp, end = 4.dp, bottom = 10.dp)
                        .swipeToDelete(offsetX, maximumWidth = maxWidth.value) {
                            onDeleted()
                        }
                } else {
                    Modifier
                        .padding(start = 10.dp, end = 4.dp, bottom = 10.dp)
                },
                shape = MaterialTheme.shapes.medium,
                color = MaterialTheme.colors.surface,
                elevation = 2.dp

            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize(),
                ) {
                    Row(
                        modifier = Modifier
                            .background(
                                color = MaterialTheme.colors.onPrimary
                            )
                            .fillMaxWidth()
                            .padding(start = 10.dp, top = 2.dp, end = 2.dp, bottom = 8.dp)
                    ) {
                        Checkbox(
                            checked = checked.value,
                            onCheckedChange = {
                                checked.value =
                                    it
                            },
                            enabled = true,
                            colors = CheckboxDefaults.colors(
                                Color(0xFF0E3057)
                            ),
                            modifier = Modifier
                                .padding(top = 22.dp, start = 4.dp, end = 8.dp)
                                .size(6.dp)
                        )
                        Column(
                            modifier = Modifier.padding(
                                horizontal = 4.dp,
                                vertical = 2.dp
                            )
                        ) {
                            Text(
                                text = todo.name,
                                style = MaterialTheme.typography.h6,
                                modifier = Modifier
                                    .padding(start = 5.dp, top = 12.dp)
                                    .fillMaxWidth(),
                                textDecoration = if (checked.value) TextDecoration.LineThrough else TextDecoration.None,
                            )
                            Text(
                                text = "${todo.date}, ${todo.time}",
                                style = MaterialTheme.typography.subtitle1,
                                modifier = Modifier
                                    .padding(start = 5.dp)
                                    .fillMaxWidth()
                            )
                        }
                    }
                }
            }
        }
    }
}


private fun Float.negateIfPositive(onPositive: () -> Unit): Float {
    return if (this > 0) {
        onPositive()
        -this
    } else this
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
                        modifier = Modifier.padding(start = 2.dp)
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
                        modifier = Modifier.padding(start = 2.dp)
                    )
                }
            }
        }
    }
}

@ExperimentalAnimationApi
@Preview
@Composable
fun TodoCardPreview() {
    MiTodoTheme {
        TodoCard(
            TodoMinimal(
                "Cook Supper",
                "21 March 2021, 08:34am",
                "",
                "",
                hide = false,
                delete = true
            ),
            onDeleted = {},
        )
    }
}
