package model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


enum class LinkType {
	URL,
	DIAL
}
@Serializable
data class OfficialVoteItem(

	@SerialName("name")
	val name: String? = null,

	@SerialName("link")
	val link: String? = null,

	val type:LinkType = LinkType.URL
)

@Serializable
data class UnofficialVotingItem(

	@SerialName("name")
	val name: String? = null,

	@SerialName("link")
	val link: String? = null
)

@Serializable
data class VotingOption(
	@SerialName("official_vote")
	val officialVote: List<OfficialVoteItem>? = null,

	@SerialName("unofficial_voting")
	val unofficialVoting: List<UnofficialVotingItem>? = null
)
