package com.example.myapplication

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.myapplication.ui.theme.Typography
import com.example.myapplication.ui.theme.green
import com.example.myapplication.ui.theme.highlight

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Home(navController: NavController, database: AppDatabase) {
    val catergories by database.menuItemDao().getCategories().observeAsState(emptyList())
    val menuItems by database.menuItemDao().getAll().observeAsState(emptyList())
    val activeCategory = remember { mutableStateOf<String?>(null) }
    var searchPhrase by remember { mutableStateOf("") }
    val catergoryFilter = remember { mutableStateOf("") }
    Column {
        TopAppBar(
            title = {
                Image(
                    painter = painterResource(id = R.drawable.logo),
                    contentDescription = "Logo",
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentSize(Alignment.Center)
                        .aspectRatio(0.2f)
                )
            },
            navigationIcon = {},
            actions = {
                Image(
                    painter = painterResource(id = R.drawable.profile),
                    contentDescription = "Profile",
                    modifier = Modifier
                        .size(50.dp)
                        .clickable(onClick = { navController.navigate(Profile.route) })
                )
            },
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = green)
        ) {
            Text(
                text = "Little Lemon",
                style = Typography.titleLarge,
                modifier = Modifier
                    .padding(top = 20.dp, start = 10.dp)
            )
            Text(
                text = "Chicago",
                style = Typography.labelLarge,
                modifier = Modifier
                    .padding(start = 10.dp)
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "We are a family owned Mediterranean restaurant, focused on traditional recipes served with a modern twist.",
                    style = Typography.bodyMedium,
                    modifier = Modifier
                        .padding(top = 20.dp, start = 10.dp, end = 10.dp)
                        .weight(1f, false)
                )
                Image(
                    painter = painterResource(id = R.drawable.hero),
                    contentDescription = "Hero",
                    modifier = Modifier
                        .height(190.dp)
                        .width(170.dp)
                        .weight(1f, false)
                        .padding(bottom = 30.dp, end = 10.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .align(Alignment.CenterVertically),
                    contentScale = ContentScale.Crop
                )
            }
            TextField(
                value = searchPhrase,
                onValueChange = { searchPhrase = it; catergoryFilter.value = ""; activeCategory.value = null },
                label = { Text(text = "Enter search phrase") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search"
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 20.dp, start = 10.dp, end = 10.dp),
                shape = RoundedCornerShape(10.dp),
            )
        }
        Text(
            text = "ORDER FOR DELIVERY!",
            style = Typography.labelMedium,
            modifier = Modifier
                .padding(top = 20.dp, start = 10.dp)
        )
        MenuCategories(catergories.toSet(), activeCategory, catergoryFilter)
        HorizontalDivider(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 10.dp),
            thickness = 1.dp,
            color = Color.Gray
        )
        val filterItem = if(searchPhrase.isNotEmpty()) {
            menuItems.filter { it.title.contains(searchPhrase, ignoreCase = true) }
        } else if (catergoryFilter.value.isNotEmpty()) {
            menuItems.filter { it.category.contains(catergoryFilter.value, ignoreCase = true) }
        } else {
            menuItems
        }
        MenuItem(filterItem)
    }
}

@Composable
private fun MenuCategories(
    category: Set<String>,
    activeCategory: MutableState<String?>,
    catergoryFilter: MutableState<String>
) {
    LazyRow(
        horizontalArrangement = Arrangement.SpaceEvenly,
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
    ){
        items(category.toList()) { item ->
            Button(
                onClick = {
                   activeCategory.value = if(activeCategory.value == item) null else item
                    catergoryFilter.value = if(catergoryFilter.value == item) "" else item
                },
                colors = ButtonColors(
                    containerColor = if(activeCategory.value == item) green else highlight,
                    disabledContainerColor = Color.Transparent,
                    contentColor = if(activeCategory.value == item) highlight else green,
                    disabledContentColor = Color.Transparent
                ),
                modifier = Modifier
                    .padding(end = 10.dp)
                    .clip(RoundedCornerShape(10.dp))
            ) {
                Text(
                    text = item,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
            }
        }
    }
}
@Composable
private fun MenuItem(menuItems: List<MenuItem>) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(10.dp),
        modifier = Modifier
            .fillMaxSize()
    ) {
        items(menuItems) { item ->
            Row(
                modifier = Modifier
                    .fillMaxSize(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Column(
                    modifier = Modifier
                        .padding(10.dp)
                        .weight(1f, false)
                ) {
                    Text(
                        text = item.title,
                        fontWeight = FontWeight.Bold,
                        fontSize = 22.sp,
                        modifier = Modifier
                            .padding(bottom = 20.dp)
                    )
                    Text(
                        text = item.description,
                        fontSize = 20.sp,
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 2,
                        modifier = Modifier
                            .padding(bottom = 20.dp)
                    )
                    Text(
                        text = "$${String.format("%.2f", item.price.toDouble())}",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 18.sp,
                        modifier = Modifier
                            .padding(bottom = 10.dp)
                    )
                }
                AsyncImage(
                    model = item.image,
                    contentDescription = item.title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(120.dp)
                        .fillMaxSize()
                        .weight(1f, false)
                        .padding(10.dp)
                        .align(Alignment.CenterVertically)
                )
            }
            HorizontalDivider(
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 10.dp),
                thickness = 1.dp,
                color = Color.Gray
            )
        }
    }
}