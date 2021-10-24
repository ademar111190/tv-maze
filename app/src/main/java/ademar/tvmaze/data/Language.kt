package ademar.tvmaze.data

import ademar.tvmaze.R
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import timber.log.Timber

enum class Language(
    @DrawableRes val icon: Int,
    @StringRes val title: Int,
) {

    ENGLISH(R.drawable.lang_english, R.string.language_english),
    JAPANESE(R.drawable.lang_japanese, R.string.language_japanese),
    PORTUGUESE(R.drawable.lang_portuguese, R.string.language_portuguese),
    SPANISH(R.drawable.lang_spanish, R.string.language_spanish),
    OTHER(R.drawable.lang_other, R.string.language_other);

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
