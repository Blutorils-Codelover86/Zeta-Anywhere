package com.zeta.anywhere

import android.app.Application
import com.zeta.anywhere.core.config.AppConfig
import com.zeta.anywhere.core.security.CredentialStore
import com.zeta.anywhere.core.security.SecureCredentialStore
import com.zeta.anywhere.data.services.CallService
import com.zeta.anywhere.data.services.ConnectionService
import com.zeta.anywhere.data.services.PairingService
import com.zeta.anywhere.data.services.mock.MockCallService
import com.zeta.anywhere.data.services.mock.MockConnectionService
import com.zeta.anywhere.data.services.mock.MockPairingService
import com.zeta.anywhere.data.services.remote.RemoteCallService
import com.zeta.anywhere.data.services.remote.RemoteConnectionService
import com.zeta.anywhere.data.services.remote.RemotePairingService

class ZetaAnywhereApp : Application() {
    lateinit var appContainer: AppContainer
        private set

    override fun onCreate() {
        super.onCreate()
        appContainer = AppContainer(this)
    }
}

class AppContainer(application: Application) {
    val config = AppConfig.fromBuildConfig()
    val credentialStore: CredentialStore = SecureCredentialStore(application)

    val pairingService: PairingService =
        if (config.mockMode) MockPairingService(config.pairingTtlSeconds)
        else RemotePairingService(config)

    val connectionService: ConnectionService =
        if (config.mockMode) MockConnectionService()
        else RemoteConnectionService(config)

    val callService: CallService =
        if (config.mockMode) MockCallService()
        else RemoteCallService(config)
}
