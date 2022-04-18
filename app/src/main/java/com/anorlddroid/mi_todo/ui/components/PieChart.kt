package com.anorlddroid.mi_todo.ui.components

import android.graphics.Paint
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.anorlddroid.mi_todo.data.database.TodoMinimal
import com.anorlddroid.mi_todo.ui.theme.lBlue
import com.anorlddroid.mi_todo.ui.theme.lRed
import kotlin.math.ceil


@Composable
fun TasksPieChart(todaysTaskList: List<TodoMinimal>) {
    val totalTasks: Int = todaysTaskList.size
    var completedTasks = 0
    for (task in todaysTaskList) {
        if (task.completed) {
            completedTasks++
        }
    }
    val todaysCompletedTasks = (completedTasks.toFloat() / totalTasks.toFloat()) * 100
    val completedTasksAngle = remember { Animatable(initialValue = 0f) }
    val completedTasksTargetAngle = remember { (completedTasks * 360f) / totalTasks }
    val remainingTasksTargetAngle = 360f - completedTasksTargetAngle
    val remainingTasksAngle = remember { Animatable(initialValue = 0f) }
    val density = LocalDensity.current.density
    val boxSize = 230f
    val circleStrokeWidth = 5f
    val outerCircleSize = (boxSize * density) - (circleStrokeWidth / 2 * density)
    val startAngleRemainingTasks: Float =
        if (completedTasks == 0 || ceil(todaysCompletedTasks).toInt() == 100) {
            completedTasksTargetAngle
        } else {
            completedTasksTargetAngle + 1f
        }
    val sweepAngleRemainingTasks: Float =
        if (completedTasks == 0 || ceil(todaysCompletedTasks).toInt() == 100) {
            remainingTasksAngle.value
        } else {
            remainingTasksAngle.value - 2f
        }
    Box {
        val textColor = MaterialTheme.colors.secondary
        val completedTasksColor = lBlue
        val remainingTasks = lRed
        Canvas(
            modifier = Modifier.size(Dp(boxSize)),
        ) {
            //draws the total phone storage space
            val paint = Paint().apply {
                textAlign = Paint.Align.CENTER
                textSize = 20.dp.toPx()
                color = textColor.toArgb()
            }

            drawContext.canvas.nativeCanvas.drawText(
                if (todaysTaskList.isEmpty()) "No Tasks " else "${ceil(todaysCompletedTasks).toInt()} %",
                center.x,
                center.y - 10,
                paint
            )

            drawContext.canvas.nativeCanvas.drawText(
                if (todaysTaskList.isEmpty()) "Assigned Today" else "Completed",
                center.x,
                center.y + 30,
                paint
            )

            rotate(-90f) {
                val offset1 = circleStrokeWidth / 2
                drawArc(
                    color = completedTasksColor,
                    startAngle = 0f,
                    sweepAngle = completedTasksAngle.value,
                    useCenter = false,
                    topLeft = Offset(offset1, offset1),
                    size = Size(outerCircleSize, outerCircleSize),
                    style = Stroke(circleStrokeWidth, 0f)
                )
            }
        }
        Canvas(
            modifier = Modifier
                .size(Dp(boxSize))
                .padding(start = 0.dp),
        ) {
            rotate(-90f) {
                val offset1 = circleStrokeWidth / 2
                drawArc(
                    color = remainingTasks,
                    startAngle = startAngleRemainingTasks,
                    sweepAngle = sweepAngleRemainingTasks,
                    useCenter = false,
                    topLeft = Offset(offset1, offset1),
                    size = Size(outerCircleSize, outerCircleSize),
                    style = Stroke(circleStrokeWidth, 0f)
                )
            }
        }
    }
    LaunchedEffect("Current Tasks") {
        completedTasksAngle.animateTo(
            targetValue = completedTasksTargetAngle,
            animationSpec = tween(
                durationMillis = 1000,
                easing = FastOutSlowInEasing
            )
        )
        remainingTasksAngle.animateTo(
            targetValue = remainingTasksTargetAngle,
            animationSpec = tween(
                durationMillis = 700,
                easing = FastOutSlowInEasing
            )
        )
    }
}