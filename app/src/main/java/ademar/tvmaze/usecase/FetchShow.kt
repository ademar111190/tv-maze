package ademar.tvmaze.usecase

import ademar.tvmaze.data.Show
import ademar.tvmaze.db.AppDatabase
import ademar.tvmaze.di.qualifiers.QualifiedScheduler
import ademar.tvmaze.di.qualifiers.QualifiedSchedulerOption.IO
import ademar.tvmaze.di.qualifiers.QualifiedSchedulerOption.MAIN_THREAD
import dagger.Reusable
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

@Reusable
class FetchShow @Inject constructor(
    private val db: AppDatabase,
    private val mapShows: MapShows,
    @QualifiedScheduler(IO) private val ioScheduler: Scheduler,
    @QualifiedScheduler(MAIN_THREAD) private val mainThreadScheduler: Scheduler,
) {

    fun byId(id: Long): Single<Show> = db.showDao().getById(id)
        .subscribeOn(ioScheduler)
        .observeOn(mainThreadScheduler)
        .map(mapShows::mapShow)

}
