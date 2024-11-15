package com.example.myapplication

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyNavigation()
        }
    }
}

@Composable
fun MyNavigation() {
    val context = LocalContext.current
    val sharedPreferences = context.getSharedPreferences("user_data", MODE_PRIVATE)
    val startDestination = if (sharedPreferences.contains("first_name")) Home.route else Onboarding.route
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = startDestination
    ){
        composable(Onboarding.route) { Onboarding(navController) }
        composable(Home.route) { Home(navController) }
        composable(Profile.route) { Profile(navController) }
    }
}
