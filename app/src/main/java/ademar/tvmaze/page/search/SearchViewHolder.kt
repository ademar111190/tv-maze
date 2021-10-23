package ademar.tvmaze.page.search

import ademar.tvmaze.data.Show
import ademar.tvmaze.tile.SeriesTile
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class SearchViewHolder(
    itemView: View,
) : RecyclerView.ViewHolder(itemView) {

    private val seriesTile = SeriesTile(itemView)

    fun bind(show: Show) {
        seriesTile.bind(show)
    }

}
