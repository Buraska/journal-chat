package com.example.journalchat.ui.screens

import android.graphics.drawable.Drawable
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
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
import androidx.compose.material.icons.outlined.Clear
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.journalchat.AppBarNavigationDrawer
import com.example.journalchat.FloatingBottomButton
import com.example.journalchat.R
import com.example.journalchat.ChatTopAppBar
import com.example.journalchat.NavigationDestination
import com.example.journalchat.ui.AppViewModelProvider
import com.example.journalchat.ui.states.ChatListMode
import com.example.journalchat.ui.theme.JournalChatTheme
import com.example.journalchat.ui.theme.Shapes
import com.example.journalchat.ui.theme.Typography
import com.example.journalchat.ui.uiModels.ChatUi
import com.example.journalchat.ui.viewModels.ChatListViewModel

object ChatListScreenDestination : NavigationDestination {
    override val route = "chatList"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatListScreen(
    onCreateButton: () -> Unit,
    exposeDrawer: () -> Unit,
    onItemClicked: (Long) -> Unit,
    onEditClicked: (Long) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ChatListViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val chatListState by viewModel.chatListState.collectAsState()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
    var isVisible by remember { mutableStateOf(false) }
    isVisible = scrollBehavior.state.collapsedFraction < 0.3
    val openAlertDialog = remember { mutableStateOf(false) }

    Scaffold(
        topBar =
        {
            when (chatListState.chatListMode) {
                ChatListMode.Default -> ChatTopAppBar(
                    title = stringResource(id = R.string.app_name),
                    navIcon = { AppBarNavigationDrawer(exposeDrawer) },
                    topAppBarScrollBehavior = scrollBehavior,
                    action = { SearchButton() }
                )

                ChatListMode.Selecting -> SelectionModeChatListTopAppBar(
                    selectionCount = viewModel.getSelectedCount(),
                    onDeleteChat = {openAlertDialog.value = !openAlertDialog.value},
                    onEditChat = { onEditClicked(chatListState.selectedChats[0].id) },
                    onClearSelection = viewModel::clearSelection
                )
            }
        },
        bottomBar = { },
        floatingActionButton = {
            FloatingBottomButton(
                imageVector = Icons.Outlined.Add,
                buttonDescription = stringResource(R.string.create_chat),
                isVisible = isVisible,
                onClick = onCreateButton,
            )
        },
        modifier = modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection)
    ) { innerPadding ->
        if (openAlertDialog.value) {
            val notificationBody: String
            val notificationTitle: String
            if (chatListState.selectedChats.count() > 1) {
                notificationBody = stringResource(id = R.string.delete_chat_dialog_body_plural)
                notificationTitle = stringResource(id = R.string.delete_chat_dialog_title_plural)
            }else{
                notificationBody = stringResource(id = R.string.delete_chat_dialog_body_singular)
                notificationTitle = stringResource(id = R.string.delete_chat_dialog_title_singular)
            }

            DeleteAlertDialog(
                onDelete = { viewModel.deleteChats() },
                onDismissRequest = { openAlertDialog.value = false },
                notificationTitle = notificationTitle,
                notificationBody = notificationBody,
            )
        }
        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
        )
        {
            ChatList(chatListState.chats,
                onItemClicked = {
                    if (chatListState.chatListMode == ChatListMode.Default){onItemClicked(it)}
                    else viewModel.selectChat(it)
                                },
                onLongClick = viewModel::selectChat)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectionModeChatListTopAppBar(
    selectionCount: Int,
    onDeleteChat: () -> Unit,
    onEditChat: () -> Unit,
    onClearSelection: () -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler {
        onClearSelection()
    }
    TopAppBar(
        title = { Text(text = selectionCount.toString(), style = Typography.displayLarge) },
        navigationIcon = {
            IconButton(onClick = { onClearSelection() }, Modifier) {
                Icon(
                    Icons.Outlined.Clear,
                    stringResource(R.string.clear_selection)
                )
            }
        },
        scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(
            rememberTopAppBarState()
        ),
        actions = {
            if (selectionCount == 1) {
                IconButton(onClick = { onEditChat() }) {
                    Icon(
                        Icons.Outlined.Edit,
                        stringResource(R.string.edit_message)
                    )

                }
            }
            IconButton(onClick = { onDeleteChat() }) {
                Icon(
                    Icons.Outlined.Delete,
                    stringResource(R.string.delete_message)
                )
            }
        },
        modifier = modifier
    )
}

@Composable
fun ChatList(
    chatList: List<ChatUi>,
    onItemClicked: (Long) -> Unit,
    onLongClick: (ChatUi) -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp)
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(2.dp),
        contentPadding = contentPadding,
        modifier = modifier
    ) {
        itemsIndexed(chatList) { index, chat ->
            ChatItem(
                chatItem = chat,
                onItemClicked = onItemClicked,
                onLongClick = { onLongClick(chat) })
        }

    }
}

@Composable
fun SearchButton() {
    IconButton(onClick = { /*TODO*/ }) {
        Icon(
            imageVector = Icons.Outlined.Search, contentDescription = stringResource(
                R.string.search
            )
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ChatItem(
    chatItem: ChatUi,
    icon: Drawable? = null,
    onItemClicked: (Long) -> Unit,
    onLongClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedColor = MaterialTheme.colorScheme.onBackground.copy(0.20f)

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clip(Shapes.medium)
            .drawWithContent {
                drawContent()
                if (chatItem.isSelected) drawRect(selectedColor)
            }
            .combinedClickable(
                onClick =
                {
                    Log.i("ChatListScreen.ChatItem", "Click on ChatItem: ${chatItem}")
                    onItemClicked(chatItem.id)
                },
                onLongClick = { onLongClick() }),
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
                    text = if (chatItem.messages.isNotEmpty()) chatItem.messages[0].content else "",
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
            onLongClick = {},
            modifier = Modifier.background(MaterialTheme.colorScheme.background)
        )
    }
}
