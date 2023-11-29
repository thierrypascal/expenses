package expenses_solution.login.model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import xtracted.data.Attribute

class RegisterFormModel {
    var username            by mutableStateOf(Attribute("Username", "", "", false))
    var password        by mutableStateOf(Attribute("Password", "", "", false))
    var retypedPassword by mutableStateOf(Attribute("Retype Password", "", "", false))
    var supervisor     by mutableStateOf(Attribute("Supervisor", false, "", true))

    var errorMessage by mutableStateOf("")

    val isValid
        get() = username.isValid && password.isValid && retypedPassword.isValid && supervisor.isValid
}