package ademar.tvmaze.page.seasons

import ademar.tvmaze.R
import ademar.tvmaze.arch.ArchBinder
import ademar.tvmaze.page.seasons.Contract.Command
import ademar.tvmaze.page.seasons.Contract.Model
import ademar.tvmaze.widget.EdgeCaseContent.showContent
import ademar.tvmaze.widget.EdgeCaseContent.showError
import ademar.tvmaze.widget.EdgeCaseContent.showLoad
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.subjects.BehaviorSubject.create
import io.reactivex.rxjava3.subjects.Subject
import javax.inject.Inject

@AndroidEntryPoint
class SeasonsActivity : AppCompatActivity(), Contract.View {

    @Inject override lateinit var subscriptions: CompositeDisposable
    @Inject lateinit var presenter: SeasonsPresenter
    @Inject lateinit var interactor: SeasonsInteractor
    @Inject lateinit var archBinder: ArchBinder

    override val output: Subject<Command> = create()

    private val seasonAdapter = SeasonAdapter(object : SeasonsEpisodeCallback {
        override fun onEpisodeClick(id: Long) {
            output.onNext(Command.EpisodeClick(id))
            output.onNext(Command.None)
        }
    })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.page_seasons)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        toolbar.setNavigationOnClickListener { finish() }

        val list = findViewById<RecyclerView>(R.id.list)
        list.adapter = seasonAdapter

        archBinder.bind(this, interactor, presenter)
    }

    override fun onResume() {
        super.onResume()
        output.onNext(Command.Initial(intent.extras?.getLong("SHOW_ID")))
    }

    override fun render(model: Model) {
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        val edgeCaseContent = findViewById<View>(R.id.edge_case_content)
        val list = findViewById<RecyclerView>(R.id.list)

        when (model) {
            is Model.Loading -> showLoad(edgeCaseContent, list)
            is Model.Error -> showError(edgeCaseContent, list, model.message)
            is Model.DataModel -> {
                showContent(edgeCaseContent, list)
                toolbar.title = model.title
                seasonAdapter.setItems(model.items)
            }
        }
    }

}
