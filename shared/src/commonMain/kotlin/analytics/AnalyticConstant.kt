package analytics

object AnalyticConstant {
    object Screen {
        const val HOME = "HOME"
        const val EPISODE_DETAIL = "EPISODE_DETAIL"
        const val PARTICIPANT_DETAIL = "PARTICIPANT_DETAIL"
        const val VOTING = "VOTING"
        const val TRENDING = "TRENDING"
        const val ELIMINATED = "ELIMINATED"

        const val ALL = "ALL"
        const val NOMINATIONS = "NOMINATIONS"
        const val NOTIFICATIONS = "NOTIFICATIONS"
    }

    object Event {
        const val SCREEN_VIEW = "screen_view"
        const val CLICKED = "click_event"
        const val YOUTUBE = "youtube"
    }

    object Params {
        const val SHOW_NAME = "SHOW_NAME"
        const val PARTICIPANT_NAME = "PARTICIPANT_NAME"
        const val CLICK_ITEM = "CLICK_ITEM"
        const val ACTION_NAME = "action_name"
        const val SCREEN_NAME = "screen_name"
        const val URL = "url"
        const val PART_IDS = "Compare_IdS"
    }
}