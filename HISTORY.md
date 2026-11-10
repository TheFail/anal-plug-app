# History / Calendar View

Monthly calendar that shows wear sessions.

## Features
- Month navigation (prev/next)
- Days with sessions highlighted
- Tap a day to see the list of sessions for that day
- Each session shows plug name, start–end time, duration, and notes
- Fully local data via `WearSessionRepository.getAllSessionsWithPlug()`

## Files
- `ui/screens/history/HistoryScreen.kt` — calendar grid + day session list
- `ui/screens/history/HistoryRoute.kt` — connects to repository

## Navigation
Wired into the bottom nav “History” tab.

## Notes
- Calendar is a simple custom grid (no external library)
- Sessions are grouped by local day (midnight boundary)
- Ready for future enhancements (session detail, start/stop timer, Google Calendar export)
