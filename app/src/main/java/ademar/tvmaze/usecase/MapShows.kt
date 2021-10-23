package ademar.tvmaze.usecase

import ademar.tvmaze.data.Genre
import ademar.tvmaze.data.Language
import ademar.tvmaze.data.Show
import ademar.tvmaze.db.GenreEntity
import ademar.tvmaze.db.ShowEntity
import ademar.tvmaze.db.ShowWithGenre
import ademar.tvmaze.network.payload.ShowItem
import dagger.Reusable
import javax.inject.Inject

@Reusable
class MapShows @Inject constructor() {

    fun mapShow(item: ShowItem): Show? {
        val id = item.id ?: return null
        val name = item.name ?: return null
        val image = item.image?.original ?: item.image?.medium ?: return null
        val language = item.language ?: return null
        val genres = item.genres?.mapNotNull { Genre.fromKeyword(it) }?.sorted() ?: emptyList()
        val rating = item.rating?.average ?: return null
        return Show(
            id = id,
            name = name,
            image = image,
            language = Language.fromKeyword(language),
            genres = genres,
            rating = rating,
        )
    }

    fun mapShow(show: Show): ShowWithGenre {
        return ShowWithGenre(
            show = ShowEntity(
                id = show.id,
                name = show.name,
                image = show.image,
                language = show.language,
                rating = show.rating,
            ),
            genres = show.genres.map {
                GenreEntity(
                    showId = show.id,
                    genre = it,
                )
            },
        )
    }

    fun mapShow(showWithGenre: ShowWithGenre): Show {
        return Show(
            id = showWithGenre.show.id,
            name = showWithGenre.show.name,
            image = showWithGenre.show.image,
            language = showWithGenre.show.language,
            genres = showWithGenre.genres.map { it.genre }.sorted(),
            rating = showWithGenre.show.rating,
        )
    }

}
