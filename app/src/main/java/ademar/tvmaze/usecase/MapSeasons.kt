package ademar.tvmaze.usecase

import ademar.tvmaze.data.Season
import ademar.tvmaze.data.Show
import ademar.tvmaze.db.SeasonEntity
import ademar.tvmaze.network.payload.SeasonResponse
import dagger.Reusable
import javax.inject.Inject

@Reusable
class MapSeasons @Inject constructor() {

    fun mapSeason(item: SeasonResponse): Season? {
        val id = item.id ?: return null
        val name = item.name ?: return null
        val number = item.number ?: return null
        return Season(
            id = id,
            name = name,
            number = number,
        )
    }

    fun mapSeason(season: Season, show: Show): SeasonEntity {
        return SeasonEntity(
            id = season.id,
            showId = show.id,
            name = season.name,
            number = season.number,
        )
    }

    fun mapSeason(entity: SeasonEntity): Season {
        return Season(
            id = entity.id,
            name = entity.name,
            number = entity.number,
        )
    }

}
