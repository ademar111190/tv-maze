package ademar.tvmaze.page.detail

import ademar.tvmaze.R
import ademar.tvmaze.arch.ArchBinder
import ademar.tvmaze.page.detail.Contract.Command
import ademar.tvmaze.page.detail.Contract.Model
import ademar.tvmaze.widget.EdgeCaseContent.showContent
import ademar.tvmaze.widget.EdgeCaseContent.showError
import ademar.tvmaze.widget.EdgeCaseContent.showLoad
import android.os.Bundle
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.subjects.PublishSubject.create
import io.reactivex.rxjava3.subjects.Subject
import javax.inject.Inject

@AndroidEntryPoint
class DetailActivity : AppCompatActivity(), Contract.View {

    @Inject override lateinit var subscriptions: CompositeDisposable
    @Inject lateinit var presenter: DetailPresenter
    @Inject lateinit var interactor: DetailInteractor
    @Inject lateinit var archBinder: ArchBinder

    override val output: Subject<Command> = create()

    private val genreAdapter = GenreAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.page_detail)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        toolbar.setNavigationOnClickListener { finish() }

        val genreList = findViewById<RecyclerView>(R.id.genres_list)
        genreList.adapter = genreAdapter

        archBinder.bind(this, interactor, presenter)
    }

    override fun onResume() {
        super.onResume()
        output.onNext(Command.Initial(intent.extras?.getLong("SHOW_ID")))
    }

    override fun render(model: Model) {
        val edgeCaseContent = findViewById<View>(R.id.edge_case_content)
        val content = findViewById<View>(R.id.content)

        when (model) {
            is Model.Loading -> showLoad(edgeCaseContent, content)
            is Model.Error -> showError(edgeCaseContent, content, model.message)
            is Model.NoShow -> showError(edgeCaseContent, content, model.message)
            is Model.ShowModel -> {
                showContent(edgeCaseContent, content)
                renderShowModel(model)
            }
        }
    }

    private fun renderShowModel(model: Model.ShowModel) {
        val banner = findViewById<ImageView>(R.id.banner)
        val name = findViewById<TextView>(R.id.name)
        val summary = findViewById<TextView>(R.id.summary)
        val timeValue = findViewById<TextView>(R.id.time_value)
        val ratingValue = findViewById<TextView>(R.id.rating_value)
        val language = findViewById<ImageView>(R.id.language)
        val languageTitle = findViewById<TextView>(R.id.language_title)
        val episodesLoad = findViewById<ProgressBar>(R.id.episodes_load)
        val episodesButton = findViewById<Button>(R.id.episodes_button)

        name.text = model.show.name
        summary.text = model.summary
        timeValue.text = model.show.time
        ratingValue.text = model.show.rating.toString()
        language.setImageResource(model.show.language.icon)
        languageTitle.setText(model.show.language.title)

        genreAdapter.setItems(model.show.genres)
        Glide.with(this)
            .load(model.show.image)
            .placeholder(R.mipmap.ic_launcher_background)
            .transition(DrawableTransitionOptions.withCrossFade(200))
            .into(banner)

        for ((id, bg) in listOf(
            R.id.day_value_monday_bg to model.monday,
            R.id.day_value_tuesday_bg to model.tuesday,
            R.id.day_value_wednesday_bg to model.wednesday,
            R.id.day_value_thursday_bg to model.thursday,
            R.id.day_value_friday_bg to model.friday,
            R.id.day_value_saturday_bg to model.saturday,
            R.id.day_value_sunday_bg to model.sunday,
        )) {
            findViewById<FrameLayout>(id).setBackgroundResource(bg)
        }

        when (model.episodes) {
            is Contract.Episodes.Loading -> {
                episodesLoad.visibility = VISIBLE
                episodesButton.visibility = GONE
            }
            is Contract.Episodes.Available -> {
                episodesLoad.visibility = GONE
                episodesButton.visibility = VISIBLE
                episodesButton.text = model.episodes.callToAction
            }
        }
    }

}
