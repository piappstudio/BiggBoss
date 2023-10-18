package model

import kotlinx.serialization.*
@Serializable
data class ShowList(
	@SerialName("shows")
	val shows: List<ShowItem>? = null,
	@SerialName("reviewers")
	val reviewers: List<ReviewerItem>? = null,
)

@Serializable
data class ShowItem(

	@SerialName("end_date")
	val endDate: String? = null,

	@SerialName("host")
	val host: String? = null,

	@SerialName("logo")
	val logo: String? = null,

	@SerialName("more_info")
	val moreInfo: String? = null,

	@SerialName("id")
	val id: String? = null,

	@SerialName("title")
	val title: String? = null,

	@SerialName("start_date")
	val startDate: String? = null,

	@SerialName("trends")
	val trends:String? = null
)