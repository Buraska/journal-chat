package com.example.journalchat

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.journalchat.ui.theme.JournalChatTheme
import com.example.journalchat.ui.theme.Typography



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
fun ChatTopAppBar(
    title: String,
    navIcon: @Composable () -> Unit,
    topAppBarScrollBehavior: TopAppBarScrollBehavior,
    action: @Composable () -> Unit = {},
    modifier: Modifier = Modifier
) {
    CenterAlignedTopAppBar(
        title = { Text(text = title, style = Typography.displayLarge) },
        navigationIcon = navIcon,
        actions ={action()
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