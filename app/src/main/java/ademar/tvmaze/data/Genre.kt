package ademar.tvmaze.data

import ademar.tvmaze.R
import androidx.annotation.DrawableRes
import timber.log.Timber

enum class Genre(
    @DrawableRes val icon: Int,
) {

    ACTION(R.drawable.ic_genre_action),
    ADVENTURE(R.drawable.ic_genre_adventure),
    ANIME(R.drawable.ic_genre_anime),
    COMEDY(R.drawable.ic_genre_comedy),
    CRIME(R.drawable.ic_genre_crime),
    DRAMA(R.drawable.ic_genre_drama),
    ESPIONAGE(R.drawable.ic_genre_espionage),
    FAMILY(R.drawable.ic_genre_family),
    FANTASY(R.drawable.ic_genre_fantasy),
    HISTORY(R.drawable.ic_genre_history),
    HORROR(R.drawable.ic_genre_horror),
    LEGAL(R.drawable.ic_genre_legal),
    MEDICAL(R.drawable.ic_genre_medical),
    MUSIC(R.drawable.ic_genre_music),
    MYSTERY(R.drawable.ic_genre_mystery),
    ROMANCE(R.drawable.ic_genre_romance),
    SCIENCE_FICTION(R.drawable.ic_genre_science_fiction),
    SPORTS(R.drawable.ic_genre_sports),
    SUPERNATURAL(R.drawable.ic_genre_supernatural),
    THRILLER(R.drawable.ic_genre_thriller),
    WAR(R.drawable.ic_genre_war),
    WESTERN(R.drawable.ic_genre_western);

    companion object {

        fun fromKeyword(keyword: String?): Genre? {
            return when (keyword?.lowercase()) {
                "action" -> ACTION
                "adventure" -> ADVENTURE
                "anime" -> ANIME
                "comedy" -> COMEDY
                "crime" -> CRIME
                "drama" -> DRAMA
                "espionage" -> ESPIONAGE
                "family" -> FAMILY
                "fantasy" -> FANTASY
                "history" -> HISTORY
                "horror" -> HORROR
                "legal" -> LEGAL
                "medical" -> MEDICAL
                "music" -> MUSIC
                "mystery" -> MYSTERY
                "romance" -> ROMANCE
                "science-fiction" -> SCIENCE_FICTION
                "sports" -> SPORTS
                "supernatural" -> SUPERNATURAL
                "thriller" -> THRILLER
                "war" -> WAR
                "western" -> WESTERN
                else -> {
                    Timber.w("Unknown genre keyword $keyword")
                    null
                }
            }
        }

    }

}
