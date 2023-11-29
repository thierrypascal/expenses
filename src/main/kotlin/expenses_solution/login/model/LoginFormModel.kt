package expenses_solution.login.model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import xtracted.data.Attribute

class LoginFormModel {
    var username         by mutableStateOf(Attribute("Username", "", "", false))
    var password     by mutableStateOf(Attribute("Password", "", "", false))

    var errorMessage by mutableStateOf("")

    val isValid
        get() = username.isValid && password.isValid
}