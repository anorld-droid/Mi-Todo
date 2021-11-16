package com.anorlddroid.mi_todo

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ScaffoldState
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.anorlddroid.mi_todo.ui.AddTodoItem
import com.anorlddroid.mi_todo.ui.Home
import kotlinx.coroutines.CoroutineScope

@ExperimentalAnimationApi
@ExperimentalMaterialApi
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MiTodoNavGraph(
    scaffoldState: ScaffoldState,
    coroutineScope: CoroutineScope,
    navController: NavHostController = rememberNavController()
){
    NavHost(
        navController = navController,
        startDestination = "ui/Home"
    ){
        composable("ui/AddTodoItem") {
            AddTodoItem (upPress = {navController.navigateUp()}, navController = navController)
        }
        composable("ui/Home") {
            Home(navController = navController, coroutineScope, scaffoldState)
        }

    }
}