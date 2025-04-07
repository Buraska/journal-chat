package com.example.journalchat.ui.screens

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Clear
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.journalchat.AppBarNavigationIconBack
import com.example.journalchat.NavigationDestination
import com.example.journalchat.R
import com.example.journalchat.ChatTopAppBar
import com.example.journalchat.ui.AppViewModelProvider
import com.example.journalchat.ui.states.ChatMode
import com.example.journalchat.ui.theme.Typography
import com.example.journalchat.ui.viewModels.ReplyingViewModel

object ReplyingScreenDestination : NavigationDestination {
    override val route = "replying"
    const val messageIdArg = "messageId"
    val routeWithArgs = "$route/{$messageIdArg}"
}

@OptIn(
    ExperimentalMaterial3Api::class,
    ExperimentalComposeUiApi::class
)
@Composable
fun ReplyingScreen(
    navigateUp: () -> Unit,
    onEditChatClicked: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ReplyingViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val chatState by viewModel.chatState.collectAsState()

    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    val scrollState = rememberLazyListState()
    val sheetState = rememberModalBottomSheetState()

    var isContextMenuVisible by rememberSaveable { mutableStateOf(false) }
    var isBottomSheetVisible by rememberSaveable { mutableStateOf(false) }
    var isEmojiPopUpVisible by rememberSaveable { mutableStateOf(false) }

    val openAlertDialog = remember { mutableStateOf(false) }

    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current
    Scaffold(
        topBar = {
            when (chatState.mode) {
                ChatMode.Selecting -> ReplyingSelectionModeTopAppBar(
                    viewModel.getSelectedCount(),
                    onDeleteMessage = { openAlertDialog.value = true },
                    onEditMessage = {
                        viewModel.startEditing()
                        focusRequester.requestFocus()
                        keyboardController?.show()
                    },
                    onClearSelection = { viewModel.clearSelection() },
                    onForward = {})

                ChatMode.Editing -> {
                    ActionTopAppBar(
                        stringResource(id = R.string.editing),
                        onStopAction = { viewModel.stopAction() })
                }

                ChatMode.Chatting -> {
                    ChatTopAppBar(
                        title = chatState.replyingMessage.content,
                        navIcon = { AppBarNavigationIconBack { navigateUp() } },
                        topAppBarScrollBehavior = scrollBehavior,
                        action = {
                            IconButton(onClick = { viewModel.switchMode(ChatMode.Searching) }) {
                                Icon(
                                    imageVector = Icons.Outlined.Search,
                                    contentDescription = stringResource(R.string.search)
                                )
                            }
                            OptionButton(onClick = {
                                isContextMenuVisible = true
                            })
                            OptionMenu(
                                isContextMenuVisible = isContextMenuVisible,
                                onEditChatClicked = { onEditChatClicked() },
                                onDismissRequest = { isContextMenuVisible = false },
                            )
                        }
                    )

                }

                ChatMode.Replying -> {
                    // TODO Code cannot reach here.
                }

                ChatMode.Searching -> {
                    ActionTopAppBar(
                        stringResource(R.string.searching),
                        onStopAction = { viewModel.stopAction() }
                    )
                }
            }
        },
        modifier = modifier
            .nestedScroll(scrollBehavior.nestedScrollConnection),
    ) { innerPadding ->
        if (openAlertDialog.value) {
            val notificationBody: String
            val notificationTitle: String
            if (chatState.selectedMessages.count() > 1) {
                notificationBody = stringResource(id = R.string.delete_message_dialog_body_plural)
                notificationTitle = stringResource(id = R.string.delete_message_dialog_title_plural)
            } else {
                notificationBody = stringResource(id = R.string.delete_message_dialog_body_singular)
                notificationTitle =
                    stringResource(id = R.string.delete_message_dialog_title_singular)
            }

            DeleteAlertDialog(
                onDelete = { viewModel.deleteMessages() },
                onDismissRequest = { openAlertDialog.value = false },
                notificationTitle = notificationTitle,
                notificationBody = notificationBody,
            )
        }

        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(top = innerPadding.calculateTopPadding()),
        ) {
            if (chatState.mode == ChatMode.Searching) {
                EmojiSearchBar(
                    userTags = viewModel.userTags,
                    filteringTag = chatState.filteringTag,
                    onEmojiClick = viewModel::selectTag,
                    inputValue = chatState.searchInput,
                    onValueChange = viewModel::searchInputChanged
                )
            }

            Messages(
                messages = chatState.messages,
                scrollState,
                onMessageClick = { messageUi ->
                    if (chatState.mode == ChatMode.Selecting) {
                        viewModel.selectMessage(messageUi)
                    } else {
                        if (messageUi.tag != null) viewModel.applyTag(messageUi, null)
                        else {
                            isEmojiPopUpVisible = true
                            viewModel.selectMessage(messageUi)
                        }
                    }
                },
                onMessageLongClick = { messageUi ->
                    if (chatState.mode != ChatMode.Editing)
                        viewModel.selectMessage(messageUi)
                },
                modifier = Modifier.weight(1f),
                onMessageCommentsClicked = { }
            )

            if (chatState.mode != ChatMode.Searching) {
                ChatInputField(
                    value = chatState.input,
                    onValueChange = { input -> viewModel.inputChanged(input) },
                    onSendMessage = {
                        viewModel.sendMessage()
                    },
                    onEditMessage = { viewModel.editMessageText() },
                    onReplyMessage = { viewModel.replyMessage() },
                    mode = chatState.mode,
                    focusRequester = focusRequester,
                    modifier = Modifier
                        .imePadding()
                        .navigationBarsPadding()
                )
            }
        }

    }
    if (isBottomSheetVisible) {
        EmojiBottomSheet(tags = chatState.tags, sheetState,
            onDismissRequest = {
                isBottomSheetVisible = false
                viewModel.clearSelection()
            },
            onTagClick = { tagUi ->
                viewModel.applyTag(tagUi)
                viewModel.clearSelection()
                isBottomSheetVisible = false
            }
        )
    }
    if (isEmojiPopUpVisible) EmojiDialog(
        selectedTags = viewModel.userTags,
        onTagClick = { tag ->
            viewModel.applyTag(tag)
            isEmojiPopUpVisible = false
        },
        onDismissRequest = {
            viewModel.clearSelection()
            isEmojiPopUpVisible = false
        },
        onExpandClick = {
            isBottomSheetVisible = true
            isEmojiPopUpVisible = false
        })
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReplyingSelectionModeTopAppBar(
    selectionCount: Int,
    onDeleteMessage: () -> Unit,
    onEditMessage: () -> Unit,
    onClearSelection: () -> Unit,
    onForward: () -> Unit,
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
                IconButton(onClick = { onEditMessage() }) {
                    Icon(
                        Icons.Outlined.Edit,
                        stringResource(R.string.edit_message)
                    )

                }
            }
            IconButton(onClick = { onDeleteMessage() }) {
                Icon(
                    Icons.Outlined.Delete,
                    stringResource(R.string.delete_message)
                )
            }
            IconButton(onClick = { onForward() }) {
                Icon(
                    painterResource(id = R.drawable.forward),
                    stringResource(R.string.forward)
                )
            }

        },
        modifier = modifier
    )
}

