package ademar.tvmaze.page.series

import ademar.tvmaze.R
import ademar.tvmaze.network.api.TvMazeService
import ademar.tvmaze.widget.Reselectable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class SeriesFragment : Fragment(), Reselectable {

    @Inject lateinit var service: TvMazeService

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.page_series, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        service.shows()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                view.findViewById<TextView>(R.id.txt).text = "$it"
            }, Timber::e)
    }

    override fun onReselected() {
        Timber.d("onReselected") // TODO
    }

}
