package ademar.tvmaze

import ademar.tvmaze.arch.ArchBinder
import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber
import javax.inject.Inject

@HiltAndroidApp
class App : Application() {

    // creates arch binder before any activity
    @Inject lateinit var archBinder: ArchBinder

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }

}
