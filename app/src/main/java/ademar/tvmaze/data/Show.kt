package ademar.tvmaze.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Show(
    val id: Long,
    val name: String,
    val image: String,
    val language: Language,
    val genres: List<Genre>,
    val rating: Double,
) : Parcelable
