package com.example.notweshare

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.compose.AppTheme
import com.example.exampleapplication.viewmodels.GroupViewModel.Companion.groupViewModel
import com.example.exampleapplication.viewmodels.UserViewModel.Companion.userViewModel
import com.example.notweshare.models.TabItem
import com.example.notweshare.notification.NotificationService
import com.example.notweshare.screens.HomeScreen
import com.example.notweshare.screens.NewExpenseScreen
import com.example.notweshare.screens.GroupDetailsScreen
import com.example.notweshare.screens.NewGroupScreen
import com.example.notweshare.screens.ProfileScreen
import com.example.notweshare.screens.LoginScreen
import com.example.notweshare.screens.RegisterScreen

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val noti = NotificationService(applicationContext)

        setContent {
            AppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier
                        .fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    LoginAndRegister()
                }
            }
        }
    }
}

@Composable
fun Navigation() {
    val navController = rememberNavController()
    var selectedTabIndex by remember { mutableStateOf(0) }
    val tabs = listOf(
        TabItem("Groups", Screen.HomeScreen.route, R.drawable.group),
        TabItem("Create Group", Screen.NewGroupScreen.route, R.drawable.square_add),
        TabItem("Profile", Screen.ProfileScreen.route, R.drawable.profile)
    )


    val tabBarHeight = with(LocalDensity.current) {
        65.sp.toDp()
    }

    val contentHeight = LocalConfiguration.current.screenHeightDp.dp - tabBarHeight

    // If We wish to change the highlighted tab, we should probably do it by modifiying an external variable

    Scaffold(
        bottomBar = {
            BottomAppBar(
                containerColor = MaterialTheme.colorScheme.onSecondaryContainer,
                contentColor = Color.White,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(tabBarHeight)
            ) {
                tabs.forEachIndexed { index, tab ->
                    val tint =
                        ColorFilter.tint(if (selectedTabIndex == index) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onBackground)
                    IconButton(
                        onClick = {
                            // This is the variable that must be changed to change the selected tab
                            selectedTabIndex = index
                            navController.navigate(tab.route)
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Bottom
                        ) {
                            Image(
                                colorFilter = tint,
                                painter = painterResource(tab.icon),
                                contentDescription = null,
                                modifier = Modifier.size(24.dp)
                            )
                            Text(
                                text = tab.title,
                                style = MaterialTheme.typography.bodySmall,
                                color = if (selectedTabIndex == index) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onBackground,
                            )
                        }
                    }
                }
            }

        }
    ) {
        NavHost(
            navController,
            startDestination = Screen.HomeScreen.route,
            modifier = Modifier.height(
                contentHeight
            )
        ) {
            with(navController) {
                composable(Screen.HomeScreen.route) {
                    HomeScreen(
                        navigateToGroupDetails = {
                            navigate(
                                Screen.GroupDetailsScreen.route
                            )
                        },
                    )
                }
                composable(Screen.NewGroupScreen.route) {
                    NewGroupScreen(
                        navigateToGroups = {
                            navigate(
                                Screen.HomeScreen.route
                            )
                        },
                    )
                }
                composable(Screen.ProfileScreen.route) { ProfileScreen() }
                composable(Screen.GroupDetailsScreen.route) {
                    GroupDetailsScreen(
                        navigateToNewExpense = {
                            navigate(
                                Screen.NewExpenseScreen.route
                            )
                        },
                        context = context,
                    )
                }
                composable(Screen.NewExpenseScreen.route) {
                    NewExpenseScreen(
                        navigateUp = {
                            navigateUp()
                        },
                    )
                }
            }

        }

        // THIS FOLLOWING LINE MUST BE THERE FOR THE LINTER TO WORK
        val something = it
    }
}

@Composable
fun LoginAndRegister() {
    val navController = rememberNavController()
    Scaffold(
    ) {
        NavHost(
            navController,
            startDestination = Screen.LoginScreen.route,
        ) {
            with(navController) {
                composable(Screen.LoginScreen.route) {
                    LoginScreen(
                        navigateToRegister = {
                            navigate(
                                Screen.RegisterScreen.route
                            )
                        },
                        navigateToMain = {
                            // Find groups with the user as a member
                            groupViewModel.findGroupsWithMember(userViewModel.activeUser.value.documentID)
                            navigate(
                                Screen.MainScreen.route
                            )
                        },
                    )
                }
                composable(Screen.RegisterScreen.route) {
                    RegisterScreen(
                        navigateToMain = {
                            // Find groups with the user as a member
                            groupViewModel.findGroupsWithMember(userViewModel.activeUser.value.documentID)
                            navigate(
                                Screen.MainScreen.route
                            )
                        },
                        navigateToLogin = {
                            navigate(
                                Screen.LoginScreen.route
                            )
                        },
                    )
                }
                composable(Screen.MainScreen.route) {
                    Navigation()
                }
            }

        }

        // THIS FOLLOWING LINE MUST BE THERE FOR THE LINTER TO WORK
        val something = it
    }

}

sealed class Screen(val route: String) {
    object MainScreen : Screen("MainScreen")
    object HomeScreen : Screen("HomeScreen")
    object NewGroupScreen : Screen("NewScreen")
    object ProfileScreen : Screen("ProfileScreen")
    object GroupDetailsScreen : Screen("GroupDetailsScreen")
    object NewExpenseScreen : Screen("NewExpenseScreen")
    object LoginScreen : Screen("LoginScreen")
    object RegisterScreen : Screen("RegisterScreen")
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    AppTheme {
        Navigation()
    }
}
