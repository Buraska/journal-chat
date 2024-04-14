package com.example.journalchat

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.List
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.Navigation
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.journalchat.ui.ChatListRoute
import com.example.journalchat.ui.ChatListScreen
import com.example.journalchat.ui.CreateChatRoute
import com.example.journalchat.ui.CreateChatScreen
import com.example.journalchat.ui.theme.Typography


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
