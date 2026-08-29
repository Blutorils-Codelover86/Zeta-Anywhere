package com.zeta.anywhere.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.zeta.anywhere.domain.state.AppSessionState
import com.zeta.anywhere.ui.glass.ZetaGlassLevel
import com.zeta.anywhere.ui.glass.zetaGlass

@Composable
fun SettingsScreen(
    state: AppSessionState.Paired,
    mockMode: Boolean,
    onBack: () -> Unit,
    onDisconnect: () -> Unit
) {
    var confirm by remember { mutableStateOf(false) }

    CenteredScreenContainer {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .zetaGlass(ZetaGlassLevel.Ambient)
                .padding(24.dp),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Text("ZETA ANYWHERE")
            Text("Connected Zeta: ${state.zetaName}")
            Text("Connection Status: ${state.connectionStatus.name}")
            Text("Notifications")
            Text("Remote Calls")
            Text("About")
            Text(if (mockMode) "Mode: Development/Mock" else "Mode: Remote")

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Button(onClick = onBack) { Text("BACK") }
                Button(onClick = { confirm = true }) { Text("DISCONNECT ZETA") }
            }
        }
    }

    if (confirm) {
        AlertDialog(
            onDismissRequest = { confirm = false },
            confirmButton = {
                Button(onClick = {
                    confirm = false
                    onDisconnect()
                }) {
                    Text("DISCONNECT")
                }
            },
            dismissButton = {
                Button(onClick = { confirm = false }) {
                    Text("CANCEL")
                }
            },
            title = { Text("Disconnect Zeta?") },
            text = { Text("This removes local credentials and returns the app to the unpaired state.") }
        )
    }
}
