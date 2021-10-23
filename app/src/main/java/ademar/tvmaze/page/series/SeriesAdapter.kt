package ademar.tvmaze.page.series

import ademar.tvmaze.R
import ademar.tvmaze.data.Show
import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class SeriesAdapter : RecyclerView.Adapter<SeriesViewHolder>() {

    private val data = mutableListOf<Show>()

    init {
        setHasStableIds(true)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SeriesViewHolder {
        return SeriesViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_series, parent, false))
    }

    override fun onBindViewHolder(holder: SeriesViewHolder, position: Int) {
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