package com.example.notweshare

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
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
import androidx.navigation.NavBackStackEntry
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.compose.AppTheme
import com.example.exampleapplication.viewmodels.GroupViewModel.Companion.groupViewModel
import com.example.exampleapplication.viewmodels.UserViewModel.Companion.userViewModel
import com.example.notweshare.models.TabItem
import com.example.notweshare.notification.NotificationJobIntentService
import com.example.notweshare.notification.NotificationService
import com.example.notweshare.screens.GroupDetailsScreen
import com.example.notweshare.screens.HomeScreen
import com.example.notweshare.screens.LoginScreen
import com.example.notweshare.screens.NewExpenseScreen
import com.example.notweshare.screens.NewGroupScreen
import com.example.notweshare.screens.PermissionDialog
import com.example.notweshare.screens.ProfileScreen
import com.example.notweshare.screens.RegisterScreen
import com.google.firebase.FirebaseApp
import com.google.firebase.messaging.FirebaseMessaging

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val noti = NotificationService(applicationContext)
        FirebaseApp.initializeApp(this)
        getFireBaseToken()
        setContent {
            AppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background
                ) {
                    LoginAndRegister()
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        PermissionDialog()
                    }
                }
            }
        }
    }

    private fun getFireBaseToken() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                //save this token to the user and store in DB for late use.
                //dont know if we should check if the token is the same as previous every time the app launches
                val token = task.result
                Log.d("NotificationService", token)
                println(token)
                // Send this token to your server to identify and target specific devices
            } else {
                Log.e("FCM Token", "Failed to get token: ${task.exception}")
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
    val tabIndexes = mapOf<String, Int>(
        Screen.HomeScreen.route to 0,
        Screen.GroupDetailsScreen.route to 0,
        Screen.NewExpenseScreen.route to 0,
        Screen.NewGroupScreen.route to 1,
        Screen.ProfileScreen.route to 2,
    )

    val routes = tabIndexes.keys


    val tabBarHeight = with(LocalDensity.current) {
        65.sp.toDp()
    }

    val contentHeight = LocalConfiguration.current.screenHeightDp.dp - tabBarHeight

    Scaffold(bottomBar = {
        BottomAppBar(
            containerColor = MaterialTheme.colorScheme.onSecondaryContainer,
            contentColor = Color.White,
            modifier = Modifier
                .fillMaxWidth()
                .height(tabBarHeight)
        ) {
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentRoute = navBackStackEntry?.destination?.route

            if (currentRoute != null) {
                selectedTabIndex = tabIndexes[currentRoute] ?: 0
            }

            tabs.forEachIndexed { index, tab ->
                val tint =
                    ColorFilter.tint(if (selectedTabIndex == index) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onBackground)
                IconButton(
                    onClick = {
                        // This is the variable that must be changed to change the selected tab
                        selectedTabIndex = index
                        navController.navigate(tab.route)
                    }, modifier = Modifier.weight(1f)
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

    }) {
        NavHost(
            navController, startDestination = Screen.HomeScreen.route, modifier = Modifier.height(
                contentHeight
            )
        ) {
            with(navController) {
                composable(
                    route = Screen.HomeScreen.route,
                    enterTransition = {
                        slideIn(routes)
                    }, exitTransition = {
                        slideOut(routes)
                    }, popEnterTransition = {
                        slideIn(routes)
                    }, popExitTransition = {
                        slideOut(routes)
                    }) {
                    HomeScreen(
                        navigateToGroupDetails = {
                            navigate(
                                Screen.GroupDetailsScreen.route
                            )
                        },
                    )
                }
                composable(Screen.NewGroupScreen.route,
                    enterTransition = {
                        slideIn(routes)
                    }, exitTransition = {
                        slideOut(routes)
                    }, popEnterTransition = {
                        slideIn(routes)
                    }, popExitTransition = {
                        slideOut(routes)
                    }) {
                    NewGroupScreen(
                        navigateToGroups = {
                            navigate(
                                Screen.HomeScreen.route
                            )
                        },
                    )
                }
                composable(Screen.ProfileScreen.route,
                    enterTransition = {
                        slideIn(routes)
                    }, exitTransition = {
                        slideOut(routes)
                    }, popEnterTransition = {
                        slideIn(routes)
                    }, popExitTransition = {
                        slideOut(routes)
                    }) { ProfileScreen() }
                composable(
                    route = Screen.GroupDetailsScreen.route,
                    enterTransition = {
                        slideIn(routes)
                    }, exitTransition = {
                        slideOut(routes)
                    }, popEnterTransition = {
                        slideIn(routes)
                    }, popExitTransition = {
                        slideOut(routes)

                    }) {
                    GroupDetailsScreen(
                        navigateToNewExpense = {
                            navigate(
                                Screen.NewExpenseScreen.route
                            )
                        },
                        context = context,
                    )
                }
                composable(
                    route = Screen.NewExpenseScreen.route,
                    enterTransition = {
                        slideIn(routes)
                    }, exitTransition = {
                        slideOut(routes)
                    }, popEnterTransition = {
                        slideIn(routes)
                    }, popExitTransition = {
                        slideOut(routes)
                    }) {
                    NewExpenseScreen(
                        navigateUp = {
                            navigateUp()
                        },
                    )
                }
            }

            // THIS FOLLOWING LINE MUST BE THERE FOR THE LINTER TO WORK
            val something = it
        }
    }
}

val animationTime = 400
val easing = CubicBezierEasing(0.65f, 0f, 0.35f, 1f)
private fun AnimatedContentTransitionScope<NavBackStackEntry>.slideIn(
    routes: Set<String>,
): EnterTransition {
    val direction = slideDirection(routes, false)

    return slideIntoContainer(
        towards = direction,
        animationSpec = tween(animationTime, easing = easing)
    )
}


private fun AnimatedContentTransitionScope<NavBackStackEntry>.slideOut(
    routes: Set<String>
): ExitTransition {
    val direction = slideDirection(routes)

    return slideOutOfContainer(
        towards = direction,
        animationSpec = tween(animationTime, easing = easing)
    )
}

private fun AnimatedContentTransitionScope<NavBackStackEntry>.slideDirection(
    routes: Set<String>,
    exit: Boolean = true
): AnimatedContentTransitionScope.SlideDirection {
    val targetRoute = this.targetState.destination.route
    val targetIndex = routes.indexOf(targetRoute)
    val currentRoute = this.initialState.destination.route
    val currentIndex = routes.indexOf(currentRoute)

    Log.d(
        "animation",
        "targetRoute: $targetRoute, targetIndex: $targetIndex, currentRoute: $currentRoute, currentIndex: $currentIndex"
    )

    val direction = if (targetIndex > currentIndex) {
        AnimatedContentTransitionScope.SlideDirection.Left
    } else {
        AnimatedContentTransitionScope.SlideDirection.Right
    }
    return direction
}

@Composable
fun LoginAndRegister() {
    val navController = rememberNavController()

    val routes = setOf(
        Screen.LoginScreen.route,
        Screen.RegisterScreen.route,
        Screen.MainScreen.route,
    )

    Scaffold(
    ) {
        NavHost(
            navController,
            startDestination = Screen.LoginScreen.route,
        ) {
            with(navController) {
                composable(Screen.LoginScreen.route,
                    enterTransition = {
                        slideIn(routes)
                    }, exitTransition = {
                        slideOut(routes)
                    }, popEnterTransition = {
                        slideIn(routes)
                    }, popExitTransition = {
                        slideOut(routes)
                    }) {
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
                composable(Screen.RegisterScreen.route,
                    enterTransition = {
                        slideIn(routes)
                    }, exitTransition = {
                        slideOut(routes)
                    }, popEnterTransition = {
                        slideIn(routes)
                    }, popExitTransition = {
                        slideOut(routes)
                    }) {
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
                composable(Screen.MainScreen.route,
                    enterTransition = {
                        slideIn(routes)
                    }, exitTransition = {
                        slideOut(routes)
                    }, popEnterTransition = {
                        slideIn(routes)
                    }, popExitTransition = {
                        slideOut(routes)
                    }) {
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

