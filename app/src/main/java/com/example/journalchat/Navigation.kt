package com.example.journalchat

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
import com.example.journalchat.ui.routes.ChatListRoute
import com.example.journalchat.ui.routes.CreateChatRoute


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JournalNavHost(
    navController: NavHostController = rememberNavController()
) {
        NavHost(
            navController = navController,
            startDestination = JournalChatScreen.Start.name,
        ) {
            composable(route = JournalChatScreen.Start.name) {
                ChatListRoute(
                    onCreateButton =  { navController.navigate(JournalChatScreen.CreateChat.name) },
                    topBarNavIcon = { AppBarNavigationDrawer(exposeDrawer = {})})
            }
            composable(route = JournalChatScreen.CreateChat.name) {
                CreateChatRoute(topBarNavIcon =  { AppBarNavigationIconBack(navigateUp = {navController.navigateUp()})})
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
