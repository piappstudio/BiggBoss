package model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Trend(

	@SerialName("image")
	val image: String? = null,

	@SerialName("title")
	val title: String? = null,

	@SerialName("url")
	val url: String? = null
)


@Serializable
data class TrendItem(

	@SerialName("title")
	val title: String? = null,

	@SerialName("items")
	val items: List<Trend>? = null
)
