package ademar.tvmaze.page.detail

import ademar.tvmaze.R
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.page_detail)
        Log.d("Ademar", "> ${intent.extras?.getLong("SHOW_ID")}")
    }

}
