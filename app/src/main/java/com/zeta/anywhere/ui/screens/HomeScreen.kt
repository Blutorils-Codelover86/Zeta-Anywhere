package com.zeta.anywhere.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.zeta.anywhere.domain.state.AppSessionState
import com.zeta.anywhere.domain.state.CallState
import com.zeta.anywhere.ui.components.ZetaCoreVisual
import com.zeta.anywhere.ui.glass.ZetaGlassLevel
import com.zeta.anywhere.ui.glass.zetaGlass

@Composable
fun HomeScreen(
    paired: AppSessionState.Paired,
    callState: CallState,
    mockMode: Boolean,
    onOpenSettings: () -> Unit,
    onCallZeta: () -> Unit,
    onSimulateIncomingCall: () -> Unit,
    onSetOnline: () -> Unit,
    onSetOffline: () -> Unit
) {
    CenteredScreenContainer {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .zetaGlass(ZetaGlassLevel.Prominent)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                IconButton(onClick = onOpenSettings) {
                    Icon(Icons.Default.Settings, contentDescription = "Settings")
                }
            }

            ZetaCoreVisual()
            Text("ZETA", style = MaterialTheme.typography.headlineMedium)
            Text("${paired.connectionStatus.name.replace('_', ' ')}")
            Text("Connected to ${paired.zetaName}")

            Button(onClick = onCallZeta) { Text("CALL ZETA") }

            if (mockMode) {
                Text("Development mode")
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Button(onClick = onSetOnline) { Text("ONLINE") }
                    Button(onClick = onSetOffline) { Text("OFFLINE") }
                }
                Button(onClick = onSimulateIncomingCall) { Text("SIMULATE INCOMING CALL") }
            }

            if (callState is CallState.Failed) {
                Text("Couldn't connect the call.")
            }
        }
    }
}
