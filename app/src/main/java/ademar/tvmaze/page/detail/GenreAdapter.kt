package ademar.tvmaze.page.detail

import ademar.tvmaze.R
import ademar.tvmaze.data.Genre
import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class GenreAdapter : RecyclerView.Adapter<GenreViewHolder>() {

    private val data = mutableListOf<Genre>()

    init {
        setHasStableIds(true)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GenreViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return GenreViewHolder(inflater.inflate(R.layout.item_detail_genre, parent, false))
    }

    override fun onBindViewHolder(holder: GenreViewHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount(): Int = data.size

    override fun getItemId(position: Int): Long = data[position].ordinal.toLong()

    @SuppressLint("NotifyDataSetChanged")
    fun setItems(items: List<Genre>) {
        data.clear()
        data.addAll(items)
        notifyDataSetChanged()
    }

}
