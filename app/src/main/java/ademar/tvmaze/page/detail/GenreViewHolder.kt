package ademar.tvmaze.page.detail

import ademar.tvmaze.R
import ademar.tvmaze.data.Genre
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class GenreViewHolder(
    itemView: View,
) : RecyclerView.ViewHolder(itemView) {

    fun bind(genre: Genre) {
        itemView.findViewById<ImageView>(R.id.genre).setImageResource(genre.icon)
        itemView.findViewById<TextView>(R.id.title).setText(genre.title)
    }

}
