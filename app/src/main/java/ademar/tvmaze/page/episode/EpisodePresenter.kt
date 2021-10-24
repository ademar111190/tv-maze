package ademar.tvmaze.page.episode

import ademar.tvmaze.R
import ademar.tvmaze.arch.ArchPresenter
import ademar.tvmaze.di.qualifiers.QualifiedScheduler
import ademar.tvmaze.di.qualifiers.QualifiedSchedulerOption.COMPUTATION
import ademar.tvmaze.di.qualifiers.QualifiedSchedulerOption.MAIN_THREAD
import ademar.tvmaze.page.episode.Contract.Model
import ademar.tvmaze.page.episode.Contract.State
import android.content.Context
import androidx.core.text.HtmlCompat
import androidx.core.text.HtmlCompat.FROM_HTML_MODE_COMPACT
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ActivityScoped
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.subjects.BehaviorSubject.createDefault
import javax.inject.Inject

@ActivityScoped
class EpisodePresenter @Inject constructor(
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
            is State.InvalidIdState -> Model.NoShow(
                message = context.getString(R.string.episode_no_data),
            )
            is State.ErrorState -> Model.Error(
                state.message,
            )
            is State.DataState -> {
                Model.EpisodeModel(
                    episode = state.episode,
                    summary = HtmlCompat.fromHtml(state.episode.summary, FROM_HTML_MODE_COMPACT),
                )
            }
        }
    }

}
