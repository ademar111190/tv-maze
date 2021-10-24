package ademar.tvmaze.usecase

import ademar.tvmaze.data.Show
import ademar.tvmaze.db.AppDatabase
import ademar.tvmaze.di.qualifiers.QualifiedScheduler
import ademar.tvmaze.di.qualifiers.QualifiedSchedulerOption.IO
import ademar.tvmaze.di.qualifiers.QualifiedSchedulerOption.MAIN_THREAD
import ademar.tvmaze.network.api.TvMazeService
import ademar.tvmaze.network.payload.ShowResponse
import dagger.Reusable
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

@Reusable
class FetchShows @Inject constructor(
    private val service: TvMazeService,
    private val db: AppDatabase,
    private val mapShows: MapShows,
    private val saveShows: SaveShows,
    @QualifiedScheduler(IO) private val ioScheduler: Scheduler,
    @QualifiedScheduler(MAIN_THREAD) private val mainThreadScheduler: Scheduler,
) {

    fun cached(): Single<List<Show>> = db.showDao().getAll()
        .subscribeOn(ioScheduler)
        .observeOn(mainThreadScheduler)
        .map { entity ->
            entity.map(mapShows::mapShow)
        }

    fun firstPage(): Single<List<Show>> = fetch(service.shows())

    fun numberedPage(page: Int): Single<List<Show>> = fetch(service.shows(page))

    private fun fetch(source: Single<List<ShowResponse>>): Single<List<Show>> = source
        .subscribeOn(ioScheduler)
        .observeOn(mainThreadScheduler)
        .map { payload ->
            payload.mapNotNull(mapShows::mapShow)
        }
        .doOnSuccess(saveShows::save)

}
