package com.example.journalchat.ui

import android.graphics.drawable.Drawable
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.journalchat.AppBarNavigationDrawer
import com.example.journalchat.FloatingBottomButton
import com.example.journalchat.R
import com.example.journalchat.TopAppBar
import com.example.journalchat.ui.theme.JournalChatTheme
import com.example.journalchat.ui.theme.Shapes
import com.example.journalchat.ui.theme.Typography
import com.example.journalchat.ui.uiModels.ChatUi

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatListScreen(
    chatList: List<ChatUi>,
    onButtonClick: () -> Unit,
    exposeDrawer:  () -> Unit,
    onItemClicked: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
    var isVisible by remember { mutableStateOf(false) }
    isVisible = scrollBehavior.state.collapsedFraction < 0.3

    Scaffold(
        topBar = {TopAppBar(
            title = stringResource(id = R.string.app_name),
            navIcon = {AppBarNavigationDrawer(exposeDrawer)},
            topAppBarScrollBehavior = scrollBehavior,
            action = {SearchButton()}
        )},
        bottomBar = { },
        floatingActionButton = {            FloatingBottomButton(
            imageVector = Icons.Outlined.Add,
            buttonDescription = stringResource(R.string.create_chat),
            isVisible = isVisible,
            onClick = onButtonClick,
        )},
        modifier = modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection)
    ){
        Box(modifier = modifier
            .fillMaxSize()
            .padding(it)) {
            ChatList(chatList, onItemClicked)

        }
    }
}


@Composable
fun ChatList(
    chatList: List<ChatUi>,
    onItemClicked: (String) -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp)
) {


    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(2.dp),
        contentPadding = contentPadding,
        modifier = modifier
    ) {
        itemsIndexed(chatList) { index, chat ->
            ChatItem(chatItem = chat, onItemClicked = onItemClicked)
        }

    }
}

@Composable
fun SearchButton(){
    IconButton(onClick = { /*TODO*/ }) {
        Icon(
            imageVector = Icons.Outlined.Search, contentDescription = stringResource(
                R.string.search
            )
        )
    }
}

@Composable
fun ChatItem(
    chatItem: ChatUi,
    icon: Drawable? = null,
    onItemClicked: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clip(Shapes.medium).clickable
            {
                Log.i("ChatListScreen.ChatItem", "Click on ChatItem: ${chatItem}")
                onItemClicked(chatItem.name)
            },
        elevation = CardDefaults.cardElevation(defaultElevation = dimensionResource(id = R.dimen.card_elevation))
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
                .padding(4.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_launcher_foreground),
                contentDescription = stringResource(R.string.chat_icon),
                modifier = Modifier
                    .weight(0.3f)
                    .scale(0.8f)
                    .clip(Shapes.small)
                    .background(Color.Gray)
            )
            Spacer(Modifier.width(dimensionResource(id = R.dimen.spacer_medium)))
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(), verticalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    chatItem.name,
                    style = Typography.bodyLarge,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = if (chatItem.messages.size > 0) chatItem.messages[0].content else "",
                    style = Typography.bodyMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}


@Composable
@Preview(showBackground = true)
fun ChatListPreview() {
    JournalChatTheme(useDarkTheme = true) {
        ChatList(
            chatList = listOf(),
            onItemClicked = {},
            modifier = Modifier.background(MaterialTheme.colorScheme.background)
        )
    }
}
