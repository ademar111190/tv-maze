package ademar.tvmaze.usecase

import ademar.tvmaze.data.Episode
import ademar.tvmaze.db.AppDatabase
import ademar.tvmaze.di.qualifiers.QualifiedScheduler
import ademar.tvmaze.di.qualifiers.QualifiedSchedulerOption.IO
import ademar.tvmaze.di.qualifiers.QualifiedSchedulerOption.MAIN_THREAD
import dagger.Reusable
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

@Reusable
class FetchEpisode @Inject constructor(
    private val db: AppDatabase,
    private val mapEpisodes: MapEpisodes,
    @QualifiedScheduler(IO) private val ioScheduler: Scheduler,
    @QualifiedScheduler(MAIN_THREAD) private val mainThreadScheduler: Scheduler,
) {

    fun cached(id: Long): Single<Episode> = db.episodeDao()
        .getById(id)
        .subscribeOn(ioScheduler)
        .observeOn(mainThreadScheduler)
        .map(mapEpisodes::mapEpisode)

}
