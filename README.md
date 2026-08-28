# Zeta Anywhere

Zeta Anywhere is a standalone Android companion app for a future Zeta desktop assistant.

## What this project includes

- Independent Android app (no dependency on desktop Zeta source)
- Kotlin + Jetpack Compose architecture
- Structured app and call state models
- Pairing contract with mock and remote service abstraction
- Connection and call service abstraction with mock simulation
- Secure credential storage using encrypted preferences
- FCM-ready call architecture placeholders
- AGSL-backed Zeta Glass design primitives with graceful fallback

## Architecture

```
app/src/main/java/com/zeta/anywhere
├── core
│   ├── config
│   ├── network
│   └── security
├── data
│   └── services
│       ├── mock
│       └── remote
├── domain
│   ├── models
│   └── state
├── ui
│   ├── components
│   ├── glass
│   ├── screens
│   └── theme
└── viewmodel
```

## Requirements

- Android Studio (latest stable)
- Android SDK 35
- JDK 17

## Run

1. Copy `.env.example` to `.env` and adjust values.
2. Keep `ZETA_MOCK_MODE=true` for development.
3. Open project in Android Studio.
4. Run app on emulator/device.

## Development mode behavior

Mock mode simulates:

- Successful pairing
- Invalid pairing code
- Expired pairing code
- Used pairing code
- Connection online/offline
- Incoming call
- Answer / decline / end call

## Pairing test codes

- Valid: `ZETA-7K9P-X4M2`
- Invalid: `INVALID-CODE`
- Used: `ZETA-USED-A1B2`
- Expired: `ZETA-EXPIRED-A1`
- Network fail simulation: include `NETFAIL` in a valid code slot when extending mock

## Testing

Run unit tests:

```bash
./gradlew test
```

## Security notes

- Do not commit real backend keys or tokens.
- Pairing code is temporary and single-use in mock behavior.
- Long-term credentials are stored via encrypted preferences.

## Integration contract

See [`docs/integration.md`](docs/integration.md) for future Zeta desktop/backend API requirements.
