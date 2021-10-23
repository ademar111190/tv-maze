package ademar.tvmaze.page.search

import ademar.tvmaze.R
import ademar.tvmaze.arch.ArchPresenter
import ademar.tvmaze.di.qualifiers.QualifiedScheduler
import ademar.tvmaze.di.qualifiers.QualifiedSchedulerOption.COMPUTATION
import ademar.tvmaze.di.qualifiers.QualifiedSchedulerOption.MAIN_THREAD
import ademar.tvmaze.page.search.Contract.Model
import ademar.tvmaze.page.search.Contract.State
import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ActivityScoped
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.subjects.BehaviorSubject.createDefault
import javax.inject.Inject

@ActivityScoped
class SearchPresenter @Inject constructor(
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
            is State.NoSearch -> Model.Empty(
                context.getString(R.string.search_empty),
            )
            is State.Searching -> Model.Loading
            is State.ErrorState -> Model.Error(
                state.message,
            )
            is State.SearchResult -> {
                if (state.series.isEmpty()) {
                    Model.Empty(
                        context.getString(R.string.search_not_found),
                    )
                } else {
                    Model.DataModel(state.series)
                }
            }
        }
    }

}
