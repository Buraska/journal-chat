package com.example.journalchat

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.List
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.journalchat.ui.routes.ChatListRoute
import com.example.journalchat.ui.routes.ChatRoute
import com.example.journalchat.ui.routes.CreateChatRoute

enum class JournalChatScreen(val title: String) {
    Start("start"),
    CreateChat("create_chat"),
    Chat("chat/{chatId}")
}




@Composable
fun JournalNavHost(
    navController: NavHostController = rememberNavController()
) {
        NavHost(
            navController = navController,
            startDestination = JournalChatScreen.Chat.title,
        ) {

            composable(route = JournalChatScreen.Start.title) {
                ChatListRoute(
                    onCreateButton =  { navController.navigate(JournalChatScreen.CreateChat.title) },
                    exposeDrawer = { },
                    onItemClicked = {chatName -> navController.navigate("chat/$chatName") })
            }
            composable(route = JournalChatScreen.CreateChat.title) {
                CreateChatRoute(navigateUp =  { navController.navigateUpOrAtHome()})
            }
            composable(route = JournalChatScreen.Chat.title) {
                ChatRoute(navigateUp = { navController.navigateUpOrAtHome()}, it.arguments?.getString("chatName"))
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
