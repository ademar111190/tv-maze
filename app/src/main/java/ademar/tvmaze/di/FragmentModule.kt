package ademar.tvmaze.di

import ademar.tvmaze.di.FragmentModule.Declarations
import ademar.tvmaze.page.series.SeriesFragment
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.FragmentComponent
import ademar.tvmaze.page.series.Contract as SeriesContract

@Module(includes = [Declarations::class])
@InstallIn(FragmentComponent::class)
object FragmentModule {

    @[Module InstallIn(FragmentComponent::class)]
    interface Declarations {

        @Binds fun bindSeriesView(impl: SeriesFragment): SeriesContract.View

    }

}