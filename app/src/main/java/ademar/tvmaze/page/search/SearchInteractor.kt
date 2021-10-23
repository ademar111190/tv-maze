package ademar.tvmaze.page.search

import ademar.tvmaze.arch.ArchInteractor
import ademar.tvmaze.di.qualifiers.QualifiedScheduler
import ademar.tvmaze.di.qualifiers.QualifiedSchedulerOption.COMPUTATION
import ademar.tvmaze.di.qualifiers.QualifiedSchedulerOption.MAIN_THREAD
import ademar.tvmaze.page.detail.DetailNavigator
import ademar.tvmaze.page.search.Contract.Command
import ademar.tvmaze.page.search.Contract.State
import ademar.tvmaze.usecase.SearchShows
import dagger.hilt.android.scopes.ActivityScoped
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Observable.just
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.subjects.BehaviorSubject.create
import javax.inject.Inject

@ActivityScoped
class SearchInteractor @Inject constructor(
    private val searchShows: SearchShows,
    private val detailNavigator: DetailNavigator,
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
        is Command.Search -> search(command.query)
        is Command.SeriesSelected -> seriesSelected(command.id)
    }

    private fun initial(): Observable<State> {
        return just(State.NoSearch)
    }

    private fun search(
        query: String,
    ): Observable<State> {
        if (query.isEmpty()) return just(State.NoSearch)

        output.onNext(State.Searching)
        return searchShows.search(query)
            .map<State> { shows ->
                State.SearchResult(
                    series = shows,
                )
            }
            .toObservable()
            .onErrorResumeNext(::mapError)
    }

    private fun seriesSelected(id: Long): Observable<State> {
        detailNavigator.openDetail(id)
        return Observable.empty()
    }

}
