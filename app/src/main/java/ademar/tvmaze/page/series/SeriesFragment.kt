package ademar.tvmaze.page.series

import ademar.tvmaze.R
import ademar.tvmaze.arch.ArchBinder
import ademar.tvmaze.page.series.Contract.Command
import ademar.tvmaze.page.series.Contract.Model
import ademar.tvmaze.widget.Reselectable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.subjects.BehaviorSubject.create
import io.reactivex.rxjava3.subjects.Subject
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class SeriesFragment : Fragment(), Reselectable, Contract.View {

    @Inject override lateinit var subscriptions: CompositeDisposable
    @Inject lateinit var presenter: SeriesPresenter
    @Inject lateinit var interactor: SeriesInteractor
    @Inject lateinit var archBinder: ArchBinder

    override val output: Subject<Command> = create()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.page_series, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        archBinder.bind(this, interactor, presenter)
    }

    override fun onResume() {
        super.onResume()
        output.onNext(Command.Initial)
    }

    override fun onReselected() {
        Timber.d("onReselected") // TODO
    }

    override fun render(model: Model) {
        val view = view ?: return
        view.findViewById<TextView>(R.id.txt).text = "$model"
    }

}
