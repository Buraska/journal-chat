package com.example.journalchat.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.consumedWindowInsets
import androidx.compose.foundation.layout.exclude
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Face
import androidx.compose.material.icons.outlined.Phone
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScaffoldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.journalchat.AppBarNavigationIconBack
import com.example.journalchat.R
import com.example.journalchat.TopAppBar
import com.example.journalchat.models.Message
import com.example.journalchat.ui.events.ChatEvent
import com.example.journalchat.ui.states.ChatState
import com.example.journalchat.ui.theme.JournalChatTheme
import com.example.journalchat.ui.theme.Typography
import com.example.journalchat.ui.theme.nonPrimaryMessageShape
import com.example.journalchat.ui.theme.primaryMessageShape
import com.example.journalchat.ui.viewModels.ChatViewModel
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun ChatScreen(
    chatState: ChatState,
    navigateUp: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ChatViewModel = viewModel()
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
    val scrollState = rememberLazyListState()
    Scaffold(
        topBar = {
            TopAppBar(
                title = chatState.name,
                navIcon = { AppBarNavigationIconBack { navigateUp() } },
                topAppBarScrollBehavior = scrollBehavior,
                action = {}
            )
        },
        bottomBar = { },
        modifier = modifier
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        contentWindowInsets = WindowInsets(0, 0, 0, 0)
    ) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .consumeWindowInsets(it)
                .systemBarsPadding(),
        ) {
            Messages(messages = chatState.messages, scrollState, modifier = Modifier.weight(1f))
            InputTextField(
                value = viewModel.chatState.input,
                onValueChange = { input -> viewModel.onEvent(ChatEvent.InputChanged(input)) },
                modifier = Modifier

            )
        }
    }
}


@Composable
fun Messages(
    messages: List<Message>,
    lazyListState: LazyListState,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp)
) {
    val fMessage = messages.getOrNull(0);
    var isLastItemPrimary: Boolean
    var lastTimeStamp: LocalDateTime



    if (fMessage != null) {
        isLastItemPrimary = fMessage.isPrimary
        lastTimeStamp = fMessage.dateTime
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(2.dp),
            reverseLayout = true,
            state = lazyListState,
            contentPadding = contentPadding,
            modifier = modifier
        ) {
            itemsIndexed(messages) { index, message ->
                if (isLastItemPrimary != message.isPrimary) {
                    Spacer(modifier = Modifier.height(8.dp))
                }
                if (!lastTimeStamp.toLocalDate().equals(message.dateTime.toLocalDate())) {
                    DayHeader(message.dateTime.getDate())
                }

                Message(message = message, modifier = Modifier.fillMaxWidth())

                isLastItemPrimary = message.isPrimary
                lastTimeStamp = message.dateTime
            }
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
fun Message(message: Message, modifier: Modifier = Modifier) {
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
                        text = message.dateTime.getTime(),
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
fun InputTextField(value: String, onValueChange: (String) -> Unit, modifier: Modifier = Modifier) {

    Surface(
        contentColor = MaterialTheme.colorScheme.inverseSurface,
        color = (MaterialTheme.colorScheme.surfaceVariant),
        modifier = modifier
            .fillMaxWidth()
            .imePadding()
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
            IconButton(onClick = { /*TODO*/ },) {
                Icon(
                    imageVector = Icons.Outlined.Add,
                    contentDescription = stringResource(R.string.stickers_and_emojis)
                )
            }
            IconButton(onClick = { /*TODO*/ }) {
                Icon(
                    imageVector = Icons.Outlined.Phone,
                    contentDescription = stringResource(R.string.stickers_and_emojis)
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

    val messages = listOf<Message>(
        Message(
            "Starting: Intent { act=android.intent.action.MAIN cat=[android.intent.category.LAUNCHER] cmp=com.example.journalchat/.MainActivity }",
            dateTime = LocalDateTime.parse(
                "2012-12-24 12:24:35",
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
            )
        ),
        Message("Starting: =NCH"),
        Message("Starting: Intent { act=android.intent.action.MAIN cat=[android.intent.category.LAUNCHER] cmp=com.example.journalchat/.MainActivity }"),
        Message(
            "Startinge.journalchat/.MainActivity }",
            isPrimary = false
        ),
        Message("ASD most often refers to: Autism spectrum disorder, a neurodevelopmental condition. Читать ещё"),
        Message(
            "Подписчиков: 2,8 тыс.О себе: Группа компаний ASD - правообладатель зарегистрированных торговых марок «Секунда» (клеи для ремонта), Paterra (товары для дома), Aviora...",
            isPrimary = false
        ),
        Message("Подписчиков: 2,8 тыс.О себе: Группа компаний ASD - правообладатель зарегистрированных торговых марок «Секунда» (клеи для ремонта), Paterra (товары для дома), Aviora..."),
        Message(
            "Подписчиков: 2,8 тыс.О себе: Группа компаний ASD - правообладатель зарегистрированных торговых марок «Секунда» (клеи для ремонта), Paterra (товары для дома), Aviora...",
            isPrimary = false
        ),
        Message("Подписчиков: 2,8 тыс.О себе: Группа компаний ASD - правообладатель зарегистрированных торговых марок «Секунда» (клеи для ремонта), Paterra (товары для дома), Aviora..."),
    )
    val chatState = ChatState("FirstChat", messages)
    JournalChatTheme(useDarkTheme = true) {
        ChatScreen(chatState, {})
    }
}
