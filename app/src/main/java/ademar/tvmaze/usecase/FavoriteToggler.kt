package ademar.tvmaze.usecase

import ademar.tvmaze.db.AppDatabase
import ademar.tvmaze.db.FavoriteEntity
import ademar.tvmaze.di.qualifiers.QualifiedScheduler
import ademar.tvmaze.di.qualifiers.QualifiedSchedulerOption.IO
import ademar.tvmaze.di.qualifiers.QualifiedSchedulerOption.MAIN_THREAD
import dagger.Reusable
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Scheduler
import javax.inject.Inject

@Reusable
class FavoriteToggler @Inject constructor(
    private val db: AppDatabase,
    private val favoriteCheck: FavoriteCheck,
    @QualifiedScheduler(IO) private val ioScheduler: Scheduler,
    @QualifiedScheduler(MAIN_THREAD) private val mainThreadScheduler: Scheduler,
) {

    fun toggle(showId: Long): Completable = favoriteCheck.isFavorite(showId)
        .flatMapCompletable { favorite ->
            if (favorite) {
                unfavorite(showId)
            } else {
                favorite(showId)
            }
        }

    fun favorite(showId: Long): Completable = db.favoriteDao()
        .insert(FavoriteEntity(showId))
        .subscribeOn(ioScheduler)
        .observeOn(mainThreadScheduler)

    fun unfavorite(showId: Long): Completable = db.favoriteDao()
        .delete(showId)
        .subscribeOn(ioScheduler)
        .observeOn(mainThreadScheduler)

}
