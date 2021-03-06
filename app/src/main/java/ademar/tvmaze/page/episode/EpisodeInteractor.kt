package ademar.tvmaze.page.episode

import ademar.tvmaze.arch.ArchInteractor
import ademar.tvmaze.di.qualifiers.QualifiedScheduler
import ademar.tvmaze.di.qualifiers.QualifiedSchedulerOption.COMPUTATION
import ademar.tvmaze.di.qualifiers.QualifiedSchedulerOption.MAIN_THREAD
import ademar.tvmaze.page.episode.Contract.Command
import ademar.tvmaze.page.episode.Contract.State
import ademar.tvmaze.usecase.FetchEpisode
import dagger.hilt.android.scopes.ActivityScoped
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Observable.just
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.subjects.BehaviorSubject.create
import javax.inject.Inject

@ActivityScoped
class EpisodeInteractor @Inject constructor(
    private val fetchEpisode: FetchEpisode,
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
    }

    private fun initial(id: Long?): Observable<State> {
        if (id == null) return just(State.InvalidIdState)
        return fetchEpisode.cached(id)
            .map<State>(State::DataState)
            .toObservable()
            .onErrorResumeNext(::mapError)
    }

}
