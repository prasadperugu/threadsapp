package com.example.threadsclone.screens

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.threadsclone.navigation.Routes
import com.example.threadsclone.viewmodel.AuthViewModel

@Composable
fun Profile(navHostController: NavHostController) {

    val authViewModel: AuthViewModel = viewModel()
    val firebaseUser by authViewModel.firebaseUser.observeAsState(null)

    LaunchedEffect(firebaseUser) {
        //when click on New User?Crate Account  launchedeffect has been calling only for first time.

        if (firebaseUser == null) {

            navHostController.navigate(Routes.Login.routes) {

                popUpTo(navHostController.graph.startDestinationId)
                launchSingleTop = true
            }
        } else {
            Log.i("ErrorCheckcase", "Something went wrong")
        }
    }



    Text(text = "Profile", modifier = Modifier
        .clickable {
            authViewModel.logout()
        }
    )

}