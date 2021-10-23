package ademar.tvmaze.page.search

import ademar.tvmaze.R
import ademar.tvmaze.widget.Reselectable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchFragment : Fragment(), Reselectable {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.page_search, container, false)
    }

    override fun onReselected() {
        view?.findViewById<RecyclerView>(R.id.list)?.scrollToPosition(0)
    }

}
