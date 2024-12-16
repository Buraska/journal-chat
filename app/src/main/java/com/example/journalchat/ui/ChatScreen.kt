package com.example.journalchat.ui

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContent
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Face
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material.icons.outlined.Phone
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.journalchat.AppBarNavigationIconBack
import com.example.journalchat.NavigationDestination
import com.example.journalchat.R
import com.example.journalchat.TopAppBar
import com.example.journalchat.ui.events.ChatEvent
import com.example.journalchat.ui.theme.Typography
import com.example.journalchat.ui.theme.nonPrimaryMessageShape
import com.example.journalchat.ui.theme.primaryMessageShape
import com.example.journalchat.ui.uiModels.MessageUi
import com.example.journalchat.ui.viewModels.ChatViewModel
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object ChatScreenDestination : NavigationDestination {
    override val route = "chat"
    const val chatIdArg = "chatId"
    val routeWithArgs = "$route/{$chatIdArg}"
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun ChatScreen(
    navigateUp: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ChatViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
    val scrollState = rememberLazyListState()
    val chatState by viewModel.chatState.collectAsState()
    var isContextMenuVisible by rememberSaveable { mutableStateOf(false) }

    Log.wtf("wtf", chatState.chat.name)

    Scaffold(
        topBar = {
            TopAppBar(
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
        },
        bottomBar = { },
        modifier = modifier
            .nestedScroll(scrollBehavior.nestedScrollConnection),
//        contentWindowInsets = WindowInsets(
//            0,
//            0,
//            0,
//            0
//        ) // remove additional insets to deal with inputText field.
    ) { innerPadding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(top = innerPadding.calculateTopPadding()),
        ) {
            Messages(
                messages = chatState.messages,
                scrollState,
                modifier = Modifier.weight(1f)
            )
            InputTextField(
                value = chatState.input,
                onValueChange = {input -> viewModel.inputChanged(input)},
                onSendMessage = {
                    viewModel.sendMessage()
                },
                modifier = Modifier.imePadding().navigationBarsPadding()
            )

        }
    }
}

@Composable
fun OptionButton(onClick: () -> Unit, modifier: Modifier = Modifier) {

    IconButton(onClick = onClick)
    {
        Icon(
            imageVector = Icons.Outlined.MoreVert,
            contentDescription = stringResource(R.string.chat_options)
        )
    }


//    Card(elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
//        modifier = modifier.onSizeChanged {
//            itemHeight = with(density) {it.height.toDp()}
//        })
//    {
//        Box(Modifier
//            .fillMaxWidth()
//            .clickable
//            { isContextMenuVisible = true }
//            .padding(16.dp))
//    }


}

@Composable
fun OptionMenu(isContextMenuVisible: Boolean, onDismissRequest: () -> Unit, dpOffset: DpOffset) {
    DropdownMenu(
        expanded = isContextMenuVisible,
        onDismissRequest = onDismissRequest,
        offset = dpOffset
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

            Message(message = message, modifier = Modifier.fillMaxWidth())

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


@Composable
fun Message(message: MessageUi, modifier: Modifier = Modifier) {
    var shape = primaryMessageShape
    var containerColor = MaterialTheme.colorScheme.surfaceVariant
    var horizontalArrangement = Arrangement.End

    if (!message.isPrimary) {
        horizontalArrangement = Arrangement.Start
        shape = nonPrimaryMessageShape
        containerColor = MaterialTheme.colorScheme.secondary
    }

    Row(modifier = modifier, horizontalArrangement = horizontalArrangement) {
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
fun InputTextField(
    value: String,
    onValueChange: (String) -> Unit,
    onSendMessage: () -> Unit,
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
                    imeAction = ImeAction.Done
                ),
                textStyle = TextStyle(color = MaterialTheme.colorScheme.inverseSurface),
                modifier = Modifier
                    .padding()
                    .weight(1f),
            )
            IconButton(onClick = { /*TODO*/ }) {
                Icon(
                    imageVector = Icons.Outlined.Add,
                    contentDescription = stringResource(R.string.stickers_and_emojis)
                )
            }
            IconButton(onClick = onSendMessage) {
                Icon(
                    imageVector = Icons.Outlined.Phone,
                    contentDescription = stringResource(R.string.send_message)
                )
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

//    val messages = listOf<MessageUi>(
//        MessageUi(
//            null,
//            "Starting: Intent { act=android.intent.action.MAIN cat=[android.intent.category.LAUNCHER] cmp=com.example.journalchat/.MainActivity }",
//            date = LocalDateTime.parse(
//                "2012-12-24 12:24:35",
//                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
//            )))
//    val chatState = ChatState("FirstChat", messages)
//    JournalChatTheme(useDarkTheme = true) {
//        ChatScreen(chatState, {})
}

