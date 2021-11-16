package com.anorlddroid.mi_todo.ui.components


import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Tab
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Sort
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp

@Composable
fun FilterBar(
    categoriesFilters: List<String>?,
    onFilterSelected: ((String) -> Unit)?,
    selectedFilter: String?
) {
    if (categoriesFilters?.isNotEmpty() == true) {
        val filterName by remember { mutableStateOf("Categories") }
        val selectedIndex = categoriesFilters.indexOfFirst { it == selectedFilter }
        LazyRow(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(start = 2.dp, end = 8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 56.dp),
        ) {
            item {
                Row(
                    modifier = Modifier
                        .padding(start = 2.dp, end = 6.dp)
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
                categoriesFilters.forEachIndexed { index, filter ->
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

