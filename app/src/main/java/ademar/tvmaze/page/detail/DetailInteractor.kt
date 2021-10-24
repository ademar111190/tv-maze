package ademar.tvmaze.page.detail

import ademar.tvmaze.arch.ArchInteractor
import ademar.tvmaze.data.Show
import ademar.tvmaze.di.qualifiers.QualifiedScheduler
import ademar.tvmaze.di.qualifiers.QualifiedSchedulerOption.COMPUTATION
import ademar.tvmaze.di.qualifiers.QualifiedSchedulerOption.MAIN_THREAD
import ademar.tvmaze.ext.valueOrError
import ademar.tvmaze.page.detail.Contract.Command
import ademar.tvmaze.page.detail.Contract.EpisodesStatus.*
import ademar.tvmaze.page.detail.Contract.State
import ademar.tvmaze.page.seasons.SeasonsNavigator
import ademar.tvmaze.usecase.*
import dagger.hilt.android.scopes.ActivityScoped
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Observable.empty
import io.reactivex.rxjava3.core.Observable.just
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.subjects.BehaviorSubject.create
import timber.log.Timber
import javax.inject.Inject

@ActivityScoped
class DetailInteractor @Inject constructor(
    private val fetchShow: FetchShow,
    private val fetchSeasons: FetchSeasons,
    private val fetchEpisodes: FetchEpisodes,
    private val favoriteCheck: FavoriteCheck,
    private val favoriteToggler: FavoriteToggler,
    private val seasonsNavigator: SeasonsNavigator,
    private val subscriptions: CompositeDisposable,
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
        is Command.EpisodesClick -> episodesClick()
        is Command.None -> empty()
        is Command.Favorite -> favorite()
        is Command.UnFavorite -> unFavorite()
    }

    private fun initial(id: Long?): Observable<State> {
        if (id == null) return just(State.InvalidIdState)
        return fetchShow.byId(id)
            .flatMap<State> { show ->
                Single.zip(
                    fetchSeasons.isCached(show),
                    favoriteCheck.isFavorite(show.id),
                    { cached, favorite ->
                        Pair(cached, favorite)
                    }
                ).map { (cached, favorite) ->
                    fetchSeasons(show)
                    State.DataState(
                        show = show,
                        favorite = favorite,
                        episodesStatus = if (cached) FETCHED else FETCHING,
                    )
                }
            }
            .toObservable()
            .onErrorResumeNext(::mapError)
    }

    private fun episodesClick(): Observable<State> {
        return output.valueOrError()
            .map { state ->
                if (state is State.DataState) {
                    if (state.episodesStatus == FETCHED) {
                        seasonsNavigator.openSeason(state.show.id)
                        state
                    } else {
                        fetchSeasons(state.show)
                        state.copy(episodesStatus = FETCHING)
                    }
                } else {
                    state
                }
            }
            .toObservable()
    }

    private fun favorite(): Observable<State> {
        return output.valueOrError()
            .toObservable()
            .map { state ->
                if (state is State.DataState) {
                    subscriptions.add(
                        favoriteToggler.favorite(state.show.id)
                            .subscribe({}, Timber::e)
                    )
                    state.copy(favorite = true)
                } else {
                    state
                }
            }
            .onErrorResumeNext(::mapError)
    }

    private fun unFavorite(): Observable<State> {
        return output.valueOrError()
            .toObservable()
            .map { state ->
                if (state is State.DataState) {
                    subscriptions.add(
                        favoriteToggler.unfavorite(state.show.id)
                            .subscribe({}, Timber::e)
                    )
                    state.copy(favorite = false)
                } else {
                    state
                }
            }
            .onErrorResumeNext(::mapError)
    }

    private fun fetchSeasons(show: Show) {
        subscriptions.add(
            fetchSeasons.newest(show)
                .flattenAsObservable { it }
                .flatMap { season ->
                    fetchEpisodes.newest(season)
                        .flattenAsObservable { it }
                }
                .subscribe({}, {
                    fetchSeasonsDone(false)
                }, {
                    fetchSeasonsDone(true)
                })
        )
    }

    private fun fetchSeasonsDone(success: Boolean) {
        subscriptions.add(
            output.valueOrError()
                .subscribe({ state ->
                    if (state is State.DataState) {
                        output.onNext(
                            state.copy(
                                episodesStatus = if (success) FETCHED else ERROR,
                            )
                        )
                    }
                }, Timber::e)
        )
    }

}
