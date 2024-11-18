package com.example.myapplication

import android.content.Context.MODE_PRIVATE
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.http.ContentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    private val client = HttpClient {
        install(ContentNegotiation) {
            json(contentType = ContentType("text", "plain"))
        }
    }

    private val database by lazy {
        AppDatabase.getDatabase(this)
    }

    private suspend fun getMenu(): List<MenuItemNetwork> {
        return client.get("https://raw.githubusercontent.com/Meta-Mobile-Developer-PC/Working-With-Data-API/main/menu.json")
            .body<MenuNetworkData>()
            .menu
    }

    private fun saveMenuTodatabase(menuItems: List<MenuItemNetwork>) {
        val menuItem = menuItems.map { it.toMenuItem() }
        database.menuItemDao().insertAll(menuItem)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyNavigation(database)
        }
        lifecycleScope.launch(Dispatchers.IO) {
            if (database.menuItemDao().isEmpty()) {
                val menuItems = getMenu()
                saveMenuTodatabase(menuItems)
            }
        }
    }

}

@Composable
fun MyNavigation(database: AppDatabase) {
    val context = LocalContext.current
    val sharedPreferences = context.getSharedPreferences("user_data", MODE_PRIVATE)
    val startDestination = if (sharedPreferences.contains("first_name")) Home.route else Onboarding.route
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = startDestination
    ){
        composable(Onboarding.route) { Onboarding(navController) }
        composable(Home.route) { Home(navController, database) }
        composable(Profile.route) { Profile(navController) }
    }
}
