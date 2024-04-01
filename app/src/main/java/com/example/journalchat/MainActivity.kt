package com.example.journalchat

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
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
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.List
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.journalchat.models.Data
import com.example.journalchat.ui.theme.JournalChatTheme
import com.example.journalchat.ui.theme.Typography
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalDensity
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.journalchat.ui.ChatList
import com.example.journalchat.ui.CreateChat

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            JournalChatTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    JournalChatApp()
                }
            }
        }
    }
}
enum class JournalChatScreen(@StringRes title: Int){
    Start(R.string.app_name),
    CreateChat(R.string.create_chat)
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JournalChatApp(
    navController: NavHostController = rememberNavController(),
    modifier: Modifier = Modifier) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
    var isVisible by remember { mutableStateOf(false) }

//    if (scrollBehavior.state.collapsedFraction < 0.3){
//        visibleState.targetState = true
//    }else visibleState.targetState = false
    if (scrollBehavior.state.collapsedFraction < 0.3){
        isVisible = true
    }else isVisible = false

    Scaffold(
        topBar = { TopAppBar(title = stringResource(id = R.string.app_name), scrollBehavior) },
        bottomBar = {  },
        modifier = modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
//            ChatList(Data.chatList, it)

            CreateChat("",{}, contentPadding = it)
            FloatingBottomButton(
                imageVector = Icons.Outlined.Add,
                buttonDescription =  stringResource(R.string.create_chat),
                isVisible = isVisible, Modifier.align(Alignment.BottomEnd))
        }
    }
}


@Composable
fun FloatingBottomButton(imageVector: ImageVector, buttonDescription: String, isVisible: Boolean, modifier: Modifier = Modifier) {
    val density = LocalDensity.current
    AnimatedVisibility(
        visible = isVisible,
        enter = slideInVertically { with(density) {100.dp.roundToPx()}},
        exit = slideOutVertically { with(density) {(120.dp).roundToPx() }} ,
        modifier = modifier.safeDrawingPadding()) {
        FloatingActionButton(
            onClick = { /*TODO*/ },
            contentColor = MaterialTheme.colorScheme.background,
            containerColor = MaterialTheme.colorScheme.secondary,
            modifier = Modifier
                .padding(16.dp)
        ){

            Icon(imageVector = imageVector,
                contentDescription = buttonDescription,
                modifier = Modifier.size(32.dp))}
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBar(title: String, scrollBehavior: TopAppBarScrollBehavior, modifier: Modifier = Modifier) {
    CenterAlignedTopAppBar(
        title = { Text(text = title, style = Typography.displayLarge) },
        navigationIcon = {
            IconButton(onClick = { /*TODO*/ }) {
                Icon(
                    imageVector = Icons.Outlined.List,
                    contentDescription = stringResource(R.string.menu)
                )
            }
        },
        actions = {
            IconButton(onClick = { /*TODO*/ }) {
                Icon(
                    imageVector = Icons.Outlined.Search, contentDescription = stringResource(
                        R.string.search
                    )
                )
            }
        },
        scrollBehavior = scrollBehavior,
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    JournalChatTheme(useDarkTheme = false) {
        JournalChatApp()
    }
}