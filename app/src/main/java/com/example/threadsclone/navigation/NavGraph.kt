package com.example.threadsclone.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.threadsclone.screens.AddThreads
import com.example.threadsclone.screens.Home
import com.example.threadsclone.screens.Notification
import com.example.threadsclone.screens.Profile
import com.example.threadsclone.screens.Search
import com.example.threadsclone.screens.Splash


@Composable
fun NavGraph(navHostController: NavHostController) {

    NavHost(
        navController = navHostController, startDestination = Routes.Splash.routes
    )
    {
        composable(Routes.Splash.routes) {
            Splash()
        }
        composable(Routes.Home.routes) {
            Home()
        }
        composable(Routes.Notification.routes) {
            Notification()
        }
        composable(Routes.Search.routes) {
            Search()
        }
        composable(Routes.AddThreads.routes) {
            AddThreads()
        }
        composable(Routes.Profile.routes) {
            Profile()
        }
    }

}