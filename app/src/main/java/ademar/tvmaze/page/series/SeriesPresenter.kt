package ademar.tvmaze.page.series

import ademar.tvmaze.arch.ArchPresenter
import ademar.tvmaze.di.qualifiers.QualifiedScheduler
import ademar.tvmaze.di.qualifiers.QualifiedSchedulerOption.COMPUTATION
import ademar.tvmaze.di.qualifiers.QualifiedSchedulerOption.MAIN_THREAD
import ademar.tvmaze.page.series.Contract.Model
import ademar.tvmaze.page.series.Contract.State
import dagger.hilt.android.scopes.FragmentScoped
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.subjects.BehaviorSubject.create
import javax.inject.Inject

@FragmentScoped
class SeriesPresenter @Inject constructor(
    subscriptions: CompositeDisposable,
    @QualifiedScheduler(COMPUTATION) private val computationScheduler: Scheduler,
    @QualifiedScheduler(MAIN_THREAD) private val mainThreadScheduler: Scheduler,
) : ArchPresenter<State, Model>(
    errorFactory = Model::Error,
    subscriptions = subscriptions,
    backgroundScheduler = computationScheduler,
    foregroundScheduler = mainThreadScheduler,
    output = create(),
) {

    override fun map(state: State): Model {
        return when (state) {
            is State.DataState -> Model.DataModel(
                series = state.series,
            )
            is State.ErrorState -> Model.Error(
                state.message,
            )
        }
    }

}
