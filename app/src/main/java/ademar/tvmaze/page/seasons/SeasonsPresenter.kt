package ademar.tvmaze.page.seasons

import ademar.tvmaze.R
import ademar.tvmaze.arch.ArchPresenter
import ademar.tvmaze.di.qualifiers.QualifiedScheduler
import ademar.tvmaze.di.qualifiers.QualifiedSchedulerOption.COMPUTATION
import ademar.tvmaze.di.qualifiers.QualifiedSchedulerOption.MAIN_THREAD
import ademar.tvmaze.page.seasons.Contract.Item.Episode
import ademar.tvmaze.page.seasons.Contract.Item.SeasonHeader
import ademar.tvmaze.page.seasons.Contract.Model
import ademar.tvmaze.page.seasons.Contract.State
import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ActivityScoped
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.subjects.BehaviorSubject.createDefault
import javax.inject.Inject

@ActivityScoped
class SeasonsPresenter @Inject constructor(
    @ApplicationContext private val context: Context,
    subscriptions: CompositeDisposable,
    @QualifiedScheduler(COMPUTATION) private val computationScheduler: Scheduler,
    @QualifiedScheduler(MAIN_THREAD) private val mainThreadScheduler: Scheduler,
) : ArchPresenter<State, Model>(
    errorFactory = Model::Error,
    subscriptions = subscriptions,
    backgroundScheduler = computationScheduler,
    foregroundScheduler = mainThreadScheduler,
    output = createDefault(Model.Loading),
) {

    override fun map(state: State): Model {
        return when (state) {
            is State.InvalidIdState -> Model.Error(
                message = context.getString(R.string.seasons_page_fail_to_load),
            )
            is State.ErrorState -> Model.Error(
                state.message,
            )
            is State.DataState -> {
                Model.DataModel(
                    title = context.getString(R.string.seasons_page_title, state.show.name),
                    items = state.seasons
                        .toList()
                        .sortedBy { (season, _) ->
                            season.number
                        }
                        .map { (season, episodes) ->
                            SeasonHeader(
                                context.getString(
                                    R.string.seasons_page_header,
                                    season.number,
                                    season.name,
                                )
                            ) to episodes
                                .toList()
                                .sortedBy { it.number }
                                .map { episode ->
                                    Episode(
                                        episode.id,
                                        context.getString(
                                            R.string.seasons_page_episode,
                                            episode.number,
                                            episode.name,
                                        ),
                                    )
                                }
                        }
                        .map { (season, episodes) ->
                            listOf(listOf(season), episodes).flatten()
                        }
                        .flatten(),
                )
            }
        }
    }

}
