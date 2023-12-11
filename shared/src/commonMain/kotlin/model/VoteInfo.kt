package model
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class VoteInfo(
	@SerialName("Poll")
	val poll: List<PollItem>? = null
)
@Serializable
data class PollItem(

	@SerialName("name")
	val name: String? = null,

	@SerialName("votes")
	val votes: List<VotesItem>? = null
)
@Serializable
data class VotesItem(

	@SerialName("week")
	val week: Int? = null,

	@SerialName("players")
	val players: List<PlayerItem>? = null,

	@SerialName("last_update_date")
	val lastUpdateDate: String? = null
)
@Serializable
data class PlayerItem(

	@SerialName("percentage")
	val percentage: Float? = null,

	@SerialName("id")
	val id: Int? = null,

	@SerialName("vote")
	val vote: Int? = null
)
