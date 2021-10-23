package ademar.tvmaze.page.search

import ademar.tvmaze.data.Show
import ademar.tvmaze.tile.SeriesTile
import ademar.tvmaze.tile.SeriesTileCallback
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class SearchViewHolder(
    itemView: View,
    callback: SeriesTileCallback,
) : RecyclerView.ViewHolder(itemView) {

    private val seriesTile = SeriesTile(itemView, callback)

    fun bind(show: Show) {
        seriesTile.bind(show)
    }

}
