package com.example.journalchat

import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.List
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.journalchat.models.Chat
import com.example.journalchat.ui.ChatListScreen
import com.example.journalchat.ui.CreateChat
import com.example.journalchat.ui.CreateChatScreen
import com.example.journalchat.ui.theme.JournalChatTheme
import com.example.journalchat.ui.theme.Typography

enum class JournalChatScreen(@StringRes val title: Int) {
    Start(R.string.app_name),
    CreateChat(R.string.create_chat)
}




@Composable
fun FloatingBottomButton(
    imageVector: ImageVector,
    buttonDescription: String,
    isVisible: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val density = LocalDensity.current
    AnimatedVisibility(
        visible = isVisible,
        enter = slideInVertically { with(density) { 100.dp.roundToPx() } },
        exit = slideOutVertically { with(density) { (120.dp).roundToPx() } },
        modifier = modifier.safeDrawingPadding()
    ) {
        FloatingActionButton(
            onClick = onClick,
            contentColor = MaterialTheme.colorScheme.background,
            containerColor = MaterialTheme.colorScheme.secondary,
            modifier = Modifier
                .padding(16.dp)
        ) {

            Icon(
                imageVector = imageVector,
                contentDescription = buttonDescription,
                modifier = Modifier.size(32.dp)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBar(
    title: String,
    navIcon: @Composable () -> Unit,
    topAppBarScrollBehavior: TopAppBarScrollBehavior,
    modifier: Modifier = Modifier
) {
    CenterAlignedTopAppBar(
        title = { Text(text = title, style = Typography.displayLarge) },
        navigationIcon = navIcon,
        actions = {
            IconButton(onClick = { /*TODO*/ }) {
                Icon(
                    imageVector = Icons.Outlined.Search, contentDescription = stringResource(
                        R.string.search
                    )
                )
            }
        },
        scrollBehavior = topAppBarScrollBehavior,
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    JournalChatTheme(useDarkTheme = false) {
//        JournalChatApp()
    }
}