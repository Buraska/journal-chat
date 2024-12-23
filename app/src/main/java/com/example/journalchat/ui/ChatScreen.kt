package com.example.journalchat.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.ArrowForward
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.Clear
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Face
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material.icons.outlined.Phone
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
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
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.journalchat.AppBarNavigationIconBack
import com.example.journalchat.NavigationDestination
import com.example.journalchat.R
import com.example.journalchat.TopAppBar
import com.example.journalchat.ui.states.ChatMode
import com.example.journalchat.ui.theme.JournalChatTheme
import com.example.journalchat.ui.theme.Shapes
import com.example.journalchat.ui.theme.Typography
import com.example.journalchat.ui.theme.nonPrimaryMessageShape
import com.example.journalchat.ui.theme.primaryMessageShape
import com.example.journalchat.ui.uiModels.MessageUi
import com.example.journalchat.ui.viewModels.ChatViewModel
import org.intellij.lang.annotations.JdkConstants.HorizontalAlignment
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

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
    modifier: Modifier = Modifier,
    viewModel: ChatViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val chatState by viewModel.chatState.collectAsState()

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
    val scrollState = rememberLazyListState()

    var isContextMenuVisible by rememberSaveable { mutableStateOf(false) }

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
                    onClearSelection = { viewModel.clearSelection() })

                ChatMode.Editing -> {
                    EditingTopAppBar(onStopEditing = { viewModel.stopEditing() })
                }

                ChatMode.Chatting -> TopAppBar(
                    title = chatState.chat.name,
                    navIcon = { AppBarNavigationIconBack { navigateUp() } },
                    topAppBarScrollBehavior = scrollBehavior,
                    action = {
                        OptionButton(onClick = { isContextMenuVisible = true })
                        OptionMenu(
                            isContextMenuVisible,
                            { isContextMenuVisible = false },
                            DpOffset.Zero
                        )
                    }
                )

            }
        },
        modifier = modifier
            .nestedScroll(scrollBehavior.nestedScrollConnection),
    ) { innerPadding ->
        if (openAlertDialog.value) {
            DeleteAlertDialog(
                onDelete = { viewModel.deleteMessages() },
                onDismissRequest = { openAlertDialog.value = false },
                chatState.selectedMessages.size > 1)
        }
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(top = innerPadding.calculateTopPadding()),
        ) {
            Messages(
                messages = chatState.messages,
                scrollState,
                modifier = Modifier.weight(1f),
                onMessageClick = { messageUi ->
                    if (chatState.mode == ChatMode.Selecting) {
                        viewModel.selectMessage(messageUi)
                    }
                },
                onMessageLongClick = { messageUi ->
                    if (chatState.mode != ChatMode.Editing)
                        viewModel.selectMessage(messageUi)
                }
            )
            ChatInputField(
                value = chatState.input,
                onValueChange = { input -> viewModel.inputChanged(input) },
                onSendMessage = {
                    viewModel.sendMessage()
                },
                onEditMessage = { viewModel.editMessage() },
                mode = chatState.mode,
                focusRequester = focusRequester,
                modifier = Modifier
                    .imePadding()
                    .navigationBarsPadding()
            )
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditingTopAppBar(
    onStopEditing: () -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler {
        onStopEditing()
    }
    TopAppBar(
        title = stringResource(R.string.editing),
        navIcon = {
            IconButton(onClick = { onStopEditing() }, Modifier) {
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
    modifier: Modifier = Modifier
) {
    BackHandler {
        onClearSelection()
    }
    TopAppBar(
        title = selectionCount.toString(),
        navIcon = {
            IconButton(onClick = { onClearSelection() }, Modifier) {
                Icon(
                    Icons.Outlined.Clear,
                    stringResource(R.string.clear_selection)
                )
            }
        },
        topAppBarScrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(
            rememberTopAppBarState()
        ),
        action = {
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

        },
        modifier = modifier
    )
}

@Composable
fun OptionButton(onClick: () -> Unit, modifier: Modifier = Modifier) {

    IconButton(onClick = onClick, modifier = modifier)
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
    onDismissRequest: () -> Unit,
    dpOffset: DpOffset,
    modifier: Modifier = Modifier
) {
    DropdownMenu(
        expanded = isContextMenuVisible,
        onDismissRequest = onDismissRequest,
        offset = dpOffset,
        modifier = modifier
    )
    {
        DropdownMenuItem(
            text = { Text(stringResource(R.string.delete)) },
            trailingIcon = {
                Icon(
                    imageVector = Icons.Outlined.Delete, contentDescription = stringResource(
                        R.string.deletion_icon
                    )
                )
            },
            onClick =
            {

                onDismissRequest()
            }
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
        containerColor = MaterialTheme.colorScheme.secondary
    }
    Row(
        modifier = modifier
            .drawWithContent {
                drawContent()
                if (message.isSelected) drawRect(selectedColor)
            }
            .combinedClickable(
                onClick = { onClick() },
                onLongClick = { onLongClick() }),
        horizontalArrangement = horizontalArrangement,
    ) {
        Row(
            horizontalArrangement = horizontalArrangement,
            modifier = Modifier.fillMaxWidth(0.95f)
        ) {
            Card(
                modifier = Modifier
                    .padding(
                        dimensionResource(id = R.dimen.padding_medium),
                        0.dp,
                        dimensionResource(id = R.dimen.padding_medium),
                        dimensionResource(id = R.dimen.card_elevation)
                    )
                    .wrapContentWidth(),
                colors = CardDefaults.cardColors(containerColor = containerColor),
                shape = shape
            ) {
                Column(modifier = Modifier.padding(4.dp)) {
                    Text(
                        text = message.content,
                        style = Typography.bodyMedium,
                        modifier = Modifier
                            .wrapContentWidth(Alignment.End)
                    )

                    Text(
                        text = message.date.getTime(),
                        style = Typography.bodySmall,
                        modifier = Modifier
                            .align(Alignment.End)
                            .padding(2.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun DeleteAlertDialog(
    onDelete: () -> Unit,
    onDismissRequest: () -> Unit,
    plural: Boolean,
    modifier: Modifier = Modifier
) {
    val mes = if (plural) stringResource(R.string.messages) else stringResource(R.string.message)
    Dialog(onDismissRequest) {
        Card(modifier = modifier, shape = ShapeDefaults.Medium) {
            Column(modifier = Modifier.padding(16.dp, 16.dp, 16.dp, 8.dp)) {
                Text(stringResource(R.string.delete_dialog_title, mes), style = Typography.titleMedium, modifier = Modifier)
                Text(stringResource(R.string.delete_dialog_body, mes), style = Typography.bodyMedium)
                Row(horizontalArrangement = Arrangement.End, modifier = Modifier.fillMaxWidth()) {
                    TextButton(
                        onClick = { onDismissRequest() }) {
                        Text("Cancel")
                    }
                    TextButton(
                        onClick = { onDelete(); onDismissRequest() },
                        colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.error)
                    ) {
                        Text("Delete")
                    }
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
    modifier: Modifier = Modifier
) {

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

                else -> IconButton(onClick = onSendMessage) {
                    Icon(
                        imageVector = Icons.Outlined.ArrowForward,
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

    DeleteAlertDialog({}, {}, true)

}

