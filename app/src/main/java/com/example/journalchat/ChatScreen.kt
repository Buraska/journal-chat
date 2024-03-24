package com.example.journalchat

import android.graphics.drawable.Drawable
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.journalchat.ui.theme.Shapes

class ChatScreen {



    @Composable
    fun ChatItem(name:String, icon: Drawable? = null,modifier: Modifier = Modifier){
        Card(
            modifier = modifier
                .fillMaxWidth()
                .clip(Shapes.medium),
            elevation = CardDefaults.cardElevation(defaultElevation = dimensionResource(id = R.dimen.card_elevation))
        ) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(dimensionResource(id = R.dimen.padding_medium))
            ) {
/*                Image(
                    painter = null,
                    contentDescription = stringResource(R.string.chat_icon),
                    modifier = Modifier
                        .clip(Shapes.medium)
                        .weight(0.3f)
                )*/
                Spacer(Modifier.width(dimensionResource(id = R.dimen.spacer_medium)))
                Column(modifier = Modifier.weight(1f)) {
                    Text(name, style = MaterialTheme.typography.displaySmall)
                }
            }
        }
    }


    @Composable
    @Preview(showBackground = true)
    fun ChatPreview(){
        ChatItem(name = "Theme of Monseratte")
    }
}