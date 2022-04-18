package com.anorlddroid.mi_todo.ui.components


import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Done
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import com.anorlddroid.mi_todo.ui.theme.lBlue
import com.anorlddroid.mi_todo.ui.theme.lRed
import com.anorlddroid.mi_todo.ui.theme.twitterBlue

@Composable
fun FilterBar(
    categoriesFilters: List<String>?,
    onFilterSelected: ((String) -> Unit)?,
    selectedFilter: String?,
) {
    var showFilters by remember { mutableStateOf(false) }
    var filterCategory by remember { mutableStateOf("Categories") }
    val selectedIndex = categoriesFilters?.indexOfFirst { it == selectedFilter }

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
                            showFilters = !showFilters
                        }
                        .background(
                            color = MaterialTheme.colors.background,
                            shape = MaterialTheme.shapes.medium
                        )
                ) {
                    Text(
                        text = filterCategory,
                        style = MaterialTheme.typography.h6,
                        color = MaterialTheme.colors.secondary,
                        modifier = Modifier.padding(start = 10.dp)
                    )
                    Icon(
                        imageVector = Icons.Filled.ArrowDropDown,
                        tint = MaterialTheme.colors.secondary,
                        contentDescription = "Sort",
                        modifier = Modifier.padding(horizontal = 1.dp)
                    )
                }
                Row(modifier = Modifier.padding(start = 10.dp)) {
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
                                .width(200.dp),
                            expanded = showFilters,
                            onDismissRequest = { showFilters = false },

                            ) {
                            DropdownMenuItem(onClick = {
                                showFilters = false
                                filterCategory = "Categories"
                                if (onFilterSelected != null) {
                                    if (categoriesFilters != null) {
                                        onFilterSelected(categoriesFilters[0])
                                    }
                                }

                            }) {
                                Row(
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text(
                                        text = "Categories",
                                        style = MaterialTheme.typography.h6,
                                        color = if (filterCategory == "Categories") twitterBlue else MaterialTheme.colors.secondary
                                    )
                                    if (filterCategory == "Categories") {
                                        Icon(
                                            Icons.Filled.Done,
                                            tint = twitterBlue,
                                            contentDescription = null,
                                        )
                                    }
                                }
                            }
                            DropdownMenuItem(onClick = {
                                showFilters = false
                                filterCategory = "Meals"
                                if (onFilterSelected != null) {
                                    onFilterSelected("Meals")
                                }
                            }) {
                                Row(
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text(
                                        text = "Meals",
                                        style = MaterialTheme.typography.h6,
                                        color = if (filterCategory == "Meals") twitterBlue else MaterialTheme.colors.secondary
                                    )
                                    if (filterCategory == "Meals") {
                                        Icon(
                                            Icons.Filled.Done,
                                            tint = twitterBlue,
                                            contentDescription = null
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
            if (filterCategory == "Categories") {
                categoriesFilters?.forEachIndexed { index, filter ->
                    Tab(
                        selected = index == selectedIndex,
                        modifier = Modifier.background(
                            color = Color.Transparent,
                            shape = MaterialTheme.shapes.small
                        ),
                        onClick = {
                            if (onFilterSelected != null) {
                                onFilterSelected(filter)
                            }
                        }
                    ) {
                        FilterChip(
                            filter = filter,
                            selected = index == selectedIndex
                        )
                    }
                }
            }
        }
    }
}


@Composable
fun FilterChip(
    filter: String,
    selected: Boolean,
    modifier: Modifier = Modifier,
    shape: Shape = MaterialTheme.shapes.small
) {

    val backgroundColor by animateColorAsState(
        if (selected) MaterialTheme.colors.primary
        else MaterialTheme.colors.background
    )

    val border = Modifier.fadeInDiagonalGradientBorder(
        showBorder = !selected,
        colors = listOf(lBlue, lRed),
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
                .then(backgroundPressed)
                .then(border),
        ) {
            Text(
                text = filter,
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

