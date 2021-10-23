package ademar.tvmaze.page.series

import ademar.tvmaze.arch.ArchInteractor
import ademar.tvmaze.di.qualifiers.QualifiedScheduler
import ademar.tvmaze.di.qualifiers.QualifiedSchedulerOption.COMPUTATION
import ademar.tvmaze.di.qualifiers.QualifiedSchedulerOption.MAIN_THREAD
import ademar.tvmaze.page.series.Contract.Command
import ademar.tvmaze.page.series.Contract.State
import ademar.tvmaze.usecase.FetchShows
import dagger.hilt.android.scopes.FragmentScoped
import io.reactivex.rxjava3.core.Observable
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
        is Command.NextPage -> initial() // TODO next page
    }

    private fun initial(): Observable<State> {
        return fetchShows.cached()
            .doOnSuccess { shows ->
                if (shows.isNotEmpty()) {
                    output.onNext(
                        State.DataState(
                            series = shows,
                        )
                    )
                }
            }
            .flatMapObservable {
                fetchShows.firstPage()
                    .flatMapObservable<State> { shows ->
                        just(
                            State.DataState(
                                series = shows,
                            )
                        )
                    }
                    .onErrorResumeNext(::mapError)
            }
    }

}
