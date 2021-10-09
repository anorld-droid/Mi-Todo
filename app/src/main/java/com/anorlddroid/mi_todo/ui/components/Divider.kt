package com.anorlddroid.mi_todo.ui.components

import android.content.res.Configuration
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.anorlddroid.mi_todo.ui.theme.MiTodoTheme

@Composable
fun MiTodoDivider(
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colors.secondary.copy(alpha = DividerAlpha),
    thickness: Dp = 1.dp,
    startIndent: Dp = 0.dp
) {
    Divider(
        modifier = modifier,
        color = color,
        thickness = thickness,
        startIndent = startIndent
    )
}

private const val DividerAlpha = 0.12f

@Preview("default", showBackground = true)
@Preview("dark theme", uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
private fun DividerPreview() {
    MiTodoTheme {
        Box(Modifier.size(height = 10.dp, width = 100.dp)) {
            MiTodoDivider(Modifier.align(Alignment.Center))
        }
    }
}