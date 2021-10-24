package ademar.tvmaze.page.favorite

import ademar.tvmaze.arch.ArchView
import ademar.tvmaze.data.Show

interface Contract {

    interface View : ArchView<Model, Command>

    sealed class Command {

        object Initial : Command()

        data class SeriesSelected(
            val id: Long,
        ) : Command()

    }

    sealed class State {

        object NoFavorites : State()

        data class Favorites(
            val series: List<Show>,
        ) : State()

        data class ErrorState(
            val message: String,
        ) : State()

    }

    sealed class Model {

        object Loading : Model()

        data class Error(
            val message: String,
        ) : Model()

        data class Empty(
            val message: String,
        ) : Model()

        data class DataModel(
            val series: List<Show>,
        ) : Model()

    }

}
