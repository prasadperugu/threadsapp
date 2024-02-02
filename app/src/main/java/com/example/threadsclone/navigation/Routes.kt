package com.example.threadsclone.navigation

sealed class Routes(val routes: String) {

    object Home : Routes("home")
    object Notification : Routes("notification")
    object Splash : Routes("splash")
    object Search : Routes("search")
    object Profile : Routes("profile")
    object AddThreads : Routes("add_threads")

}