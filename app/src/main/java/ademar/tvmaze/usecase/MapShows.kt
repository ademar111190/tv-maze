package ademar.tvmaze.usecase

import ademar.tvmaze.data.Genre
import ademar.tvmaze.data.Language
import ademar.tvmaze.data.ScheduleDay
import ademar.tvmaze.data.Show
import ademar.tvmaze.db.GenreEntity
import ademar.tvmaze.db.ScheduleDayEntity
import ademar.tvmaze.db.ShowEntity
import ademar.tvmaze.db.ShowWithGenreWithScheduleDay
import ademar.tvmaze.network.payload.ShowResponse
import dagger.Reusable
import javax.inject.Inject

@Reusable
class MapShows @Inject constructor() {

    fun mapShow(item: ShowResponse): Show? {
        val id = item.id ?: return null
        val name = item.name ?: return null
        val image = item.image?.original ?: item.image?.medium ?: return null
        val language = item.language ?: return null
        val genres = item.genres?.mapNotNull { Genre.fromKeyword(it) }?.sorted()?.toSet() ?: emptySet()
        val rating = item.rating?.average ?: return null
        val summary = item.summary ?: return null
        val time = item.schedule?.time ?: return null
        val days = item.schedule.days?.mapNotNull { ScheduleDay.fromDay(it) }?.sorted()?.toSet() ?: emptySet()
        return Show(
            id = id,
            name = name,
            image = image,
            language = Language.fromKeyword(language),
            genres = genres,
            rating = rating,
            summary = summary,
            time = time,
            days = days,
        )
    }

    fun mapShow(show: Show): ShowWithGenreWithScheduleDay {
        return ShowWithGenreWithScheduleDay(
            show = ShowEntity(
                id = show.id,
                name = show.name,
                image = show.image,
                language = show.language,
                rating = show.rating,
                summary = show.summary,
                time = show.time,
            ),
            genres = show.genres.map {
                GenreEntity(
                    showId = show.id,
                    genre = it,
                )
            },
            scheduleDays = show.days.map {
                ScheduleDayEntity(
                    showId = show.id,
                    scheduleDay = it,
                )
            },
        )
    }

    fun mapShow(showEntity: ShowWithGenreWithScheduleDay): Show {
        return Show(
            id = showEntity.show.id,
            name = showEntity.show.name,
            image = showEntity.show.image,
            language = showEntity.show.language,
            genres = showEntity.genres.map { it.genre }.sorted().toSet(),
            rating = showEntity.show.rating,
            summary = showEntity.show.summary,
            time = showEntity.show.time,
            days = showEntity.scheduleDays.map { it.scheduleDay }.sorted().toSet(),
        )
    }

}
