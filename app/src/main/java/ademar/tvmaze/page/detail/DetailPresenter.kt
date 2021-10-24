package ademar.tvmaze.page.detail

import ademar.tvmaze.R
import ademar.tvmaze.arch.ArchPresenter
import ademar.tvmaze.data.ScheduleDay
import ademar.tvmaze.data.ScheduleDay.*
import ademar.tvmaze.data.Show
import ademar.tvmaze.di.qualifiers.QualifiedScheduler
import ademar.tvmaze.di.qualifiers.QualifiedSchedulerOption.COMPUTATION
import ademar.tvmaze.di.qualifiers.QualifiedSchedulerOption.MAIN_THREAD
import ademar.tvmaze.page.detail.Contract.Episodes.Loaded
import ademar.tvmaze.page.detail.Contract.Episodes.Loading
import ademar.tvmaze.page.detail.Contract.EpisodesStatus.*
import ademar.tvmaze.page.detail.Contract.Model
import ademar.tvmaze.page.detail.Contract.State
import android.content.Context
import androidx.annotation.DrawableRes
import androidx.core.text.HtmlCompat
import androidx.core.text.HtmlCompat.FROM_HTML_MODE_COMPACT
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ActivityScoped
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.subjects.BehaviorSubject.createDefault
import javax.inject.Inject

@ActivityScoped
class DetailPresenter @Inject constructor(
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
                message = context.getString(R.string.detail_no_show),
            )
            is State.ErrorState -> Model.Error(
                state.message,
            )
            is State.DataState -> {
                Model.ShowModel(
                    show = state.show,
                    summary = HtmlCompat.fromHtml(state.show.summary, FROM_HTML_MODE_COMPACT),
                    monday = dayIcon(MONDAY, state.show),
                    tuesday = dayIcon(TUESDAY, state.show),
                    wednesday = dayIcon(WEDNESDAY, state.show),
                    thursday = dayIcon(THURSDAY, state.show),
                    friday = dayIcon(FRIDAY, state.show),
                    saturday = dayIcon(SATURDAY, state.show),
                    sunday = dayIcon(SUNDAY, state.show),
                    episodes = when (state.episodesStatus) {
                        FETCHING -> Loading
                        FETCHED -> Loaded(context.getString(R.string.detail_episodes_button))
                        ERROR -> Loaded(context.getString(R.string.detail_episodes_button_retry))
                    },
                )
            }
        }
    }

    @DrawableRes
    private fun dayIcon(scheduleDay: ScheduleDay, show: Show): Int {
        return if (scheduleDay in show.days) {
            R.drawable.bg_tile_schedule_day_on
        } else {
            R.drawable.bg_tile_schedule_day_off
        }
    }

}
