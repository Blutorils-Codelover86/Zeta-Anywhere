# Zeta Anywhere Integration Contract (Desktop/Backend)

This document defines the API contract the future Zeta desktop + backend stack must implement to integrate with Zeta Anywhere.

## 1) Pairing

### Pairing code generation requirements

- Format: `ZETA-XXXX-XXXX` (`X` = uppercase alphanumeric)
- Expiration: default 10 minutes (configurable)
- Single-use: code becomes invalid immediately after successful exchange
- Security: code is not a persistent credential

### Pair endpoint

`POST /api/v1/pair`

Request:

```json
{
  "pairing_code": "ZETA-7K9P-X4M2",
  "device_name": "Android Phone",
  "device_type": "android"
}
```

Success response:

```json
{
  "success": true,
  "zeta_id": "zeta_abc123",
  "device_id": "device_xyz789",
  "access_token": "token",
  "refresh_token": "refresh_token",
  "expires_in": 3600,
  "endpoint": "https://region.zeta-service.example"
}
```

Failure response examples:

```json
{ "success": false, "error": "invalid_pairing_code" }
{ "success": false, "error": "pairing_code_expired" }
{ "success": false, "error": "pairing_code_used" }
```

## 2) Authentication

- Use short-lived access tokens (Bearer)
- Use refresh tokens/device session renewal where applicable
- Device revocation must be supported
- Token scopes must prevent unauthorized call/session access

### Suggested endpoints

- `POST /api/v1/auth/refresh`
- `POST /api/v1/devices/{device_id}/revoke`

## 3) Connection state

### Expected endpoint

`GET /api/v1/connection/state`

Example response:

```json
{
  "connected": true,
  "zeta_status": "online",
  "last_seen": "2026-08-28T13:30:00Z"
}
```

Status values:

- `online`
- `offline`
- `starting`
- `maintenance`

## 4) Remote calls

### Call initiation from Zeta side

Desktop triggers backend to create call session:

`POST /api/v1/calls`

```json
{
  "reason": "important_event",
  "priority": "normal"
}
```

Response:

```json
{
  "call_id": "call_abc123",
  "created_at": "2026-08-28T13:40:00Z",
  "expires_at": "2026-08-28T13:45:00Z"
}
```

### Notification payload (FCM)

Notification/data should only include retrieval identifiers:

```json
{
  "type": "incoming_call",
  "call_id": "call_abc123"
}
```

Do **not** include secrets or session auth in FCM payload.

### Call session retrieval

`GET /api/v1/calls/{call_id}`

```json
{
  "call_id": "call_abc123",
  "state": "ringing",
  "from": "zeta_abc123",
  "display_name": "Zeta",
  "signaling_ws_url": "wss://signal.zeta-service.example/calls/call_abc123"
}
```

## 5) Real-time communication and signaling

### Signaling (WebSocket)

Use authenticated WebSocket signaling for:

- SDP offer/answer exchange
- ICE candidate exchange
- Call control events (answer/decline/end)

Suggested events:

- `call.offer`
- `call.answer`
- `call.ice_candidate`
- `call.end`
- `call.error`

### WebRTC requirements

- Support ICE, STUN, TURN
- TURN fallback mandatory for NAT-restricted cases
- Session auth required before SDP/ICE exchange
- Expired/unauthorized call sessions must be rejected

## 6) Device disconnect flow

When user disconnects in app:

1. Local credentials are deleted from secure storage.
2. App returns to unpaired state.
3. Backend should support explicit device revocation to invalidate tokens.

## 7) Error contract recommendations

Use stable machine-readable error codes:

- `invalid_pairing_code`
- `pairing_code_expired`
- `pairing_code_used`
- `auth_failed`
- `zeta_offline`
- `call_timeout`
- `call_connect_failed`
- `network_error`

Return safe user-facing messages from app; do not expose stack traces.
