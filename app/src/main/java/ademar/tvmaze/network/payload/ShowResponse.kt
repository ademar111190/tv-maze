package ademar.tvmaze.network.payload

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ShowItem(
    @Json(name = "id") val id: Long?,
    @Json(name = "name") val name: String?,
    @Json(name = "summary") val summary: String?,
    @Json(name = "image") val image: ShowImage?,
    @Json(name = "language") val language: String?,
    @Json(name = "genres") val genres: List<String?>?,
    @Json(name = "rating") val rating: Rating?,
    @Json(name = "schedule") val schedule: Schedule?,
)

@JsonClass(generateAdapter = true)
data class ShowImage(
    @Json(name = "medium") val medium: String?,
    @Json(name = "original") val original: String?,
)

@JsonClass(generateAdapter = true)
data class Rating(
    @Json(name = "average") val average: Double?,
)

@JsonClass(generateAdapter = true)
data class Schedule(
    @Json(name = "time") val time: String?,
    @Json(name = "days") val days: List<String?>?,
)
