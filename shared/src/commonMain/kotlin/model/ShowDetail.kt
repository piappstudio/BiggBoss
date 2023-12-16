package model

import kotlinx.datetime.Clock
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
		return if (!reEntryDate.isNullOrEmpty()) {
			!eliminatedDate.isNullOrEmpty() &&  !reEntryEvictedDate.isNullOrEmpty()
		}else {
			!eliminatedDate.isNullOrEmpty()
		}
	}

	fun noOfStars():Int {
		val allNotes = history?.flatMap { it.notes?: emptyList() }
		return allNotes?.count { note -> note.contains("GS", true) }?:0
	}

	fun countBasedOnKey(key:String):Int {
		val allNotes = history?.flatMap { it.notes?: emptyList() }
		return allNotes?.count { note -> note.compareTo(key, true) == 0 }?:0
	}

	fun noOfPoint():Int {
		val totalTTF = history?.sumOf { weekHistory ->
			weekHistory.notes?.filter { it.startsWith("TTF-") }
				?.sumOf {
					it.substringAfter("TTF-").toIntOrNull() ?: 1
				} ?: 0
		}
		return totalTTF?:0
	}

	fun totalDays():String {
		val startDate = startDate ?: PiGlobalInfo.episodeDetail?.startDate
		var endDate = eliminatedDate?.toDate()
		return if (eliminatedDate!=null && reEntryDate!=null) {
			// Start Date - First Eliminated
			val firstNominationDates = startDate?.toDate()?.daysSoFar(endDate!!)?:0L
			val reEntryDate = reEntryDate.toDate()?.daysSoFar(Clock.System.now())?:0L
			(firstNominationDates+reEntryDate).toString()
		} else {
			endDate = eliminatedDate?.toDate() ?: Clock.System.now()
			startDate?.toDate()?.daysSoFar(endDate).toString()
		}
	}
	fun howManyTimeINominated(arrOfKeys:List<Int>):Map<Int, Int> {
		val map = mutableMapOf<Int,Int>()
		arrOfKeys.forEach {key->
			val count = history?.flatMap { it.nominations ?: emptyList() }?.count { key ==it }?:0
			map[key] = count
		}
		return map
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
