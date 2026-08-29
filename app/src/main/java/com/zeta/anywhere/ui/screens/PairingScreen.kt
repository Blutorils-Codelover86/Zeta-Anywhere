package com.zeta.anywhere.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardOptions
import androidx.compose.ui.unit.dp
import com.zeta.anywhere.ui.glass.ZetaGlassLevel
import com.zeta.anywhere.ui.glass.zetaGlass

@Composable
fun PairingScreen(
    onSubmitPairingCode: (String) -> Unit,
    onBack: () -> Unit,
    lastPairingMessage: String?,
    pairingInProgress: Boolean
) {
    var pairingCode by remember { mutableStateOf("") }

    CenteredScreenContainer {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .zetaGlass(ZetaGlassLevel.Interactive)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            HeaderBlock(
                title = "Pair Zeta",
                subtitle = "Enter the one-time code from your Zeta desktop app."
            )

            OutlinedTextField(
                value = pairingCode,
                onValueChange = { pairingCode = it.uppercase() },
                label = { Text("Pairing code") },
                placeholder = { Text("ZETA-7K9P-X4M2") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Characters),
                modifier = Modifier.fillMaxWidth()
            )

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Button(onClick = onBack, enabled = !pairingInProgress) { Text("BACK") }
                Button(
                    onClick = { onSubmitPairingCode(pairingCode) },
                    enabled = !pairingInProgress && pairingCode.isNotBlank()
                ) {
                    Text(if (pairingInProgress) "CONNECTING..." else "CONNECT")
                }
            }

            lastPairingMessage?.let { Text(text = it) }
            Text("Code is single-use and expires.")
        }
    }
}
