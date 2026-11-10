# Session Logging (Start / Stop Timer)

Simple start/stop wear session control.

## How it works
- On a plug’s detail screen there is a **Session** card
- **Start session** → creates a `WearSession` with `startTime = now` and `endTime = null`
- **Stop session** → sets `endTime = now` on the ongoing session
- Shows elapsed minutes while a session is running
- All data is local via `WearSessionRepository`

## Files
- `ui/session/SessionControlCard.kt` — pure UI card
- `ui/session/SessionControlRoute.kt` — loads ongoing session and saves start/stop

## Integration
Added to the top of the Plug detail screen so you can start/stop directly from a plug.

## Result
Sessions immediately appear in the History calendar view.
