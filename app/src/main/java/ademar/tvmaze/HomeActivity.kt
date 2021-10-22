package ademar.tvmaze

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import timber.log.Timber

class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.page_home)
        setUpNavigation()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        if (supportFragmentManager.fragments.isEmpty()) {
            finish()
        }
    }

    private fun setUpNavigation() {
        val navigation = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        navigation.setOnItemReselectedListener { menu ->
            Timber.d("menu reselected $menu") // TODO
        }
        navigation.setOnItemSelectedListener { menu ->
            Timber.d("menu selected $menu") // TODO
            false
        }
        navigation.selectedItemId = R.id.navigation_series
        supportFragmentManager.addOnBackStackChangedListener {
            Timber.d("back stack changed listener") // TODO
        }
        val initial = when (intent?.extras?.getString("INITIAL_ACTION", null)) {
            "ademar.tvmaze.series" -> R.id.navigation_series
            else -> null
        }
        if (initial != null) {
            navigation.selectedItemId = initial
        }
    }

}
