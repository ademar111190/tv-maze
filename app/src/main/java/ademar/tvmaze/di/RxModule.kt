package ademar.tvmaze.di

import ademar.tvmaze.di.qualifiers.QualifiedScheduler
import ademar.tvmaze.di.qualifiers.QualifiedSchedulerOption.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Singleton

@[Module InstallIn(SingletonComponent::class)]
object RxModule {

    @Provides
    fun providesCompositeDisposable() = CompositeDisposable()

    @[Provides Singleton QualifiedScheduler(IO)]
    fun providesSchedulerIo(): Scheduler = Schedulers.io()

    @[Provides Singleton QualifiedScheduler(COMPUTATION)]
    fun providesSchedulerComputation(): Scheduler = Schedulers.computation()

    @[Provides Singleton QualifiedScheduler(MAIN_THREAD)]
    fun providesSchedulerMainThread(): Scheduler = AndroidSchedulers.mainThread()

}
