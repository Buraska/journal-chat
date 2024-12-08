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
import com.example.journalchat.ui.ChatScreen
import com.example.journalchat.ui.ChatScreenDestination
import com.example.journalchat.ui.routes.ChatListRoute
import com.example.journalchat.ui.routes.CreateChatRoute

enum class JournalChatScreen(val title: String) {
    Start("start"),
    CreateChat("create_chat"),
    Chat("chat/{chatName}")
}




@Composable
fun JournalNavHost(
    navController: NavHostController = rememberNavController()
) {
        NavHost(
            navController = navController,
            startDestination = JournalChatScreen.Start.title,
        ) {

            composable(route = JournalChatScreen.Start.title) {
                ChatListRoute(
                    onCreateButton =  { navController.navigate(JournalChatScreen.CreateChat.title) },
                    exposeDrawer = { },
                    onItemClicked = {chatId -> navController.navigate("chat/$chatId") })
            }
            composable(route = JournalChatScreen.CreateChat.title) {
                CreateChatRoute(navigateUp =  { navController.navigateUpOrAtHome()})
            }
            composable(route = ChatScreenDestination.routeWithArgs,
                arguments = listOf(navArgument(ChatScreenDestination.chatIdArg){type = NavType.LongType})) {
                ChatScreen(navigateUp = { navController.navigateUpOrAtHome()})
            }
        }
}

fun NavHostController.navigateUpOrAtHome(){
    if (!this.popBackStack()){
        this.navigate(JournalChatScreen.Start.name)
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
fun AppBarNavigationDrawer(exposeDrawer: () -> Unit){
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

