package com.example.journalchat

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.List
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.journalchat.ui.screens.ChatListScreen
import com.example.journalchat.ui.screens.ChatScreen
import com.example.journalchat.ui.screens.ChatScreenDestination
import com.example.journalchat.ui.screens.ChatCreationScreen
import com.example.journalchat.ui.screens.ChatCreationScreenDestination
import com.example.journalchat.ui.screens.ChatEditionDestination
import com.example.journalchat.ui.screens.ChatEditionScreen
import com.example.journalchat.ui.screens.ChatListScreenDestination

val homeRoute = ChatListScreenDestination.route

@Composable
fun JournalNavHost(
    navController: NavHostController = rememberNavController()
) {

    NavHost(
        navController = navController,
        startDestination = homeRoute,
    ) {

        composable(route = ChatListScreenDestination.route) {
            ChatListScreen(
                onCreateButton = { navController.navigate(ChatCreationScreenDestination.route) },
                exposeDrawer = { },
                onItemClicked = {chatId -> navController.navigate("${ChatScreenDestination.route}/$chatId") },
                onEditClicked = {chatId -> navController.navigate("${ChatEditionDestination.route}/$chatId")})
        }
        composable(route = ChatCreationScreenDestination.route) {
            ChatCreationScreen(navigateUp = { navController.navigateUpOrAtHome() })
        }
        composable(route = ChatScreenDestination.routeWithArgs,
            arguments = listOf(navArgument(ChatScreenDestination.chatIdArg) {
                type = NavType.LongType
            })
        ) {
            ChatScreen(navigateUp = { navController.navigateUpOrAtHome() })
        }
        composable(route = ChatEditionDestination.routeWithArgs,
            arguments = listOf(navArgument(ChatEditionDestination.chatIdArg) {
                type = NavType.LongType
            })
        ) {
            ChatEditionScreen(navigateUp = { navController.navigateUpOrAtHome() })
        }
    }
}

fun NavHostController.navigateUpOrAtHome() {
    if (!this.popBackStack()) {
        this.navigate(homeRoute)
    }
}

@Composable
fun AppBarNavigationIconBack(navigateUp: () -> Unit) {
    IconButton(onClick = navigateUp) {
        Icon(
            imageVector = Icons.Outlined.ArrowBack,
            contentDescription = stringResource(R.string.back)
        )
    }
}

@Composable
fun AppBarNavigationDrawer(exposeDrawer: () -> Unit) {
    IconButton(onClick = exposeDrawer) {
        Icon(
            imageVector = Icons.Outlined.List,
            contentDescription = stringResource(R.string.menu)
        )
    }
}

interface NavigationDestination {
    val route: String
}

