package com.example.journalchat.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.journalchat.FloatingBottomButton
import com.example.journalchat.R
import com.example.journalchat.TopAppBar
import com.example.journalchat.models.Chat
import com.example.journalchat.ui.theme.Shapes


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateChatScreen(
    onButtonClick: (Chat) -> Unit,
    topBarNavIcon: @Composable () -> Unit,
    modifier: Modifier = Modifier
) {
    var text by rememberSaveable { mutableStateOf("") }
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())

    Scaffold(
        topBar = {
            TopAppBar(
                title = stringResource(id = R.string.app_name),
                navIcon = topBarNavIcon,
                topAppBarScrollBehavior = scrollBehavior
            )
        },
        bottomBar = { },
        modifier = modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection)
    ) {
        Box(modifier = modifier
            .fillMaxSize()
            .padding(it)) {
            CreateChat(text, { text = it }, modifier = Modifier)
            FloatingBottomButton(
                imageVector = Icons.Outlined.Check,
                buttonDescription = stringResource(R.string.create_chat),
                isVisible = true,
                onClick = {
                    onButtonClick(Chat(text, mutableListOf(), null))
                },
                Modifier.align(Alignment.BottomEnd)
            )
        }
    }
}

@Composable
fun CreateChat(
    text: String,
    onValueChange: (String) -> Unit,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    modifier: Modifier = Modifier
) {

    Column(modifier = modifier.padding(contentPadding)) {
        Card(
            modifier = modifier
                .clip(Shapes.medium)
                .fillMaxWidth(),
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
                    TextField(value = text, onValueChange = onValueChange, label = {
                        Text(
                            stringResource(R.string.enter_chat_name)
                        )
                    })
                }
            }
        }
    }

}

@Composable
@Preview
fun PreviewAddChat(modifier: Modifier = Modifier) {
    CreateChat(text = "", onValueChange = {})
}
