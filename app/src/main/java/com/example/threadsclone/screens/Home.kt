package com.example.threadsclone.screens

import android.util.Log
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.threadsclone.item_view.ThreadItem
import com.example.threadsclone.model.UserModel
import com.example.threadsclone.viewmodel.HomeViewModel
import com.example.threadsclone.viewmodel.ThreadModel
import com.google.firebase.auth.FirebaseAuth


val SOMEX = "AppDebug";

@Composable
fun Home(navHostController: NavHostController) {

    val homeViewModel: HomeViewModel = viewModel()


    val threadsAndUsers: List<Pair<ThreadModel, UserModel>>? by homeViewModel.threadsAndUsers.observeAsState(
        null
    )

    LazyColumn {
        Log.e(
            SOMEX, "outsideLazy${
                threadsAndUsers.let {
                    Log.e(SOMEX, it.toString())
                }
            }"
        )
        items(threadsAndUsers ?: emptyList()) { pairs ->
            ThreadItem(
                thread = pairs.first,
                users = pairs.second,
                navHostController = navHostController,
                userId = FirebaseAuth.getInstance().currentUser!!.uid
            )
        }

    }


}

@Preview(showBackground = true)
@Composable
fun ShowHome() {
//    Home()
}