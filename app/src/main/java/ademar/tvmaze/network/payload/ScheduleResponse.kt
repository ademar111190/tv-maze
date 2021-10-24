package ademar.tvmaze.network.payload

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ScheduleResponse(
    @Json(name = "time") val time: String?,
    @Json(name = "days") val days: List<String?>?,
)
