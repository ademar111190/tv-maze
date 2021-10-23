package ademar.tvmaze.usecase

import ademar.tvmaze.data.Show
import ademar.tvmaze.db.AppDatabase
import ademar.tvmaze.di.qualifiers.QualifiedScheduler
import ademar.tvmaze.di.qualifiers.QualifiedSchedulerOption.IO
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.disposables.CompositeDisposable
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SaveShows @Inject constructor(
    private val db: AppDatabase,
    private val mapShows: MapShows,
    private val subscriptions: CompositeDisposable,
    @QualifiedScheduler(IO) private val ioScheduler: Scheduler,
) {

    fun save(shows: List<Show>) {
        shows.map(mapShows::mapShow).onEach { showWithGenre ->
            db.showDao().let { dao ->
                subscriptions.add(
                    dao.insert(showWithGenre.show)
                        .subscribeOn(ioScheduler)
                        .subscribe({}, Timber::e)
                )
                subscriptions.add(
                    dao.clearGenres(showWithGenre.show.id)
                        .subscribeOn(ioScheduler)
                        .andThen {
                            showWithGenre.genres.onEach {
                                subscriptions.add(
                                    dao.insert(it)
                                        .subscribeOn(ioScheduler)
                                        .subscribe({}, Timber::e)
                                )
                            }
                        }
                        .subscribe({}, Timber::e)
                )
            }
        }
    }

}
