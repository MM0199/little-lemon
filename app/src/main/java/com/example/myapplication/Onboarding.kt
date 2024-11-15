package com.example.myapplication

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
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
import androidx.compose.runtime.setValue
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
import com.example.myapplication.ui.theme.textBackground

fun saveUserData(context: Context, firstName: String, lastName: String, email: String) {
    val sharedPreferences = context.getSharedPreferences("user_data", Context.MODE_PRIVATE)
    val editor = sharedPreferences.edit()
    editor.putString("first_name", firstName)
    editor.putString("last_name", lastName)
    editor.putString("email", email)
    editor.apply()
}

@Composable
fun Onboarding(navController: NavController) {
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var showDialog by remember { mutableStateOf(false) }
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val context = LocalContext.current
    
    Column(modifier = Modifier.fillMaxSize()) {
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
            text = "Let's get to know you",
            fontSize = 30.sp,
            color = Color.White,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .background(textBackground)
                .padding(top = 50.dp)
                .fillMaxWidth()
                .height(80.dp)
        )
        Text(
            text = "Personal information",
            fontSize = 25.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(top = 80.dp, bottom = 50.dp, start = 10.dp)
        )
        Column {
            OutlinedTextField(
                value = firstName,
                onValueChange = {firstName = it},
                label = { Text("First Name")},
                placeholder = { Text("Enter your first name")},
                singleLine = true,
                modifier = Modifier
                    .padding(start = 10.dp, end = 10.dp, top = 10.dp, bottom = 25.dp)
                    .fillMaxWidth()
            )
            OutlinedTextField(
                value = lastName,
                onValueChange = {lastName = it},
                label = { Text("Last Name")},
                placeholder = { Text("Enter your last name")},
                singleLine = true,
                modifier = Modifier
                    .padding(start = 10.dp, end = 10.dp, top = 10.dp, bottom = 25.dp)
                    .fillMaxWidth()
            )
            OutlinedTextField(
                value = email,
                onValueChange = {email = it},
                label = { Text("Email")},
                placeholder = { Text("Enter your email")},
                singleLine = true,
                modifier = Modifier
                    .padding(10.dp)
                    .fillMaxWidth()
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        Button(
            onClick = {
                if(firstName.isNotEmpty() && lastName.isNotEmpty() && email.isNotEmpty()){
                    saveUserData(context = context, firstName, lastName, email)
                    navController.navigate(Home.route)
                } else {
                    showDialog = true
                }
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = if(isPressed) Color.Blue else buttonBackground,
                contentColor = Color.Black),
            interactionSource = interactionSource,
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier
                .height(70.dp)
                .padding(start = 10.dp, end = 10.dp, bottom = 20.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = "Register",
                fontSize = 18.sp
            )
        }

        if (showDialog) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                title = { Text("Error") },
                text = { Text("Registration unsuccessful. Please enter all data.") },
                confirmButton = {
                    Button(
                        onClick = { showDialog = false }) {
                        Text("OK")
                    }
                }
            )
        }
    }
}
