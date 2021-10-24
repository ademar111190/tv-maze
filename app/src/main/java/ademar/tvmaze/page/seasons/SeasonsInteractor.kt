package ademar.tvmaze.page.seasons

import ademar.tvmaze.arch.ArchInteractor
import ademar.tvmaze.data.Episode
import ademar.tvmaze.data.Season
import ademar.tvmaze.di.qualifiers.QualifiedScheduler
import ademar.tvmaze.di.qualifiers.QualifiedSchedulerOption.COMPUTATION
import ademar.tvmaze.di.qualifiers.QualifiedSchedulerOption.MAIN_THREAD
import ademar.tvmaze.page.episode.EpisodeNavigator
import ademar.tvmaze.page.seasons.Contract.Command
import ademar.tvmaze.page.seasons.Contract.State
import ademar.tvmaze.usecase.FetchEpisodes
import ademar.tvmaze.usecase.FetchSeasons
import ademar.tvmaze.usecase.FetchShow
import dagger.hilt.android.scopes.ActivityScoped
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Observable.empty
import io.reactivex.rxjava3.core.Observable.just
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.subjects.BehaviorSubject.create
import javax.inject.Inject

@ActivityScoped
class SeasonsInteractor @Inject constructor(
    private val fetchShow: FetchShow,
    private val fetchSeasons: FetchSeasons,
    private val fetchEpisodes: FetchEpisodes,
    private val episodeNavigator: EpisodeNavigator,
    subscriptions: CompositeDisposable,
    @QualifiedScheduler(COMPUTATION) private val computationScheduler: Scheduler,
    @QualifiedScheduler(MAIN_THREAD) private val mainThreadScheduler: Scheduler,
) : ArchInteractor<Command, State>(
    errorFactory = State::ErrorState,
    subscriptions = subscriptions,
    backgroundScheduler = computationScheduler,
    foregroundScheduler = mainThreadScheduler,
    output = create(),
) {

    override fun map(
        command: Command,
    ): Observable<State> = when (command) {
        is Command.Initial -> initial(command.id)
        is Command.EpisodeClick -> episodesClick(command.id)
        is Command.None -> empty()
    }

    private fun initial(id: Long?): Observable<State> {
        if (id == null) return just(State.InvalidIdState)
        return fetchShow.byId(id)
            .flatMap { show ->
                fetchSeasons.cached(show)
                    .flattenAsObservable { it }
                    .flatMapSingle { season ->
                        fetchEpisodes.cached(season)
                            .map { it.toSet() }
                            .map { episodes ->
                                season to episodes
                            }
                    }
                    .collectInto(mutableMapOf<Season, Set<Episode>>()) { map, pair ->
                        map[pair.first] = pair.second
                    }
                    .map<State> { seasons ->
                        State.DataState(
                            show = show,
                            seasons = seasons,
                        )
                    }
            }
            .toObservable()
            .onErrorResumeNext(::mapError)
    }

    private fun episodesClick(episodeId: Long): Observable<State> {
        episodeNavigator.openEpisode(episodeId)
        return empty()
    }

}
