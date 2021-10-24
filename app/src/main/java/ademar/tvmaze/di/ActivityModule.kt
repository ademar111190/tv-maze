package ademar.tvmaze.di

import ademar.tvmaze.di.ActivityModule.Declarations
import ademar.tvmaze.page.detail.DetailActivity
import ademar.tvmaze.page.seasons.SeasonsActivity
import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import ademar.tvmaze.page.detail.Contract as DetailContract
import ademar.tvmaze.page.seasons.Contract as SeasonsContract

@Module(includes = [Declarations::class])
@InstallIn(ActivityComponent::class)
object ActivityModule {

    @Provides fun providesCompatActivity(impl: Activity) = impl as AppCompatActivity

    @[Module InstallIn(ActivityComponent::class)]
    interface Declarations {

        @Binds fun bindDetailView(impl: DetailActivity): DetailContract.View

        @Binds fun bindSeasonsView(impl: SeasonsActivity): SeasonsContract.View

    }

}
