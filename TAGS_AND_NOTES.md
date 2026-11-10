# Tags Editor + Session Notes Editor

## Tags editor
- Button in the top bar of the Plugs list (label icon)
- Opens a full screen to add and delete tags
- Tags are stored locally and immediately available in the plug form

## Session notes editor
- Edit button on each session card in the History calendar
- Opens a notes editor for that session
- Saves notes back to the WearSession

## Files
- `ui/screens/tags/TagsEditorScreen.kt` + `TagsEditorRoute.kt`
- `ui/screens/history/SessionNotesEditor.kt`

Both are wired into navigation and hide the bottom bar while open.
