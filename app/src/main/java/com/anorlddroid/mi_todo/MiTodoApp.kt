package com.anorlddroid.mi_todo

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.compose.rememberNavController
import com.anorlddroid.mi_todo.ui.components.MiTodoScaffold
import com.anorlddroid.mi_todo.ui.theme.MiTodoTheme
import com.google.accompanist.insets.ProvideWindowInsets

@ExperimentalAnimationApi
@RequiresApi(Build.VERSION_CODES.O)
@ExperimentalMaterialApi
@Composable
fun MiTodoApp(){
    ProvideWindowInsets {
        MiTodoTheme {
            val navController = rememberNavController()
            val scaffoldState = rememberScaffoldState()
            val scope = rememberCoroutineScope()
            MiTodoScaffold(
                scaffoldState = scaffoldState
            ) {
                MiTodoNavGraph(
                    scaffoldState = scaffoldState,
                    coroutineScope = scope,
                    navController = navController)
            }
        }
    }
}