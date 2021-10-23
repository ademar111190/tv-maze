package ademar.tvmaze.page.series

import ademar.tvmaze.arch.ArchView
import ademar.tvmaze.data.Show

interface Contract {

    interface View : ArchView<Model, Command>

    sealed class Command {

        object Initial : Command()

        object NextPage : Command()

        object ChangeSort : Command()

    }

    sealed class State {

        data class DataState(
            val series: List<Show>,
            val hasNextPage: Boolean,
            val sortOption: SortOption,
        ) : State()

        data class ErrorState(
            val message: String,
        ) : State()

    }

    sealed class Model {

        object LoadModel : Model()

        data class DataModel(
            val title: String,
            val series: List<Show>,
            val showNextLoad: Boolean,
        ) : Model()

        data class Error(
            val title: String,
            val message: String,
        ) : Model()

    }

    enum class SortOption {
        ID,
        NAME,
    }

}
