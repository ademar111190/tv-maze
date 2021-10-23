package ademar.tvmaze.page.series

import ademar.tvmaze.R
import ademar.tvmaze.arch.ArchBinder
import ademar.tvmaze.page.series.Contract.Command
import ademar.tvmaze.page.series.Contract.Model
import ademar.tvmaze.widget.EdgeCaseContent.showContent
import ademar.tvmaze.widget.EdgeCaseContent.showError
import ademar.tvmaze.widget.EdgeCaseContent.showLoad
import ademar.tvmaze.widget.Reselectable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.subjects.BehaviorSubject.create
import io.reactivex.rxjava3.subjects.Subject
import javax.inject.Inject

@AndroidEntryPoint
class SeriesFragment : Fragment(), Reselectable, Contract.View {

    @Inject override lateinit var subscriptions: CompositeDisposable
    @Inject lateinit var presenter: SeriesPresenter
    @Inject lateinit var interactor: SeriesInteractor
    @Inject lateinit var archBinder: ArchBinder

    private val adapter = SeriesAdapter()

    override val output: Subject<Command> = create()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.page_series, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<RecyclerView>(R.id.list).adapter = adapter
        archBinder.bind(this, interactor, presenter)
    }

    override fun onResume() {
        super.onResume()
        output.onNext(Command.Initial)
    }

    override fun onReselected() {
        view?.findViewById<RecyclerView>(R.id.list)?.scrollToPosition(0)
    }

    override fun render(model: Model) {
        val view = view ?: return
        val toolbar = view.findViewById<Toolbar>(R.id.toolbar)
        val edgeCaseContent = view.findViewById<View>(R.id.edge_case_content)
        val content = view.findViewById<RecyclerView>(R.id.list)

        when (model) {
            is Model.LoadModel -> showLoad(edgeCaseContent, content)
            is Model.Error -> {
                showError(edgeCaseContent, content, model.message)
                toolbar.title = model.title
            }
            is Model.DataModel -> {
                showContent(edgeCaseContent, content)
                adapter.setItems(model.series)
                toolbar.title = model.title
            }
        }
    }

}
