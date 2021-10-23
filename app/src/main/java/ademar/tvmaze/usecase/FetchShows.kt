package ademar.tvmaze.usecase

import ademar.tvmaze.data.Genre
import ademar.tvmaze.data.Language
import ademar.tvmaze.data.Show
import ademar.tvmaze.di.qualifiers.QualifiedScheduler
import ademar.tvmaze.di.qualifiers.QualifiedSchedulerOption.IO
import ademar.tvmaze.di.qualifiers.QualifiedSchedulerOption.MAIN_THREAD
import ademar.tvmaze.network.api.TvMazeService
import ademar.tvmaze.network.payload.ShowItem
import dagger.Reusable
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

@Reusable
class FetchShows @Inject constructor(
    private val service: TvMazeService,
    @QualifiedScheduler(IO) private val ioScheduler: Scheduler,
    @QualifiedScheduler(MAIN_THREAD) private val mainThreadScheduler: Scheduler,
) {

    fun firstPage(): Single<List<Show>> = service.shows()
        .subscribeOn(ioScheduler)
        .observeOn(mainThreadScheduler)
        .map { payload ->
            payload.mapNotNull(::mapShow)
        }

    private fun mapShow(item: ShowItem): Show? {
        val id = item.id ?: return null
        val name = item.name ?: return null
        val image = item.image?.original ?: item.image?.medium ?: return null
        val language = item.language ?: return null
        val genres = item.genres?.mapNotNull { Genre.fromKeyword(it) } ?: emptyList()
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

}
