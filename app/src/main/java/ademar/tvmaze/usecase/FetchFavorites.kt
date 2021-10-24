package ademar.tvmaze.usecase

import ademar.tvmaze.data.Show
import ademar.tvmaze.db.AppDatabase
import ademar.tvmaze.db.ShowWithGenreWithScheduleDay
import ademar.tvmaze.di.qualifiers.QualifiedScheduler
import ademar.tvmaze.di.qualifiers.QualifiedSchedulerOption.IO
import ademar.tvmaze.di.qualifiers.QualifiedSchedulerOption.MAIN_THREAD
import dagger.Reusable
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

@Reusable
class FetchFavorites @Inject constructor(
    private val db: AppDatabase,
    private val mapShows: MapShows,
    @QualifiedScheduler(IO) private val ioScheduler: Scheduler,
    @QualifiedScheduler(MAIN_THREAD) private val mainThreadScheduler: Scheduler,
) {

    fun all(): Single<List<Show>> = db.favoriteDao()
        .getAll()
        .subscribeOn(ioScheduler)
        .observeOn(mainThreadScheduler)
        .flattenAsObservable { it }
        .flatMapSingle { favorite ->
            db.showDao().getById(favorite.id)
                .subscribeOn(ioScheduler)
                .observeOn(mainThreadScheduler)
        }
        .collectInto(mutableListOf<ShowWithGenreWithScheduleDay>()) { list, show ->
            list.add(show)
        }
        .map { it.map(mapShows::mapShow) }

}
