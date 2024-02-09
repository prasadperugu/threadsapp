package com.example.threadsclone.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.threadsclone.screens.AddThreads
import com.example.threadsclone.screens.BottomNav
import com.example.threadsclone.screens.Home
import com.example.threadsclone.screens.Login
import com.example.threadsclone.screens.Notification
import com.example.threadsclone.screens.Profile
import com.example.threadsclone.screens.Register
import com.example.threadsclone.screens.Search
import com.example.threadsclone.screens.Splash


@Composable
fun NavGraph(navController: NavHostController) {

    NavHost(
        navController = navController, startDestination = Routes.Splash.routes
    )
    {
        composable(Routes.Splash.routes) {
            Splash(navController)
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
            AddThreads(navController)
        }
        composable(Routes.Profile.routes) {
            Profile(navController)
        }
         composable(Routes.BottomNav.routes) {
            BottomNav(navController)
        }
        composable(Routes.Login.routes) {
            Login(navController)
        }
         composable(Routes.Register.routes) {
            Register(navController)
        }

    }

}