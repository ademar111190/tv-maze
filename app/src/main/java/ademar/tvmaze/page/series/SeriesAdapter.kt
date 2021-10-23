package ademar.tvmaze.page.series

import ademar.tvmaze.R
import ademar.tvmaze.data.Show
import ademar.tvmaze.page.series.SeriesViewHolder.LoadViewHolder
import ademar.tvmaze.page.series.SeriesViewHolder.SeriesItemViewHolder
import ademar.tvmaze.tile.SeriesTileCallback
import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class SeriesAdapter(
    private val loadReachCallback: () -> Unit,
    private val callback: SeriesTileCallback,
) : RecyclerView.Adapter<SeriesViewHolder>() {

    private val viewTypeItem = 1
    private val viewTypeLoad = 2
    private val data = mutableListOf<Show>()
    private var addNextLoad = false

    init {
        setHasStableIds(true)
    }

    override fun getItemViewType(position: Int): Int {
        return if (position >= data.size) viewTypeLoad else viewTypeItem
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SeriesViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return if (viewType == viewTypeItem) {
            SeriesItemViewHolder(inflater.inflate(R.layout.item_series, parent, false), callback)
        } else {
            LoadViewHolder(inflater.inflate(R.layout.item_load, parent, false))
        }
    }

    override fun onBindViewHolder(holder: SeriesViewHolder, position: Int) {
        if (holder is SeriesItemViewHolder) {
            holder.bind(data[position])
        } else {
            loadReachCallback()
        }
    }

    override fun getItemCount(): Int = data.size + if (addNextLoad) 1 else 0

    override fun getItemId(position: Int): Long = if (position >= data.size) -1 else data[position].id

    @SuppressLint("NotifyDataSetChanged")
    fun setItems(items: List<Show>, addNextLoad: Boolean) {
        data.clear()
        data.addAll(items)
        this.addNextLoad = addNextLoad
        notifyDataSetChanged()
    }

}
