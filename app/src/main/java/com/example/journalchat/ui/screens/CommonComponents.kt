package com.example.journalchat.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.journalchat.ui.theme.Typography

@Composable
fun DeleteAlertDialog(
    onDelete: () -> Unit,
    onDismissRequest: () -> Unit,
    notificationTitle: String,
    notificationBody: String,
    modifier: Modifier = Modifier
) {
    Dialog(onDismissRequest) {
        Card(modifier = modifier, shape = ShapeDefaults.Medium) {
            Column(modifier = Modifier.padding(16.dp, 16.dp, 16.dp, 8.dp)) {
                Text(
                    notificationTitle,
                    style = Typography.titleMedium,
                    modifier = Modifier
                )
                Text(
                    notificationBody,
                    style = Typography.bodyMedium
                )
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