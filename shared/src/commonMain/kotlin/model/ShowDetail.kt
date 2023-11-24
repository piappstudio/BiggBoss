package model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import ui.component.shared.PiSingleNotification


@Serializable
data class ShowDetail(

	@SerialName("voting_options")
	val votingOption: VotingOption?=null,
	@SerialName("participants")
	val participants: List<ParticipantItem>? = null,
	val notifications: List<PiSingleNotification>? = null,

	val startDate: String?=null

	)

@Serializable
data class ParticipantItem(

	@SerialName("image")
	val image: String? = null,
	@SerialName("start_date")
	val startDate:String? = null,
	@SerialName("eliminated_date")
	val eliminatedDate: String? = null,
	@SerialName("re_entry_date")
	val reEntryDate:String? = null,
	@SerialName("re_evicted_date")
	val reEntryEvictedDate:String? =null,
	@SerialName("name")
	val name: String? = null,

	@SerialName("id")
	val id: String? = null,

	@SerialName("isNominated")
	val isNominated: Boolean? = null,

	@SerialName("isCaptain")
	val isCaptain: Boolean? = null,

	@SerialName("full_image")
	val fullImage:String?=null,

	@SerialName("dial_number")
	val dialNumber:String? = null,
	@SerialName("history")
	val history: List<HistoryItem>? = null
) {
	fun isEliminated():Boolean {
		if (!reEntryDate.isNullOrEmpty()) {
			return !eliminatedDate.isNullOrEmpty() &&  !reEntryEvictedDate.isNullOrEmpty()
		}else {
			return !eliminatedDate.isNullOrEmpty()
		}
	}
}


inline fun <reified T> parseJsonToData(jsonString: String):T?{
	return try {
		val json = Json { ignoreUnknownKeys = true }
		json.decodeFromString(jsonString)
	} catch (e: Exception) {
		null
	}
}
@Serializable
data class ReviewerItem(

	@SerialName("channel_name")
	val channelName: String? = null,

	@SerialName("image")
	val image: String? = null,

	@SerialName("url")
	val url: String? = null,

	@SerialName("description")
	val description:String? = null
)
