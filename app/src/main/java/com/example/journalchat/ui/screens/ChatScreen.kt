package com.example.journalchat.ui.screens

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.Clear
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Face
import androidx.compose.material.icons.outlined.KeyboardArrowRight
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusProperties
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.Placeable
import androidx.compose.ui.layout.SubcomposeLayout
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.LineBreak
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.PopupProperties
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.journalchat.AppBarNavigationIconBack
import com.example.journalchat.NavigationDestination
import com.example.journalchat.R
import com.example.journalchat.ChatTopAppBar
import com.example.journalchat.ui.AppViewModelProvider
import com.example.journalchat.ui.states.ChatMode
import com.example.journalchat.ui.theme.Typography
import com.example.journalchat.ui.theme.nonPrimaryMessageShape
import com.example.journalchat.ui.theme.primaryMessageShape
import com.example.journalchat.ui.uiModels.MessageUi
import com.example.journalchat.ui.uiModels.TagUi
import com.example.journalchat.ui.viewModels.ChatViewModel
import org.intellij.lang.annotations.JdkConstants.HorizontalAlignment
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.random.Random

object ChatScreenDestination : NavigationDestination {
    override val route = "chat"
    const val chatIdArg = "chatId"
    val routeWithArgs = "$route/{$chatIdArg}"
}

@OptIn(
    ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class,
    ExperimentalComposeUiApi::class
)
@Composable
fun ChatScreen(
    navigateUp: () -> Unit,
    onEditChatClicked: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ChatViewModel = viewModel(factory = AppViewModelProvider.Factory)
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
                ChatMode.Selecting -> SelectionModeTopAppBar(
                    viewModel.getSelectedCount(),
                    onDeleteMessage = { openAlertDialog.value = true },
                    onEditMessage = {
                        viewModel.startEditing()
                        focusRequester.requestFocus()
                        keyboardController?.show()
                    },
                    onClearSelection = { viewModel.clearSelection() },
                    onReply = {
                        viewModel.startReplying()
                        focusRequester.requestFocus()
                        keyboardController?.show()
                    },
                    onForward = {})

                ChatMode.Editing -> {
                    ActionTopAppBar(
                        stringResource(id = R.string.editing),
                        onStopAction = { viewModel.stopAction() })
                }

                ChatMode.Chatting -> {
                    ChatTopAppBar(
                        title = chatState.chat.name,
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
                    ActionTopAppBar(
                        stringResource(R.string.replying),
                        onStopAction = { viewModel.stopAction() })
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
                modifier = Modifier.weight(1f),
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
                }
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

@Composable
fun EmojiSearchBar(
    userTags: List<TagUi>,
    filteringTag: TagUi?,
    onEmojiClick: (TagUi) -> Unit,
    inputValue: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit,
    modifier: Modifier = Modifier
) {
    TextField(
        value = inputValue,
        placeholder = { Text("Search for..") },
        onValueChange = onValueChange,
        maxLines = 1,
        modifier = Modifier.fillMaxWidth()
    )
    LazyRow {
        items(userTags) { tag ->
            val color =
                if (tag == filteringTag) MaterialTheme.colorScheme.surfaceVariant else MaterialTheme.colorScheme.surface
            Card(
                colors = CardDefaults.cardColors(containerColor = color),
                modifier = modifier
                    .padding(4.dp)
                    .clickable { onEmojiClick(tag) }) {
                Text(
                    text = tag.emojiCode,
                    textAlign = TextAlign.Center,
                    fontSize = 20.sp,
                    modifier = modifier.padding(4.dp)
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmojiBottomSheet(
    tags: List<TagUi>,
    sheetState: SheetState,
    onDismissRequest: () -> Unit,
    onTagClick: (TagUi) -> Unit,
    modifier: Modifier = Modifier
) {
    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        sheetState = sheetState,
        modifier = modifier
    ) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(6),
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
        ) {
            items(tags) { tag ->
                Emoji(
                    text = tag.emojiCode,
                    onTagClick = { onTagClick(tag) },
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }
        }
    }
}

@Composable
fun EmojiDialog(
    selectedTags: List<TagUi>,
    onTagClick: (TagUi) -> Unit,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    onExpandClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    BackHandler {
        onDismissRequest()
    }
    Box(modifier = Modifier
        .fillMaxWidth(0.7f)
        .background(color = Color.Black.copy(alpha = 0.22f))
        .clickable(interactionSource, indication = null) { onDismissRequest() }
    ) {
        Card(modifier = modifier.align(Alignment.Center)) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(5),
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(0.7f)
            ) {
                items(selectedTags) { tag ->
                    Emoji(
                        text = tag.emojiCode,
                        onTagClick = { onTagClick(tag) },
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                }
                item {
                    IconButton(onClick = onExpandClick) {
                        Icon(
                            imageVector = Icons.Outlined.Add,
                            contentDescription = stringResource(R.string.expand_emoji_list)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun Emoji(
    text: String,
    onTagClick: () -> Unit,
    modifier: Modifier = Modifier,
    fontSize: TextUnit = 36.sp
) {
    Text(
        text = text,
        textAlign = TextAlign.Center,
        fontSize = fontSize,
        modifier = modifier
            .clickable { onTagClick() }
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActionTopAppBar(
    title: String,
    onStopAction: () -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler {
        onStopAction()
    }
    ChatTopAppBar(
        title = title,
        navIcon = {
            IconButton(onClick = { onStopAction() }, Modifier) {
                Icon(
                    Icons.Outlined.Clear,
                    "Clear selection"
                )
            }
        },
        topAppBarScrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(
            rememberTopAppBarState()
        ),
        action = {},
        modifier = modifier
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectionModeTopAppBar(
    selectionCount: Int,
    onDeleteMessage: () -> Unit,
    onEditMessage: () -> Unit,
    onClearSelection: () -> Unit,
    onReply: () -> Unit,
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
                IconButton(onClick = { onReply() }) {
                    Icon(
                        painterResource(id = R.drawable.reply),
                        stringResource(R.string.reply)
                    )
                }
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

@Composable
fun OptionButton(onClick: () -> Unit, modifier: Modifier = Modifier) {

    IconButton(onClick = onClick, modifier = modifier.focusProperties { canFocus = false })
    {
        Icon(
            imageVector = Icons.Outlined.MoreVert,
            contentDescription = stringResource(R.string.chat_options)
        )
    }
}

@Composable
fun OptionMenu(
    isContextMenuVisible: Boolean,
    onEditChatClicked: () -> Unit,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
) {
    DropdownMenu(
        expanded = isContextMenuVisible,
        onDismissRequest = onDismissRequest,
        offset = DpOffset.Zero,
        properties = PopupProperties(focusable = false)
    )
    {
        DropdownMenuItem(
            text = { Text(stringResource(R.string.edit)) },
            trailingIcon = {
                Icon(
                    imageVector = Icons.Outlined.Edit, contentDescription = stringResource(
                        R.string.deletion_icon
                    )
                )
            },
            onClick =
            {
                onEditChatClicked()
                onDismissRequest()
            },
            modifier = modifier.focusProperties { canFocus = false }
        )
    }
}

@Composable
fun Messages(
    messages: List<MessageUi>,
    lazyListState: LazyListState,
    onMessageClick: (MessageUi) -> Unit,
    onMessageLongClick: (MessageUi) -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp)
) {
    val fMessage = messages.getOrNull(0);
    var isLastItemPrimary: Boolean?
    var lastTimeStamp: LocalDateTime?


    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(2.dp),
        reverseLayout = true,
        state = lazyListState,
        contentPadding = contentPadding,
        modifier = modifier
    ) {
        isLastItemPrimary = null
        lastTimeStamp = null

        itemsIndexed(messages) { index, message ->

            Message(
                message = message,
                modifier = Modifier.fillMaxWidth(),
                onClick = { onMessageClick(message) },
                onLongClick = { onMessageLongClick(message) }
            )

            isLastItemPrimary = message.isPrimary
            lastTimeStamp = message.date
        }
    }
}


@Composable
fun DayHeader(dayName: String, modifier: Modifier = Modifier) {
    Row(
        modifier = Modifier
            .padding(vertical = 8.dp, horizontal = 16.dp)
            .height(16.dp)
    ) {
        Divider(
            modifier = Modifier
                .weight(1f)
                .align(Alignment.CenterVertically),
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f)
        )
        Text(
            text = dayName,
            modifier = Modifier.padding(horizontal = 16.dp),
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Divider(
            modifier = Modifier
                .weight(1f)
                .align(Alignment.CenterVertically),
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f)
        )
    }
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Message(
    message: MessageUi,
    onClick: () -> Unit,
    onLongClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var shape = primaryMessageShape
    var containerColor = MaterialTheme.colorScheme.surfaceVariant
    var selectedColor = MaterialTheme.colorScheme.onBackground.copy(0.20f)
    var horizontalArrangement = Arrangement.End

    if (!message.isPrimary) {
        horizontalArrangement = Arrangement.Start
        shape = nonPrimaryMessageShape
        containerColor = MaterialTheme.colorScheme.tertiary
    }

    Row(
        horizontalArrangement = horizontalArrangement,
        modifier = modifier
            .drawWithContent {
                drawContent()
                if (message.isSelected) drawRect(selectedColor)
            }
            .combinedClickable(
                onClick = { onClick() },
                onLongClick = { onLongClick() })
    ) {
        Card(
            modifier = Modifier
                .padding(
                    dimensionResource(id = R.dimen.padding_medium),
                    0.dp,
                    dimensionResource(id = R.dimen.padding_medium),
                    dimensionResource(id = R.dimen.card_elevation)
                ),
            colors = CardDefaults.cardColors(containerColor = containerColor),
            shape = shape
        ) {
            SubcomposeLayout(modifier = Modifier.padding(8.dp)) { constraints ->
                val mainContent = subcompose("mainContent") {
                    Text(
                        text = message.content,
                        style = Typography.bodyMedium,
                    )
                }.first().measure(constraints)
                val date = subcompose("date") {
                    Text(
                        text = message.date.getTime(),
                        style = Typography.bodySmall,
                        modifier = Modifier
                            .align(Alignment.End)
                            .padding(6.dp, 6.dp, 0.dp, 0.dp)
                    )
                }
                    .first().measure(constraints)

                var reply: Placeable? = null
                if (message.reference != null) {
                    reply = subcompose("reply") {
                        Card(
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background),
                            modifier = Modifier
                        ) {
                            Text(
                                text = message.reference.content,
                                style = Typography.bodyMedium,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier.padding(8.dp)
                            )
                        }
                    }.first().measure(constraints)
                }


                var tag: Placeable? = null
                if (message.tag != null) {
                    tag = subcompose("tag") {
                        Card(
                            modifier = Modifier.padding(top = 4.dp, end = 4.dp)
                        ) {
                            Text(
                                modifier = Modifier.padding(4.dp),
                                text = message.tag.emojiCode,
                                fontSize = 20.sp
                            )
                        }
                    }.first().measure(constraints)
                }

                val replyWidth = reply?.width ?: 0
                val tagWidth = tag?.width ?: 0
                var cardWidth = maxOf(replyWidth, mainContent.width, date.width + tagWidth)

                val replyHeight = reply?.height ?: 0
                var cardHeight = mainContent.height + replyHeight
                cardHeight += tag?.height ?: date.height

                var isShort = false
                if (tag == null && cardWidth == date.width) {
                    cardHeight -= date.height
                    cardWidth += mainContent.width
                    isShort = true
                }

                var replyArea: Placeable? = null
                if (message.reference != null) {
                    replyArea = subcompose("replyArea") {
                        Card(
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background),
                            modifier = Modifier.size(DpSize(cardWidth.toDp(), replyHeight.toDp()))
                        ) {
                        }
                    }.first().measure(constraints)
                }

                var references: Placeable? = null
                if (message.references != null) {
                    val text: String
                    val refCount = message.references.size
                    text = if (refCount > 1) "$refCount comments" else "$refCount comment"

                    val referenceText = subcompose("referencesText") {
                        Row(horizontalArrangement = Arrangement.SpaceBetween) {
                            Text(
                                text,
                                style = Typography.bodySmall,
                                modifier = Modifier.align(Alignment.CenterVertically)
                            )
                            Icon(Icons.Outlined.KeyboardArrowRight, "asd")
                        }
                    }.first().measure(constraints)

                    references = subcompose("references") {
                        Column(
                            modifier = Modifier.width((maxOf(cardWidth.toDp(), referenceText.width.toDp())))
                        ) {
                            Divider(modifier = Modifier.padding(4.dp), color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.45f))
                            Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                                Text(
                                    text,
                                    style = Typography.bodySmall,
                                    modifier = Modifier.align(Alignment.CenterVertically)
                                )
                                Icon(Icons.Outlined.KeyboardArrowRight, "asd")
                            }
                        }
                    }.first().measure(constraints)
                }

                val referencesHeight = references?.height ?: 0
                val referencesWidth = references?.width ?: 0
                cardHeight += referencesHeight
                cardWidth = maxOf(cardWidth, referencesWidth)

                layout(cardWidth, cardHeight) {
                    var offset = 0
                    if (reply != null && replyArea != null) {
                        replyArea.place(0, 0)
                        reply.place(0, 0)
                        offset = reply.height
                    }
                    mainContent.place(0, offset)
                    offset += mainContent.height
                    if (tag != null) {
                        tag.place(0, offset)
                        offset += tag.height - date.height

                    }
                    if (isShort) offset -= date.height
                    date.place(cardWidth - date.width, offset)
                    offset += date.height

                    references?.place(0, offset)
                }
            }

        }
    }
}

@Composable
fun ChatInputField(
    value: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit,
    onSendMessage: () -> Unit,
    onEditMessage: () -> Unit,
    mode: ChatMode,
    focusRequester: FocusRequester,
    modifier: Modifier = Modifier,
    onReplyMessage: () -> Unit
) {
    Log.d("Custom", "ChatInputField is recomposed")
    Surface(
        contentColor = MaterialTheme.colorScheme.inverseSurface,
        color = (MaterialTheme.colorScheme.surfaceVariant),
        modifier = modifier
    ) {
        Row(
            modifier = Modifier
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End,
        ) {
            IconButton(onClick = { /*TODO*/ }) {
                Icon(
                    imageVector = Icons.Outlined.Face,
                    contentDescription = stringResource(R.string.stickers_and_emojis)
                )
            }
            BasicTextField(
                value = value,
                onValueChange = onValueChange,
                maxLines = 6,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Default
                ),
                textStyle = TextStyle(color = MaterialTheme.colorScheme.inverseSurface),
                modifier = Modifier
                    .padding()
                    .weight(1f)
                    .focusRequester(focusRequester),
            )
            IconButton(onClick = { /*TODO*/ }) {
                Icon(
                    imageVector = Icons.Outlined.Add,
                    contentDescription = stringResource(R.string.stickers_and_emojis)
                )
            }
            when (mode) {
                ChatMode.Editing ->
                    IconButton(onClick = onEditMessage) {
                        Icon(
                            imageVector = Icons.Outlined.Check,
                            contentDescription = stringResource(R.string.confirm_editing)
                        )
                    }

                ChatMode.Replying ->
                    IconButton(onClick = onReplyMessage) {
                        Icon(
                            painter = painterResource(id = R.drawable.send),
                            contentDescription = stringResource(R.string.reply)
                        )
                    }

                else -> IconButton(onClick = onSendMessage) {
                    Icon(
                        painter = painterResource(id = R.drawable.send),
                        contentDescription = stringResource(R.string.send_message)
                    )
                }
            }

        }
    }
}

fun LocalDateTime.getTime(): String {
    return this.format(DateTimeFormatter.ofPattern("HH:mm"))
}

fun LocalDateTime.getDate(): String {
    return this.format(DateTimeFormatter.ofPattern("MM/dd"))
}


@Preview()
@Composable
fun ChatPreview() {

    Scaffold(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .padding(it)
        ) {
            Message(
                message = MessageUi(0, content = "da", tag = TagUi(0, "\uD83D\uDE00"), chatId = 0),
                onClick = { /*TODO*/ },
                onLongClick = { /*TODO*/ }
            )
            Message(
                message = MessageUi(0, content = "da", chatId = 0),
                onClick = { /*TODO*/ },
                onLongClick = { /*TODO*/ })
            Message(
                message = MessageUi(
                    0,
                    content = "da. Об этом много говорят на кухнях",
                    chatId = 0,
                    references = listOf(MessageUi(0, content = "asda", chatId = 0))
                ),
                onClick = { /*TODO*/ },
                onLongClick = { /*TODO*/ })
            Message(
                message = MessageUi(
                    0,
                    content = "da. Об этом много говорят на кухнях",
                    tag = TagUi(0, "\uD83D\uDE00"),
                    chatId = 0
                ),
                onClick = { /*TODO*/ },
                onLongClick = { /*TODO*/ })
        }
        Message(
            message = MessageUi(0, content = "da", tag = TagUi(0, "\uD83D\uDE00"), chatId = 0),
            onClick = { /*TODO*/ },
            onLongClick = { /*TODO*/ })
    }
}

