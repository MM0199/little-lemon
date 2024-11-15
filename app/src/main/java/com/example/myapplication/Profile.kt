package com.example.myapplication

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.myapplication.ui.theme.buttonBackground
import com.example.myapplication.ui.theme.green

@Composable
fun Profile(navController: NavController) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val context = LocalContext.current
    val sharedPreferences = context.getSharedPreferences("user_data", Context.MODE_PRIVATE)

    Column(
        modifier = Modifier.fillMaxSize(),
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription ="Logo",
            modifier = Modifier
                .width(200.dp)
                .height(100.dp)
                .align(Alignment.CenterHorizontally)
                .padding(top = 5.dp)
        )
        Text(
            text = "Profile information",
            fontSize = 25.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(top = 200.dp, bottom = 50.dp, start = 10.dp)
        )
        Column {
            Text(
                text = "First name: ${sharedPreferences.getString("first_name", "")}",
                fontSize = 20.sp,
                color = Color.Black,
                modifier = Modifier
                    .padding(start = 10.dp, end = 10.dp, top = 10.dp, bottom = 25.dp)
                    .fillMaxWidth()
            )
            Text(
                text = "Last name: ${sharedPreferences.getString("last_name", "")}",
                fontSize = 20.sp,
                color = Color.Black,
                modifier = Modifier
                    .padding(start = 10.dp, end = 10.dp, top = 10.dp, bottom = 25.dp)
            )
            Text(
               text = "Email: ${sharedPreferences.getString("email", "")}",
                fontSize = 20.sp,
                color = Color.Black,
                modifier = Modifier
                    .padding(10.dp)
                    .fillMaxWidth()
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        Button(
            onClick = {
                navController.navigate(Onboarding.route)
                sharedPreferences.edit().clear().apply()
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = if(isPressed) green else buttonBackground,
                contentColor = Color.Black),
            interactionSource = interactionSource,
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier
                .height(70.dp)
                .padding(start = 10.dp, end = 10.dp, bottom = 20.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = "Log out",
                fontSize = 18.sp
            )
        }
    }
}