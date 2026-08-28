package com.zeta.anywhere

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.zeta.anywhere.domain.state.AppSessionState
import com.zeta.anywhere.domain.state.CallState
import com.zeta.anywhere.ui.glass.ZetaGlassProvider
import com.zeta.anywhere.ui.screens.ActiveCallScreen
import com.zeta.anywhere.ui.screens.HomeScreen
import com.zeta.anywhere.ui.screens.IncomingCallScreen
import com.zeta.anywhere.ui.screens.OnboardingScreen
import com.zeta.anywhere.ui.screens.PairingScreen
import com.zeta.anywhere.ui.screens.SettingsScreen
import com.zeta.anywhere.ui.theme.ZetaAnywhereTheme
import com.zeta.anywhere.viewmodel.MainViewModel
import com.zeta.anywhere.viewmodel.MainViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val app = applicationContext as ZetaAnywhereApp
            ZetaAnywhereTheme {
                val vm: MainViewModel = viewModel(
                    factory = MainViewModelFactory(app.appContainer)
                )
                ZetaAnywhereApp(vm)
            }
        }
    }
}

@Composable
private fun ZetaAnywhereApp(viewModel: MainViewModel) {
    val appState by viewModel.appState.collectAsState()
    val callState by viewModel.callState.collectAsState()
    var showSettings by remember { mutableStateOf(false) }

    ZetaGlassProvider {
        when (val call = callState) {
            is CallState.Incoming -> IncomingCallScreen(
                call = call,
                onDecline = viewModel::declineIncomingCall,
                onAnswer = viewModel::answerIncomingCall
            )

            is CallState.Connected,
            is CallState.Connecting -> ActiveCallScreen(
                state = callState,
                onEnd = viewModel::endCall
            )

            else -> {
                when (appState) {
                    AppSessionState.Starting -> OnboardingScreen(
                        onConnectClick = viewModel::startPairing,
                        loading = true
                    )

                    AppSessionState.Unpaired -> OnboardingScreen(
                        onConnectClick = viewModel::startPairing,
                        loading = false
                    )

                    AppSessionState.Pairing -> PairingScreen(
                        onSubmitPairingCode = viewModel::submitPairingCode,
                        onBack = viewModel::cancelPairing,
                        lastPairingMessage = viewModel.lastPairingMessage.collectAsState().value,
                        pairingInProgress = viewModel.pairingInProgress.collectAsState().value
                    )

                    is AppSessionState.Paired -> {
                        if (showSettings) {
                            SettingsScreen(
                                state = appState,
                                mockMode = viewModel.mockMode,
                                onBack = { showSettings = false },
                                onDisconnect = {
                                    showSettings = false
                                    viewModel.disconnectZeta()
                                }
                            )
                        } else {
                            HomeScreen(
                                paired = appState,
                                callState = callState,
                                mockMode = viewModel.mockMode,
                                onOpenSettings = { showSettings = true },
                                onCallZeta = viewModel::startOutgoingCall,
                                onSimulateIncomingCall = viewModel::simulateIncomingCall,
                                onSetOnline = { viewModel.setZetaOnline(true) },
                                onSetOffline = { viewModel.setZetaOnline(false) }
                            )
                        }
                    }
                }
            }
        }
    }
}
