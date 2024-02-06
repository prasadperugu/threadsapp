package com.example.threadsclone.screens

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect

import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.threadsclone.R
import com.example.threadsclone.navigation.Routes
import com.example.threadsclone.viewmodel.AuthViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Register(navHostController: NavHostController) {
    var email by remember {
        mutableStateOf("")
    }
    var name by remember {
        mutableStateOf("")
    }

    var userName by remember {
        mutableStateOf("")
    }

    var bio by remember {
        mutableStateOf("")
    }
    var password by remember {
        mutableStateOf("")
    }

    var imageUri by remember {
        mutableStateOf<Uri?>(null)
    }

    val authViewModel: AuthViewModel = viewModel()
    val firebaseUser by authViewModel.firebaseUser.observeAsState(null)

    val permissionToRequest = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        Manifest.permission.READ_MEDIA_IMAGES
    } else {
        Manifest.permission.READ_EXTERNAL_STORAGE
    }
    val context = LocalContext.current

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        imageUri = uri
    }
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {


        } else {

        }
    }



    //launchedeffect is not hitting if we click register here
    LaunchedEffect(firebaseUser) {
        //when click on New User?Crate Account  launchedeffect has been calling only for first time.
        Log.i("Insidefirebase_","Yes")
        if (firebaseUser!=null) {
            Log.i("Insidefirebase_1","Yes")
            navHostController.navigate(Routes.BottomNav.routes) {
                Log.i("Insidefirebase_2","Yes")
                popUpTo(navHostController.graph.startDestinationId)
                launchSingleTop = true
            }
        }
        else{
            Log.i("ErrorCheckcase","Something went wrong")
        }
    }




    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center

    ) {
        Text(
            text = "Register here", style = TextStyle(
                fontWeight = FontWeight.ExtraBold, fontSize = 24.sp
            )
        )
        Box(modifier = Modifier.height(25.dp))

        Image(
            painter = if (imageUri == null) painterResource(id = R.drawable.man)
            else rememberAsyncImagePainter(model = imageUri), contentDescription = "Person",
            modifier = Modifier
                .size(96.dp)
                .clip(CircleShape)
                .background(Color.LightGray)
                .clickable {

                    val isGranted = ContextCompat.checkSelfPermission(
                        context, permissionToRequest
                    ) == PackageManager.PERMISSION_GRANTED

                    if (isGranted) {
                        launcher.launch("image/*")
                    } else {
                        permissionLauncher.launch(permissionToRequest)
                    }
                },
            contentScale = ContentScale.Crop
        )


        Box(modifier = Modifier.height(25.dp))

        OutlinedTextField(
            value = name, onValueChange = { name = it }, label = {
                Text(text = "Name")
            }, keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text
            ), singleLine = true, modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = userName, onValueChange = { userName = it }, label = {
                Text(text = "Username")
            }, keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text
            ), singleLine = true, modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = bio, onValueChange = { bio = it }, label = {
                Text(text = "Bio")
            }, keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email
            ), singleLine = true, modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = email, onValueChange = { email = it }, label = {
                Text(text = "Email")
            }, keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email
            ), singleLine = true, modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = password, onValueChange = { password = it }, label = {
                Text(text = "Password")
            }, keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password
            ), singleLine = true, modifier = Modifier.fillMaxWidth()
        )
        Box(modifier = Modifier.height(30.dp))

        ElevatedButton(onClick = {

            if (name.isEmpty()
                || email.isEmpty()
                || bio.isEmpty()
                || password.isEmpty()
                || imageUri == null
            ) {
                Log.i("searchingforbio", "rgistrationcode")
                Toast.makeText(
                    context, "Please fill all details", Toast.LENGTH_SHORT
                ).show()
            } else {
                Log.i("searchingfor0", "rgistrationcode")
                authViewModel.register(
                    email, password, name, bio, userName, imageUri!!, context
                )
            }


        }, modifier = Modifier.fillMaxWidth()) {
            Text(
                text = "Register here", style = TextStyle(
                    fontWeight = FontWeight.ExtraBold, fontSize = 24.sp
                ), modifier = Modifier.padding(vertical = 6.dp)
            )
        }

        TextButton(onClick = {
            navHostController.navigate(Routes.Login.routes) {
                popUpTo(navHostController.graph.startDestinationId)
                launchSingleTop = true
            }

        },modifier = Modifier.fillMaxWidth()) {
            Text(text = "Already Register? Login here"
                , style = TextStyle(
                    fontSize = 16.sp
                ))
        }
    }

}

@Preview(showBackground = true)
@Composable
fun RegisterView() {
//    Register()
}
