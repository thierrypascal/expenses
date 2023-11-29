package expenses_solution.login.controller

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AppRegistration
import androidx.compose.material.icons.filled.Login
import androidx.compose.ui.graphics.vector.ImageVector
import xtracted.controller.Action

sealed class LoginAction(
        override val name: String,
        override val icon: ImageVector? = null,
        override val enabled: Boolean) : Action {

    class Login(enabled: Boolean)                  : LoginAction("Login", Icons.Filled.Login, enabled)
    class Register(enabled: Boolean)               : LoginAction("Register", Icons.Filled.AppRegistration, enabled)

    class SetUserName(val userName: String)        : LoginAction("Set Username", null, true)
    class SetPassword(val password: String)        : LoginAction("Set Password", null, true)
    class SetRetypedPassword(val password: String) : LoginAction("Retype Password", null, true)
    class SetSupervisor(val supervisor: Boolean)   : LoginAction("Set Supervisor", null, true)

    object SwitchToLogin                           : LoginAction("Login", null, true)
    object SwitchToRegister                        : LoginAction("Register", null, true)
}