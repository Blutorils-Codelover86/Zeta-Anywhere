package com.zeta.anywhere.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
fun IncomingCallScreen(
    call: CallState.Incoming,
    onDecline: () -> Unit,
    onAnswer: () -> Unit
) {
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
            HeaderBlock(title = call.title, subtitle = call.body)
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                Button(onClick = onDecline) { Text("DECLINE") }
                Button(onClick = onAnswer) { Text("ANSWER") }
            }
        }
    }
}
