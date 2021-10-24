package ademar.tvmaze.usecase

import ademar.tvmaze.data.Episode
import ademar.tvmaze.data.Season
import ademar.tvmaze.db.AppDatabase
import ademar.tvmaze.di.qualifiers.QualifiedScheduler
import ademar.tvmaze.di.qualifiers.QualifiedSchedulerOption.IO
import ademar.tvmaze.di.qualifiers.QualifiedSchedulerOption.MAIN_THREAD
import ademar.tvmaze.network.api.TvMazeService
import dagger.Reusable
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

@Reusable
class FetchEpisodes @Inject constructor(
    private val service: TvMazeService,
    private val db: AppDatabase,
    private val mapEpisodes: MapEpisodes,
    private val saveEpisodes: SaveEpisodes,
    @QualifiedScheduler(IO) private val ioScheduler: Scheduler,
    @QualifiedScheduler(MAIN_THREAD) private val mainThreadScheduler: Scheduler,
) {

    fun cached(): Single<List<Episode>> = db.episodeDao().getAll()
        .subscribeOn(ioScheduler)
        .observeOn(mainThreadScheduler)
        .map { entity ->
            entity.map(mapEpisodes::mapEpisode)
        }

    fun newest(season: Season): Single<List<Episode>> = service.episodes(season.id)
        .subscribeOn(ioScheduler)
        .observeOn(mainThreadScheduler)
        .map { payload ->
            payload.mapNotNull(mapEpisodes::mapEpisode)
        }
        .doOnSuccess { episodes ->
            saveEpisodes.save(episodes, season)
        }

}
