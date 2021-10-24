package ademar.tvmaze

import ademar.tvmaze.page.search.SearchFragment
import ademar.tvmaze.page.series.SeriesFragment
import ademar.tvmaze.widget.Reselectable
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager.POP_BACK_STACK_INCLUSIVE
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.page_home)
        setUpNavigation()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        if (lastFragmentOrNull() == null) {
            finish()
        }
    }

    private fun setUpNavigation() {
        val navigation = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        navigation.setOnItemReselectedListener { menu ->
            val current = lastFragmentOrNull()
            val itemId = menu.itemId
            if (current is Reselectable) current.onReselected()
            if (current == null && itemId == R.id.navigation_series) {
                val fragment = SeriesFragment()
                supportFragmentManager.popBackStackImmediate(null, POP_BACK_STACK_INCLUSIVE)
                supportFragmentManager.beginTransaction()
                    .replace(R.id.content, fragment)
                    .addToBackStack("$fragment")
                    .commitAllowingStateLoss()
            }
        }
        navigation.setOnItemSelectedListener { menu ->
            val fragment = when (menu.itemId) {
                R.id.navigation_series -> {
                    supportFragmentManager.popBackStackImmediate(null, POP_BACK_STACK_INCLUSIVE)
                    SeriesFragment()
                }
                R.id.navigation_search -> SearchFragment()
                else -> null
            }
            if (fragment != null) {
                val transaction = supportFragmentManager.beginTransaction()
                if (fragment is SeriesFragment) {
                    transaction.replace(R.id.content, fragment)
                } else {
                    transaction.add(R.id.content, fragment)
                }
                transaction.addToBackStack("$fragment")
                transaction.commitAllowingStateLoss()
            }
            true
        }
        navigation.selectedItemId = R.id.navigation_series
        supportFragmentManager.addOnBackStackChangedListener {
            val id = when (lastFragmentOrNull()) {
                is SeriesFragment -> R.id.navigation_series
                is SearchFragment -> R.id.navigation_search
                else -> null
            }
            if (id != null) {
                navigation.menu.findItem(id)?.isChecked = true
            }
        }
        val initial = when (intent?.extras?.getString("INITIAL_ACTION", null)) {
            "ademar.tvmaze.action.series" -> R.id.navigation_series
            "ademar.tvmaze.action.search" -> R.id.navigation_search
            else -> null
        }
        if (initial != null) {
            navigation.selectedItemId = initial
        }
    }

    private fun lastFragmentOrNull(): Fragment? {
        return supportFragmentManager.fragments.findLast { frag ->
            frag is SeriesFragment || frag is SearchFragment
        }
    }

}
