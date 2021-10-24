package ademar.tvmaze.network.payload

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class RatingResponse(
    @Json(name = "average") val average: Double?,
)
