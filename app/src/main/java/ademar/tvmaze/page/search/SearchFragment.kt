package ademar.tvmaze.page.search

import ademar.tvmaze.R
import ademar.tvmaze.widget.AfterTextChanged
import ademar.tvmaze.widget.Reselectable
import android.content.Intent
import android.os.Bundle
import android.speech.RecognizerIntent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.subjects.PublishSubject.create
import io.reactivex.rxjava3.subjects.Subject
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@AndroidEntryPoint
class SearchFragment : Fragment(), Reselectable {

    @Inject lateinit var subscriptions: CompositeDisposable

    private val termEmitter: Subject<String> = create()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.page_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val searchField = view.findViewById<EditText>(R.id.search_field)
        val toolbar = view.findViewById<Toolbar>(R.id.toolbar)

        searchField.addTextChangedListener(AfterTextChanged(::onSearchChanged))

        toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.action_search_clear -> {
                    searchField.setText("")
                }
                R.id.action_search_voice -> {
                    val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
                    intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
                    intent.putExtra(RecognizerIntent.EXTRA_PROMPT, getString(R.string.search_menu_voice))
                    startActivityForResult(intent, 1)
                }
                else -> null
            } != null
        }
    }

    override fun onResume() {
        super.onResume()
        subscriptions.add(
            termEmitter
                .throttleWithTimeout(1000, TimeUnit.MILLISECONDS)
                .subscribe({
                    Timber.d("TODO search $it")
                }, Timber::e)
        )
        onSearchChanged(view?.findViewById<EditText>(R.id.search_field)?.text?.toString() ?: "")
    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?,
    ) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1 && resultCode == AppCompatActivity.RESULT_OK && data != null) {
            val term = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)?.firstOrNull()
            if (term != null) {
                view?.findViewById<EditText>(R.id.search_field)?.setText(term)
            }
        }
    }

    override fun onReselected() {
        view?.findViewById<RecyclerView>(R.id.list)?.scrollToPosition(0)
    }

    private fun onSearchChanged(term: String) {
        val view = view ?: return
        val toolbar = view.findViewById<Toolbar>(R.id.toolbar)
        val clear = toolbar.menu.findItem(R.id.action_search_clear)
        val voice = toolbar.menu.findItem(R.id.action_search_voice)
        if (term.isEmpty()) {
            clear.isVisible = false
            voice.isVisible = true
        } else {
            clear.isVisible = true
            voice.isVisible = false
        }
        termEmitter.onNext(term)
    }

}
