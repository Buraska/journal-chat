package com.example.journalchat

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.List
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.journalchat.ui.ChatScreen
import com.example.journalchat.ui.routes.ChatListRoute
import com.example.journalchat.ui.routes.ChatRoute
import com.example.journalchat.ui.routes.CreateChatRoute

enum class JournalChatScreen(@StringRes val title: Int) {
    Start(R.string.app_name),
    CreateChat(R.string.create_chat),
    Chat(R.string.chat)
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JournalNavHost(
    navController: NavHostController = rememberNavController()
) {
        NavHost(
            navController = navController,
            startDestination = JournalChatScreen.Chat.name,
        ) {

            //TODO Two different approaches to send navigation up function. Choose one
            composable(route = JournalChatScreen.Start.name) {
                ChatListRoute(
                    onCreateButton =  { navController.navigate(JournalChatScreen.CreateChat.name) },
                    exposeDrawer = { },
                    onItemClicked = {})
            }
            composable(route = JournalChatScreen.CreateChat.name) {
                CreateChatRoute(navigateUp =  {navController.navigateUp()})
            }
            composable(route = JournalChatScreen.Chat.name) {
                ChatRoute(navigateUp = {navController.navigateUp()})
            }
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
