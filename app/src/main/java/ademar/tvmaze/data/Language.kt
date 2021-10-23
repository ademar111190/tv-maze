package ademar.tvmaze.data

import ademar.tvmaze.R
import androidx.annotation.DrawableRes

enum class Language(
    @DrawableRes val icon: Int,
) {
    ENGLISH(R.drawable.lang_english),
    JAPANESE(R.drawable.lang_japanese),
    PORTUGUESE(R.drawable.lang_portuguese),
    SPANISH(R.drawable.lang_spanish),
    OTHER(R.drawable.lang_other),
}
