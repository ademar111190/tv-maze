package ademar.tvmaze.page.search

import ademar.tvmaze.R
import ademar.tvmaze.data.Show
import ademar.tvmaze.tile.SeriesTileCallback
import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class SearchAdapter(
    private val callback: SeriesTileCallback,
) : RecyclerView.Adapter<SearchViewHolder>() {

    private val data = mutableListOf<Show>()

    init {
        setHasStableIds(true)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return SearchViewHolder(inflater.inflate(R.layout.item_series, parent, false), callback)
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount(): Int = data.size

    override fun getItemId(position: Int): Long = data[position].id

    @SuppressLint("NotifyDataSetChanged")
    fun setItems(items: List<Show>) {
        data.clear()
        data.addAll(items)
        notifyDataSetChanged()
    }

}
