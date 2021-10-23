package ademar.tvmaze.page.series

import ademar.tvmaze.arch.ArchView
import ademar.tvmaze.data.Show

interface Contract {

    interface View : ArchView<Model, Command>

    sealed class Command {

        object Initial : Command()

        object NextPage : Command()

    }

    sealed class State {

        data class DataState(
            val series: List<Show>
        ) : State()

        data class ErrorState(
            val message: String,
        ) : State()

    }

    sealed class Model {

        object LoadModel : Model()

        data class DataModel(
            val series: List<Show>,
        ) : Model()

        data class Error(
            val message: String,
        ) : Model()

    }

}
