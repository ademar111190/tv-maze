package ademar.tvmaze.page.series

import ademar.tvmaze.arch.ArchInteractor
import ademar.tvmaze.data.Show
import ademar.tvmaze.di.qualifiers.QualifiedScheduler
import ademar.tvmaze.di.qualifiers.QualifiedSchedulerOption.COMPUTATION
import ademar.tvmaze.di.qualifiers.QualifiedSchedulerOption.MAIN_THREAD
import ademar.tvmaze.ext.valueOrError
import ademar.tvmaze.page.series.Contract.Command
import ademar.tvmaze.page.series.Contract.State
import ademar.tvmaze.usecase.FetchShows
import dagger.hilt.android.scopes.FragmentScoped
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Observable.empty
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.subjects.BehaviorSubject.create
import io.reactivex.rxjava3.subjects.BehaviorSubject.just
import javax.inject.Inject

@FragmentScoped
class SeriesInteractor @Inject constructor(
    private val fetchShows: FetchShows,
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
        is Command.Initial -> initial()
        is Command.NextPage -> nextPage()
    }

    private var currentPage = 0
        @Synchronized get
        @Synchronized set

    private var waitingNextPage = false
        @Synchronized get
        @Synchronized set

    private fun initial(): Observable<State> {
        currentPage = 0
        waitingNextPage = true
        return fetchShows.cached()
            .map { shows ->
                State.DataState(
                    series = shows,
                    hasNextPage = false,
                )
            }
            .map(::sortedShows)
            .doOnSuccess { state ->
                if (state is State.DataState && state.series.isNotEmpty()) {
                    output.onNext(state)
                }
            }
            .flatMapObservable { state ->
                var observer = fetchShows.firstPage()
                    .flatMapObservable<State> { shows ->
                        just(
                            State.DataState(
                                series = mergeSeries(shows, state),
                                hasNextPage = true,
                            )
                        )
                    }
                    .map(::sortedShows)
                observer = if (state is State.DataState && state.series.isNotEmpty()) {
                    observer.onErrorReturn { state.copy(hasNextPage = false) }
                } else {
                    observer.onErrorResumeNext(::mapError)
                }
                observer
            }
            .doOnNext {
                waitingNextPage = false
            }
    }

    private fun nextPage(): Observable<State> {
        if (waitingNextPage) return empty()

        waitingNextPage = true
        currentPage++

        return fetchShows.numberedPage(currentPage)
            .map { shows ->
                State.DataState(
                    series = shows,
                    hasNextPage = true,
                )
            }
            .flatMap<State> { newState ->
                output.valueOrError()
                    .map { lastState ->
                        newState.copy(
                            series = mergeSeries(newState.series, lastState),
                        )
                    }
                    .onErrorReturn {
                        newState.copy(
                            hasNextPage = false,
                        )
                    }
            }
            .toObservable()
            .onErrorResumeNext { error ->
                output.valueOrError()
                    .flatMapObservable { lastState ->
                        if (lastState is State.DataState) {
                            just(
                                lastState.copy(
                                    hasNextPage = false,
                                )
                            )
                        } else {
                            mapError(error)
                        }
                    }
                    .onErrorResumeNext(::mapError)
            }
            .map(::sortedShows)
            .doOnNext {
                waitingNextPage = false
            }
    }

    private fun mergeSeries(base: List<Show>, state: State): List<Show> {
        return if (state is State.DataState) {
            val mergedShows = base.toMutableList()
            val ids = base.map { it.id }.toSet()
            mergedShows.addAll(state.series.filter { !ids.contains(it.id) })
            mergedShows
        } else {
            base
        }
    }

    private fun sortedShows(state: State): State {
        return if (state is State.DataState) {
            val sortedList = when (state.sortOption) {
                Contract.SortOption.ID -> state.series.sortedBy { it.id }
                Contract.SortOption.NAME -> state.series.sortedBy { it.name }
            }
            state.copy(
                series = sortedList,
            )
        } else {
            state
        }
    }

}
