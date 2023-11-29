package expenses_solution.login.controller

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import expenses_solution.login.model.LoginFormModel
import expenses_solution.login.model.RegisterFormModel
import shareddata.User

class LoginState {
    enum class Page {
        LOGIN, REGISTER, LOGGED_IN
    }

    val title = "Login or Register"

    var currentPage by mutableStateOf(Page.LOGIN)

    var user : User? = null

    val loginFormModel    = LoginFormModel()
    val registerFormModel = RegisterFormModel()

}

