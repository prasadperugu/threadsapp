package com.example.threadsclone.screens

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.threadsclone.item_view.ThreadItem
import com.example.threadsclone.model.UserModel
import com.example.threadsclone.navigation.Routes
import com.example.threadsclone.utils.SharedPref
import com.example.threadsclone.viewmodel.AuthViewModel
import com.example.threadsclone.viewmodel.UserViewModel
import com.google.firebase.auth.FirebaseAuth


@Composable
fun OtherUsers(navHostController: NavHostController, uid: String) {
    val authViewModel: AuthViewModel = viewModel()
    val firebaseUser by authViewModel.firebaseUser.observeAsState(null)

    val userViewModel: UserViewModel = viewModel()
    val threads by userViewModel.threads.observeAsState(null)
    val users by userViewModel.users.observeAsState(null)

    val context = LocalContext.current



    userViewModel.fetchThreads(uid)
    userViewModel.fetchUser(uid)


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




    LazyColumn() {
        item {
            ConstraintLayout(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {

                val (
                    text, logo, userName,
                    bio, followers, following, imageBox, button
                ) = createRefs()


                Text(text = users!!.name, style = TextStyle(
                    fontWeight = FontWeight.ExtraBold, fontSize = 24.sp
                ), modifier = Modifier.constrainAs(text) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                }
                )

                Image(
                    painter =
//            painterResource(id = R.drawable.baseline_close_24),
                    rememberAsyncImagePainter(model = users!!.imageUrl),
                    contentDescription = "close",
                    modifier = Modifier
                        .constrainAs(logo)
                        {
                            top.linkTo(parent.top)
                            end.linkTo(parent.end)
                        }
                        .size(120.dp)
                        .clip(CircleShape), contentScale = ContentScale.Crop
                )

                Text(text =users!!.userName, style = TextStyle(
                    fontWeight = FontWeight.ExtraBold, fontSize = 24.sp
                ), modifier = Modifier.constrainAs(userName) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                }
                )

                Text(text = users!!.bio, style = TextStyle(
                    fontWeight = FontWeight.ExtraBold, fontSize = 24.sp
                ), modifier = Modifier.constrainAs(bio) {
                    top.linkTo(userName.bottom)
                    start.linkTo(parent.start)
                }
                )

                Text(text = "0 Followers", style = TextStyle(
                    fontWeight = FontWeight.ExtraBold, fontSize = 24.sp
                ), modifier = Modifier.constrainAs(followers) {
                    top.linkTo(bio.bottom)
                    start.linkTo(parent.start)
                }
                )

                Text(text = "0 Following", style = TextStyle(
                    fontWeight = FontWeight.ExtraBold, fontSize = 24.sp
                ), modifier = Modifier.constrainAs(following) {
                    top.linkTo(followers.bottom)
                    start.linkTo(parent.start)
                }
                )

                ElevatedButton(onClick = {
//                    authViewModel.logout()
                }, modifier = Modifier.constrainAs(button) {
                    top.linkTo(following.bottom)
                    start.linkTo(parent.start)
                }
                ) {
                    Text(text = "Follow")

                }


            }
        }

        if (threads != null && users != null) {
            items(threads ?: emptyList()) { pair ->
                ThreadItem(
                    thread = pair,
                    users = users!!,
                    navHostController = navHostController,
                    userId = SharedPref.getuserName(context)
                )
            }
        }
    }


}