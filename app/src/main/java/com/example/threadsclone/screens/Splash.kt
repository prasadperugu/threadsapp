package com.example.threadsclone.screens




import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavHostController
import com.example.threadsclone.R
import com.example.threadsclone.navigation.Routes
import kotlinx.coroutines.delay


@Composable
fun Splash(navController: NavHostController) {



    ConstraintLayout(modifier = Modifier.fillMaxSize()) {
        val (image) = createRefs()
        Image(
            painter = painterResource(id = R.drawable.logo)
            , contentDescription = "logo"
             ,modifier = Modifier.constrainAs(image){
                top.linkTo(parent.top)

    })

    }

    LaunchedEffect(true){
        delay(3000)

        navController.navigate(Routes.BottomNav.routes)
    }


}