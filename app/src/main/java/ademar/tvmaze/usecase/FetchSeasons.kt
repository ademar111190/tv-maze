package ademar.tvmaze.usecase

import ademar.tvmaze.data.Season
import ademar.tvmaze.data.Show
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
class FetchSeasons @Inject constructor(
    private val service: TvMazeService,
    private val db: AppDatabase,
    private val mapSeasons: MapSeasons,
    private val saveSeasons: SaveSeasons,
    @QualifiedScheduler(IO) private val ioScheduler: Scheduler,
    @QualifiedScheduler(MAIN_THREAD) private val mainThreadScheduler: Scheduler,
) {

    fun cached(): Single<List<Season>> = db.seasonDao().getAll()
        .subscribeOn(ioScheduler)
        .observeOn(mainThreadScheduler)
        .map { entity ->
            entity.map(mapSeasons::mapSeason)
        }

    fun newest(show: Show): Single<List<Season>> = service.seasons(show.id)
        .subscribeOn(ioScheduler)
        .observeOn(mainThreadScheduler)
        .map { payload ->
            payload.mapNotNull(mapSeasons::mapSeason)
        }
        .doOnSuccess { seasons ->
            saveSeasons.save(seasons, show)
        }

}
