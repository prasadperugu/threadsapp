package com.example.threadsclone.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import coil.compose.rememberAsyncImagePainter
import com.example.threadsclone.R
import com.example.threadsclone.utils.SharedPref

@Composable
fun AddThreads() {

    val context = LocalContext.current

    var thread by remember {
        mutableStateOf("")
    }

    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        val (
            crossPic, text, logo, userName, editText, attachMedia, replyText, button, imageBox
        ) = createRefs()
        Image(
            painter = painterResource(id = R.drawable.baseline_close_24),
            contentDescription = "close",
            modifier = Modifier
                .constrainAs(crossPic)
                {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)

                }
                .clickable {


                })

        Text(text = "Add Thread", style = TextStyle(
            fontWeight = FontWeight.ExtraBold, fontSize = 24.sp
        ), modifier = Modifier.constrainAs(text) {
            top.linkTo(crossPic.top)
            start.linkTo(crossPic.end, margin = 12.dp)
            bottom.linkTo(crossPic.bottom)
        }
        )

        Image(
            painter = painterResource(id = R.drawable.baseline_close_24),
            contentDescription = "close",
            modifier = Modifier.constrainAs(logo)
            {
                top.linkTo(text.bottom)
                start.linkTo(parent.start)

            }.size(36.dp).clip(CircleShape)
            , contentScale = ContentScale.Crop
        )

        Text(text =
//        "text"
        SharedPref.getuserName(context)
            , style = TextStyle(
             fontSize = 20.sp
        ), modifier = Modifier.constrainAs(userName) {
            top.linkTo(logo.top)
            start.linkTo(logo.end, margin = 12.dp)
            bottom.linkTo(logo.bottom)
        }
        )

        BasicTextFieldWithHint(
             hint = "Start a thread..."
            , value = thread
            , onValueChange = {thread = it}
            , modifier = Modifier.constrainAs(editText){
                top.linkTo(userName.bottom)
                start.linkTo(userName.start)
                end.linkTo(parent.end)
            }.padding(horizontal = 8.dp, vertical = 8.dp)
        )


    }

}

@Composable
fun BasicTextFieldWithHint(
    hint: String
    ,value:String
    ,onValueChange: (String) -> Unit
    , modifier: Modifier
){

    Box(modifier = modifier){
        if (value.isEmpty()){
            Text(text = hint, color = Color.Gray)
        }
        BasicTextField(value = value,onValueChange = onValueChange
        , textStyle = TextStyle.Default.copy(color = Color.Black)
            ,modifier = Modifier.fillMaxWidth()
        ) {


            
        }
    }

}


@Preview(showBackground = true)
@Composable
fun AddPostView() {
    AddThreads()
}
