package com.zeta.anywhere.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.zeta.anywhere.ui.components.ZetaCoreVisual
import com.zeta.anywhere.ui.glass.ZetaGlassLevel
import com.zeta.anywhere.ui.glass.zetaGlass

@Composable
fun OnboardingScreen(
    onConnectClick: () -> Unit,
    loading: Boolean
) {
    CenteredScreenContainer {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .zetaGlass(ZetaGlassLevel.Prominent)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            ZetaCoreVisual()
            HeaderBlock(
                title = "Your AI, anywhere.",
                subtitle = "Zeta Anywhere connects to your personal Zeta installation."
            )
            Button(onClick = onConnectClick, enabled = !loading) {
                if (loading) {
                    CircularProgressIndicator(modifier = Modifier.padding(end = 8.dp))
                }
                Text("CONNECT ZETA")
            }
        }
    }
}
