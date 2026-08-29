package com.zeta.anywhere.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.zeta.anywhere.domain.state.CallState
import com.zeta.anywhere.ui.components.ZetaCoreVisual
import com.zeta.anywhere.ui.glass.ZetaGlassLevel
import com.zeta.anywhere.ui.glass.zetaGlass

@Composable
fun ActiveCallScreen(
    state: CallState,
    onEnd: () -> Unit
) {
    val statusText = when (state) {
        is CallState.Connecting -> "CONNECTING"
        is CallState.Connected -> state.assistantState.name.uppercase()
        CallState.ConnectionLost -> "CONNECTION LOST"
        CallState.Timeout -> "CALL TIMEOUT"
        is CallState.Failed -> "CALL FAILED"
        else -> "IDLE"
    }

    CenteredScreenContainer {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .zetaGlass(ZetaGlassLevel.Core)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            ZetaCoreVisual()
            Text("ZETA")
            Text(statusText)
            Button(onClick = onEnd) { Text("END") }
        }
    }
}
