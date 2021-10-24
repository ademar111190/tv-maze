package ademar.tvmaze.usecase

import ademar.tvmaze.data.Episode
import ademar.tvmaze.data.Season
import ademar.tvmaze.db.AppDatabase
import ademar.tvmaze.di.qualifiers.QualifiedScheduler
import ademar.tvmaze.di.qualifiers.QualifiedSchedulerOption.IO
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.disposables.CompositeDisposable
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SaveEpisodes @Inject constructor(
    private val db: AppDatabase,
    private val mapEpisodes: MapEpisodes,
    private val subscriptions: CompositeDisposable,
    @QualifiedScheduler(IO) private val ioScheduler: Scheduler,
) {

    fun save(episodes: List<Episode>, season: Season) {
        episodes.map { mapEpisodes.mapEpisode(it, season) }.onEach { episode ->
            db.episodeDao().let { dao ->
                subscriptions.add(
                    dao.insert(episode)
                        .subscribeOn(ioScheduler)
                        .subscribe({}, Timber::e)
                )
            }
        }
    }

}
