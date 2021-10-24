package ademar.tvmaze.usecase

import ademar.tvmaze.data.Episode
import ademar.tvmaze.data.Season
import ademar.tvmaze.db.EpisodeEntity
import ademar.tvmaze.network.payload.EpisodeResponse
import dagger.Reusable
import javax.inject.Inject

@Reusable
class MapEpisodes @Inject constructor() {

    fun mapEpisode(item: EpisodeResponse): Episode? {
        val id = item.id ?: return null
        val name = item.name ?: return null
        val number = item.number ?: return null
        val summary = item.summary ?: return null
        val season = item.season ?: return null
        val image = item.image?.original ?: item.image?.medium
        return Episode(
            id = id,
            name = name,
            number = number,
            summary = summary,
            season = season,
            image = image,
        )
    }

    fun mapEpisode(episode: Episode, season: Season): EpisodeEntity {
        return EpisodeEntity(
            id = episode.id,
            name = episode.name,
            number = episode.number,
            seasonId = season.id,
            summary = episode.summary,
            season = episode.season,
            image = episode.image,
        )
    }

    fun mapEpisode(entity: EpisodeEntity): Episode {
        return Episode(
            id = entity.id,
            name = entity.name,
            number = entity.number,
            summary = entity.summary,
            season = entity.season,
            image = entity.image,
        )
    }

}
