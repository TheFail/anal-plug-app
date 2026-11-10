# Dashboard with Real Stats

Overview screen with live statistics.

## Shows
- **Average wear time** (completed sessions only, in minutes)
- **Total sessions**
- **Most used plugs** (top 5 by session count)

## Data source
- `WearSessionRepository.getAverageDurationMillis()`
- `WearSessionRepository.getMostUsedPlugIds()`
- `PlugRepository` for names

## Files
- `ui/screens/dashboard/DashboardScreen.kt` — pure UI cards
- `ui/screens/dashboard/DashboardRoute.kt` — loads live data

## Navigation
Wired into the bottom nav “Dashboard” tab.

## Notes
- Empty state when no sessions exist
- Updates automatically when sessions are added/stopped
