package com.anorlddroid.mi_todo.ui

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.anorlddroid.mi_todo.R
import com.anorlddroid.mi_todo.data.ToDos
import com.anorlddroid.mi_todo.data.getCategoriesFilters
import com.anorlddroid.mi_todo.data.getTodos
import com.anorlddroid.mi_todo.data.getWeekdayFilters
import com.anorlddroid.mi_todo.ui.components.*
import com.anorlddroid.mi_todo.ui.theme.MiTodoTheme
import com.google.accompanist.insets.statusBarsPadding
import kotlin.math.cos
import kotlin.math.roundToInt
import kotlin.math.sin

@Composable
fun Home(navController: NavController) {
    val todos = remember { mutableStateListOf(*getTodos()) }
    MiTodoScaffold(
        topBar = {
            HomeBar(modifier = Modifier)
        },
        content = {
            HomeContent(
                todoItems = todos,
                onDelete = { todos.remove(it) }
            )
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


@Composable
fun HomeBar(modifier: Modifier = Modifier) {
    var showMenu by remember { mutableStateOf(false) }
    Column(modifier = modifier.statusBarsPadding())
    {
        TopAppBar(
            modifier = modifier,
            backgroundColor = MaterialTheme.colors.background,
            contentColor = MaterialTheme.colors.secondary,
            elevation = 0.dp, // No shadow needed
            actions = {
                IconButton(onClick = {
                    showMenu = !showMenu
                }) {
                    Icon(
                        Icons.Outlined.MoreVert,
                        tint = MaterialTheme.colors.primary,
                        contentDescription = "More Operations"
                    )
                }
                DropdownMenu(
                    modifier = Modifier
                        .background(
                            color = MaterialTheme.colors.onPrimary
                        )
                        .padding(start = 2.dp),
                    expanded = showMenu,
                    onDismissRequest = { showMenu = false },
                ) {
                    DropdownMenuItem(onClick = { showMenu = false /*TODO*/ }) {
                        Text(
                            text = "Hide/Unhide",
                            style = MaterialTheme.typography.h6,
                            color = MaterialTheme.colors.secondary
                        )
                    }

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
    todoItems: MutableList<ToDos>,
    onDelete: (todoItem: ToDos) -> Unit
) {

    val weekdayFilter = getWeekdayFilters()
    val categoryFilter = getCategoriesFilters()
    Column(
        modifier = Modifier
            .padding(2.dp)
            .fillMaxSize()
    )
    {
        FilterBar(weekdayFilters = weekdayFilter, categoriesFilters = categoryFilter)
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

            }
            item {
                weekdayFilter.forEach {
                    Text(
                        text = it.name,
                        style = MaterialTheme.typography.h5,
                        modifier = Modifier.padding(
                            top = 2.dp,
                            bottom = 22.dp,
                            start = 4.dp,
                            end = 3.dp
                        )
                    )
                    todoItems.forEachIndexed { index, todo ->
                        key(todo) {
                            TodoCard(todo = todo, onDeleted = { onDelete(todo) }, index = index)

                        }
                    }
                }
            }
        }
    }
}


@Composable
fun TodoCard(todo: ToDos, onDeleted: () -> Unit, index: Int) {
    val particleRadiusDp = dimensionResource(id = R.dimen.particle_radius)
    val particleRadius: Float
    val itemHeightDp = dimensionResource(id = R.dimen.image_size)
    val itemHeight: Float
    val explosionParticleRadius: Float
    val explosionRadius: Float
    with(LocalDensity.current) {
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
    val sideShapeWidth = funnelWidth + particleRadius * 2

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
            translate(offsetX.value - 18) {
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

        MiTodoSurface(
            modifier = Modifier
                .padding(start = 16.dp, end = 4.dp, bottom = 8.dp)
                .swipeToDelete(offsetX, maximumWidth = maxWidth.value) {
                    onDeleted()
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
                        .padding(start = 6.dp, top = 6.dp, end = 6.dp)
                ) {
                    Checkbox(
                        checked = checked.value,
                        onCheckedChange = {
                            checked.value =
                                it // Todo implement LineThrough to mark as completed
                        },
                        colors = CheckboxDefaults.colors(
                            Color(0xFF0E3057)
                        )
                    )
                    Text(
                        text = todo.name,
                        modifier = Modifier.padding(start  = 2.dp),
                        style = MaterialTheme.typography.h6,
                        textDecoration = if (checked.value) TextDecoration.LineThrough else TextDecoration.None,
                    )
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .background(
                            color = MaterialTheme.colors.onPrimary
                        )
                        .fillMaxWidth()
                        .padding(start = 30.dp, bottom = 6.dp)
                ) {
                    Text(
                        text = "21 March 2021, ",//TODO store in db
                        style = MaterialTheme.typography.subtitle1,
                        textDecoration = if (checked.value) TextDecoration.LineThrough else TextDecoration.None,
                    )
                    Text(
                        text = todo.time,
                        style = MaterialTheme.typography.subtitle1,
                        textDecoration = if (checked.value) TextDecoration.LineThrough else TextDecoration.None,
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
    MiTodoTheme(true) {
        TodoCard(
            ToDos(
                "Cook Supper",
                "21 March 2021, 08:34am"
            ),
            onDeleted = {},
            index = 1
        )
    }
}

private fun Float.negateIfPositive(onPositive: () -> Unit): Float {
    return if (this > 0) {
        onPositive()
        -this
    } else this
}


