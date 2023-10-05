package model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ShowDetail(

	@SerialName("reviewers")
	val reviewers: List<ReviewerItem?>? = null,

	@SerialName("participants")
	val participants: List<ParticipantItem?>? = null
)

@Serializable
data class ReviewerItem(

	@SerialName("channel_name")
	val channelName: String? = null,

	@SerialName("image")
	val image: String? = null,

	@SerialName("url")
	val url: String? = null
)

@Serializable
data class ParticipantItem(

	@SerialName("image")
	val image: String? = null,

	@SerialName("eliminated_date")
	val eliminatedDate: String? = null,

	@SerialName("name")
	val name: String? = null,

	@SerialName("id")
	val id: String? = null,

	@SerialName("isNominated")
	val isNominated: Boolean? = null
)
