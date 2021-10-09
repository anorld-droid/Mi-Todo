package com.anorlddroid.mi_todo.ui.components


import android.content.res.Configuration
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Sort
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.PopupProperties
import com.anorlddroid.mi_todo.data.Filter
import com.anorlddroid.mi_todo.ui.theme.MiTodoTheme

@Composable
fun FilterBar(weekdayFilters: List<Filter>, categoriesFilters: List<Filter>) {
    var filterName by remember { mutableStateOf("Weekday") }
    var showMenu by remember { mutableStateOf(false) }
    LazyRow(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(start = 2.dp, end = 8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 56.dp),
    ) {
        item {
            Box {
                Row(
                    modifier = Modifier
                        .padding(start = 2.dp, end = 6.dp)
                        .clickable {
                            showMenu = !showMenu
                        }
                ) {
                    Icon(
                        imageVector = Icons.Filled.Sort,
                        contentDescription = "Sort",
                    )
                    Text(
                        text = filterName,
                        style = MaterialTheme.typography.h6,
                        color = MaterialTheme.colors.secondary
                    )
                }
                MaterialTheme(
                    shapes = MaterialTheme.shapes.copy(medium = RoundedCornerShape(16.dp))
                ) {
                    DropdownMenu(
                        modifier = Modifier
                            .background(
                                color = MaterialTheme.colors.onPrimary
                            )
                            .width(200.dp)
                            .padding(start = 2.dp),
                        expanded = showMenu,
                        onDismissRequest = { showMenu = false },

                    ) {
                        DropdownMenuItem(onClick = {
                            showMenu = false
                            filterName = "Categories"
                        }) {
                            Text(
                                text = "Categories",
                                style = MaterialTheme.typography.h6,
                                color = MaterialTheme.colors.secondary
                            )
                        }
                        DropdownMenuItem(onClick = {
                            showMenu = false
                            filterName = "Weekday"
                        }) {
                            Text(
                                text = "Weekday",
                                style = MaterialTheme.typography.h6,
                                color = MaterialTheme.colors.secondary
                            )
                        }
                    }
                }
            }
            if (filterName == "Weekday") {
                weekdayFilters.forEachIndexed { index, filter ->
                    FilterChip(index, weekdayFilters, filter)
                }
            } else {
                categoriesFilters.forEachIndexed { index, filter ->
                    FilterChip(index, categoriesFilters, filter)
                }
            }
        }
    }
}


@Composable
fun FilterChip(
    index: Int,
    filters: List<Filter>,
    filter: Filter,
    modifier: Modifier = Modifier,
    shape: Shape = MaterialTheme.shapes.small
) {

    val (selected, setSelected) = filter.enabled
    val backgroundColor by animateColorAsState(
        if (selected) MaterialTheme.colors.primary
        else MaterialTheme.colors.background
    )

    val border = Modifier.fadeInDiagonalGradientBorder(
        showBorder = !selected,
        colors = listOf(Color(0xFF16bffd), Color(0xFFcb3066)),
        shape = shape
    )
    val textColor by animateColorAsState(
        if (selected) Color.Black else MaterialTheme.colors.secondary
    )

    MiTodoSurface(
        modifier = modifier
            .padding(6.dp)
            .height(28.dp),
        color = backgroundColor,
        contentColor = textColor,
        shape = shape,
        elevation = 0.dp
    ) {
        val interactionSource = remember { MutableInteractionSource() }

        val pressed by interactionSource.collectIsPressedAsState()
        val backgroundPressed =
            if (pressed) {
                Modifier.offsetGradientBackground(
                    listOf(Color(0xFF0f0c29), Color(0xFF302b63), Color(0xFF24243e)),
                    200f,
                    0f
                )
            } else {
                Modifier.background(Color.Transparent)
            }
        Row(
            modifier = Modifier
                .toggleable(
                    value = selected,
                    onValueChange = setSelected,
                    interactionSource = interactionSource,
                    indication = null
                )
                .then(backgroundPressed)
                .then(border),
        ) {
            Text(
                text = filter.name,
                style = MaterialTheme.typography.caption,
                maxLines = 1,
                modifier = Modifier.padding(
                    horizontal = 20.dp,
                    vertical = 6.dp
                )
            )
        }
    }
}


@Preview("default")
@Preview("dark theme", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview("large font", fontScale = 2f)
@Composable
private fun FilterDisabledPreview() {
    MiTodoTheme {
        FilterChip(
            1,
            listOf(Filter(name = "Demo", enabled = false)),
            Filter(name = "Demo", enabled = false),
            Modifier.padding(4.dp)
        )
    }
}

@Preview("default")
@Preview("dark theme", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview("large font", fontScale = 2f)
@Composable
private fun FilterEnabledPreview() {
    MiTodoTheme {
        FilterChip(
            1,
            listOf(Filter(name = "Demo", enabled = true)),
            Filter(name = "Demo", enabled = true)
        )
    }
}
