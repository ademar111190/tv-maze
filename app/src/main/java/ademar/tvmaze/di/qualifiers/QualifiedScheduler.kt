package ademar.tvmaze.di.qualifiers

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class QualifiedScheduler(val option: QualifiedSchedulerOption)

enum class QualifiedSchedulerOption {
    IO,
    COMPUTATION,
    MAIN_THREAD,
}
