package ademar.tvmaze.page.seasons

import ademar.tvmaze.R
import ademar.tvmaze.page.seasons.Contract.Item
import ademar.tvmaze.page.seasons.SeasonsViewHolder.EpisodeItemViewHolder
import ademar.tvmaze.page.seasons.SeasonsViewHolder.SeasonHeaderViewHolder
import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class SeasonAdapter(
    private val callback: SeasonsEpisodeCallback,
) : RecyclerView.Adapter<SeasonsViewHolder>() {

    private val viewTypeHeader = 1
    private val viewTypeItem = 2
    private val data = mutableListOf<Item>()

    init {
        setHasStableIds(true)
    }

    override fun getItemViewType(position: Int): Int {
        return if (data[position] is Item.SeasonHeader) viewTypeHeader else viewTypeItem
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SeasonsViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return if (viewType == viewTypeHeader) {
            SeasonHeaderViewHolder(inflater.inflate(R.layout.item_seasons_header, parent, false))
        } else {
            EpisodeItemViewHolder(inflater.inflate(R.layout.item_seasons_episode, parent, false), callback)
        }
    }

    override fun onBindViewHolder(holder: SeasonsViewHolder, position: Int) {
        val item = data[position]
        if (holder is SeasonHeaderViewHolder && item is Item.SeasonHeader) {
            holder.bind(item)
        } else if (holder is EpisodeItemViewHolder && item is Item.Episode) {
            holder.bind(item)
        }
    }

    override fun getItemCount(): Int = data.size

    override fun getItemId(position: Int): Long = data[position].hashCode().toLong()

    @SuppressLint("NotifyDataSetChanged")
    fun setItems(items: List<Item>) {
        data.clear()
        data.addAll(items)
        notifyDataSetChanged()
    }

}
