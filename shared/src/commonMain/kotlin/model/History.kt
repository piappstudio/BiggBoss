package model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class HistoryItem(

	@SerialName("week")
	val week: Int? = null,

	@SerialName("notes")
	val notes: List<String>? = null,
	@SerialName("nominates")
	val nominations:List<Int>?= null,
	val nominatedBy:List<ParticipantItem>? = null
)
