package ademar.tvmaze.page.favorite

import ademar.tvmaze.R
import ademar.tvmaze.data.Show
import ademar.tvmaze.tile.SeriesTileCallback
import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class FavoriteAdapter(
    private val callback: SeriesTileCallback,
) : RecyclerView.Adapter<FavoriteViewHolder>() {

    private val data = mutableListOf<Show>()

    init {
        setHasStableIds(true)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return FavoriteViewHolder(inflater.inflate(R.layout.item_series, parent, false), callback)
    }

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
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
