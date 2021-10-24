package ademar.tvmaze.page.favorite

import ademar.tvmaze.R
import ademar.tvmaze.arch.ArchBinder
import ademar.tvmaze.page.favorite.Contract.Command
import ademar.tvmaze.page.favorite.Contract.Model
import ademar.tvmaze.tile.SeriesTileCallback
import ademar.tvmaze.widget.EdgeCaseContent.showContent
import ademar.tvmaze.widget.EdgeCaseContent.showError
import ademar.tvmaze.widget.EdgeCaseContent.showLoad
import ademar.tvmaze.widget.Reselectable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.subjects.BehaviorSubject.create
import io.reactivex.rxjava3.subjects.Subject
import javax.inject.Inject

@AndroidEntryPoint
class FavoriteFragment : Fragment(), Reselectable, Contract.View {

    @Inject override lateinit var subscriptions: CompositeDisposable
    @Inject lateinit var presenter: FavoritePresenter
    @Inject lateinit var interactor: FavoriteInteractor
    @Inject lateinit var archBinder: ArchBinder

    override val output: Subject<Command> = create()

    private val adapter = FavoriteAdapter(object : SeriesTileCallback {
        override fun onItemClick(id: Long) {
            output.onNext(Command.SeriesSelected(id))
        }
    })

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.page_favorite, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val list = view.findViewById<RecyclerView>(R.id.list)
        list.adapter = adapter

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
        val edgeCaseContent = view.findViewById<View>(R.id.edge_case_content)
        val content = view.findViewById<RecyclerView>(R.id.list)

        when (model) {
            is Model.Loading -> showLoad(edgeCaseContent, content)
            is Model.Error -> showError(edgeCaseContent, content, model.message)
            is Model.Empty -> showError(edgeCaseContent, content, model.message)
            is Model.DataModel -> {
                showContent(edgeCaseContent, content)
                adapter.setItems(model.series)
            }
        }
    }

}
