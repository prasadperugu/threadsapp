package com.example.threadsclone.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType


import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import androidx.navigation.NavHostController
import com.example.threadsclone.navigation.Routes


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Login(navController: NavHostController){


    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }


    Column(modifier = Modifier
        .fillMaxSize()
        .padding(24.dp)
        , horizontalAlignment = Alignment.CenterHorizontally
        , verticalArrangement = Arrangement.Center

    ) {
        Text(text = "Login", style = TextStyle(
            fontWeight = FontWeight.ExtraBold
            , fontSize = 24.sp
        ))

        Box(modifier = Modifier.height(50.dp))
        OutlinedTextField(
            value = email
            , onValueChange = {email = it}
            , label = {
                Text(text = "Email")
    }, keyboardOptions = KeyboardOptions(
        keyboardType = KeyboardType.Email
    ), singleLine = true
            , modifier = Modifier.fillMaxWidth()
        )
        Box(modifier = Modifier.height(20.dp))
        OutlinedTextField(
            value = password
            , onValueChange = {password = it}
            , label = {
                Text(text = "Password")
            }, keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password
            ), singleLine = true
            , modifier = Modifier.fillMaxWidth()
        )
        Box(modifier = Modifier.height(30.dp))

        ElevatedButton(onClick = {
            
        }, modifier = Modifier.fillMaxWidth()) {
          Text(text = "Login"
          , style = TextStyle(
              fontWeight = FontWeight.ExtraBold
                  , fontSize = 24.sp
          ), modifier = Modifier.padding(vertical = 6.dp))
        }

        TextButton(onClick = {
            navController.navigate(Routes.Register.routes) {
                popUpTo(navController.graph.startDestinationId)
                launchSingleTop = true
            }

        }, modifier = Modifier.fillMaxWidth()) {
            Text(text = "New User? Create Account"
                , style = TextStyle(
                    fontSize = 16.sp
                ))
        }
    }

}

@Preview(showBackground = true)
@Composable
fun LoginView(){
//    Login()
}