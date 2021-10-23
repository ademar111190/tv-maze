package ademar.tvmaze.usecase

import ademar.tvmaze.data.Genre
import ademar.tvmaze.data.Language
import ademar.tvmaze.data.Show
import ademar.tvmaze.db.AppDatabase
import ademar.tvmaze.db.GenreEntity
import ademar.tvmaze.db.ShowEntity
import ademar.tvmaze.db.ShowWithGenre
import ademar.tvmaze.di.qualifiers.QualifiedScheduler
import ademar.tvmaze.di.qualifiers.QualifiedSchedulerOption.IO
import ademar.tvmaze.di.qualifiers.QualifiedSchedulerOption.MAIN_THREAD
import ademar.tvmaze.network.api.TvMazeService
import ademar.tvmaze.network.payload.ShowItem
import dagger.Reusable
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.core.Single
import timber.log.Timber
import javax.inject.Inject

@Reusable
class FetchShows @Inject constructor(
    private val service: TvMazeService,
    private val db: AppDatabase,
    @QualifiedScheduler(IO) private val ioScheduler: Scheduler,
    @QualifiedScheduler(MAIN_THREAD) private val mainThreadScheduler: Scheduler,
) {

    fun cached(): Single<List<Show>> = db.showDao().getAll()
        .subscribeOn(ioScheduler)
        .observeOn(mainThreadScheduler)
        .map { entity ->
            entity.map(::mapShow)
        }

    fun firstPage(): Single<List<Show>> = service.shows()
        .subscribeOn(ioScheduler)
        .observeOn(mainThreadScheduler)
        .map { payload ->
            payload.mapNotNull(::mapShow)
        }
        .doOnSuccess { shows ->
            shows.map(::mapShow).onEach { showWithGenre ->
                db.showDao().let { dao ->
                    dao.insert(showWithGenre.show)
                        .subscribeOn(ioScheduler)
                        .subscribe({}, Timber::e)
                    dao.clearGenres(showWithGenre.show.id)
                        .subscribeOn(ioScheduler)
                        .andThen {
                            showWithGenre.genres.onEach {
                                dao.insert(it)
                                    .subscribeOn(ioScheduler)
                                    .subscribe({}, Timber::e)
                            }
                        }
                        .subscribe({}, Timber::e)
                }
            }
        }

    private fun mapShow(item: ShowItem): Show? {
        val id = item.id ?: return null
        val name = item.name ?: return null
        val image = item.image?.original ?: item.image?.medium ?: return null
        val language = item.language ?: return null
        val genres = item.genres?.mapNotNull { Genre.fromKeyword(it) }?.sorted() ?: emptyList()
        val rating = item.rating?.average ?: return null
        return Show(
            id = id,
            name = name,
            image = image,
            language = Language.fromKeyword(language),
            genres = genres,
            rating = rating,
        )
    }

    private fun mapShow(show: Show): ShowWithGenre {
        return ShowWithGenre(
            show = ShowEntity(
                id = show.id,
                name = show.name,
                image = show.image,
                language = show.language,
                rating = show.rating,
            ),
            genres = show.genres.map {
                GenreEntity(
                    showId = show.id,
                    genre = it,
                )
            },
        )
    }

    private fun mapShow(showWithGenre: ShowWithGenre): Show {
        return Show(
            id = showWithGenre.show.id,
            name = showWithGenre.show.name,
            image = showWithGenre.show.image,
            language = showWithGenre.show.language,
            genres = showWithGenre.genres.map { it.genre }.sorted(),
            rating = showWithGenre.show.rating,
        )
    }

}
