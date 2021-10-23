package ademar.tvmaze.data

import ademar.tvmaze.R
import androidx.annotation.DrawableRes
import timber.log.Timber

enum class Language(
    @DrawableRes val icon: Int,
) {

    ENGLISH(R.drawable.lang_english),
    JAPANESE(R.drawable.lang_japanese),
    PORTUGUESE(R.drawable.lang_portuguese),
    SPANISH(R.drawable.lang_spanish),
    OTHER(R.drawable.lang_other);

    companion object {

        fun fromKeyword(keyword: String?): Language {
            return when (keyword?.lowercase()) {
                "english" -> ENGLISH
                "japanese" -> JAPANESE
                "portuguese" -> PORTUGUESE
                "spanish" -> SPANISH
                else -> {
                    Timber.w("Unknown language keyword $keyword")
                    OTHER
                }
            }
        }

    }

}
