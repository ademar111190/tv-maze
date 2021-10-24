package ademar.tvmaze.network.payload

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ImageResponse(
    @Json(name = "medium") val medium: String?,
    @Json(name = "original") val original: String?,
)
