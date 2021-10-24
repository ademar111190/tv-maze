package ademar.tvmaze.page.episode

import ademar.tvmaze.R
import ademar.tvmaze.arch.ArchBinder
import ademar.tvmaze.page.episode.Contract.Command
import ademar.tvmaze.page.episode.Contract.Model
import ademar.tvmaze.widget.EdgeCaseContent.showContent
import ademar.tvmaze.widget.EdgeCaseContent.showError
import ademar.tvmaze.widget.EdgeCaseContent.showLoad
import android.os.Bundle
import android.view.View
import android.view.View.*
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.ViewCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.subjects.BehaviorSubject.create
import io.reactivex.rxjava3.subjects.Subject
import javax.inject.Inject

@AndroidEntryPoint
class EpisodeActivity : AppCompatActivity(), Contract.View {

    @Inject override lateinit var subscriptions: CompositeDisposable
    @Inject lateinit var presenter: EpisodePresenter
    @Inject lateinit var interactor: EpisodeInteractor
    @Inject lateinit var archBinder: ArchBinder

    override val output: Subject<Command> = create()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.page_episode)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        toolbar.setNavigationOnClickListener { finish() }
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.container)) { _, insets ->
            val lp = toolbar.layoutParams as CoordinatorLayout.LayoutParams
            lp.setMargins(0, insets.systemWindowInsetTop, 0, 0)
            toolbar.layoutParams = lp

            val navBarProtection = findViewById<View>(R.id.nav_bar_protection)
            val nlp = navBarProtection.layoutParams as ConstraintLayout.LayoutParams
            nlp.height = insets.systemWindowInsetBottom
            navBarProtection.layoutParams = nlp

            insets.consumeSystemWindowInsets()
        }

        findViewById<CoordinatorLayout>(R.id.container).systemUiVisibility =
            SYSTEM_UI_FLAG_LAYOUT_STABLE or SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION

        archBinder.bind(this, interactor, presenter)
    }

    override fun onResume() {
        super.onResume()
        output.onNext(Command.Initial(intent.extras?.getLong("EPISODE_ID")))
    }

    override fun render(model: Model) {
        val edgeCaseContent = findViewById<View>(R.id.edge_case_content)
        val content = findViewById<View>(R.id.content)

        when (model) {
            is Model.Loading -> showLoad(edgeCaseContent, content)
            is Model.Error -> showError(edgeCaseContent, content, model.message)
            is Model.NoShow -> showError(edgeCaseContent, content, model.message)
            is Model.EpisodeModel -> {
                showContent(edgeCaseContent, content)
                renderShowModel(model)
            }
        }
    }

    private fun renderShowModel(model: Model.EpisodeModel) {
        val banner = findViewById<ImageView>(R.id.banner)
        val name = findViewById<TextView>(R.id.name)
        val summary = findViewById<TextView>(R.id.summary)
        val seasonValue = findViewById<TextView>(R.id.season_value)
        val numberValue = findViewById<TextView>(R.id.number_value)

        name.text = model.episode.name
        summary.text = model.summary
        seasonValue.text = model.episode.season.toString()
        numberValue.text = model.episode.number.toString()

        Glide.with(this)
            .load(model.episode.image ?: "")
            .placeholder(R.mipmap.ic_launcher_background)
            .transition(DrawableTransitionOptions.withCrossFade(200))
            .into(banner)
    }

}
