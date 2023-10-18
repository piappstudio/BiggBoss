package model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class WeeklyInfo(
	@SerialName("nominations")
	val nominations: List<NominationsItem>? = null
)

@Serializable
data class NominationsItem(

	@SerialName("options")
	val options: List<OptionsItem>? = null,

	@SerialName("id")
	val id: Int? = null
)
@Serializable
data class Options(

	@SerialName("options")
	val options: List<OptionsItem>? = null
)
@Serializable
data class OptionsItem(

	@SerialName("week")
	val week: Int? = null,

	@SerialName("options")
	val options: List<Int?>? = null
)
