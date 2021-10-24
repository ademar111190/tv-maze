package ademar.tvmaze.page.detail

import ademar.tvmaze.arch.ArchView
import ademar.tvmaze.data.Show
import androidx.annotation.DrawableRes

interface Contract {

    interface View : ArchView<Model, Command>

    sealed class Command {

        data class Initial(
            val id: Long?,
        ) : Command()

    }

    sealed class State {

        object InvalidIdState : State()

        data class ErrorState(
            val message: String,
        ) : State()

        data class InitialDataState(
            val show: Show,
        ) : State()

    }

    sealed class Model {

        object Loading : Model()

        data class Error(
            val message: String,
        ) : Model()

        data class NoShow(
            val message: String,
        ) : Model()

        data class ShowModel(
            val show: Show,
            val summary: CharSequence,
            @DrawableRes val monday: Int,
            @DrawableRes val tuesday: Int,
            @DrawableRes val wednesday: Int,
            @DrawableRes val thursday: Int,
            @DrawableRes val friday: Int,
            @DrawableRes val saturday: Int,
            @DrawableRes val sunday: Int,
        ) : Model()

    }

}