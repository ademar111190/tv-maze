package ademar.tvmaze.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Show(
    val id: Long,
    val name: String,
    val summary: String,
    val image: String,
    val time: String,
    val days: Set<ScheduleDay>,
    val language: Language,
    val genres: Set<Genre>,
    val rating: Double,
) : Parcelable
