package expenses_solution.login.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.ApplicationScope
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.rememberWindowState
import expenses_solution.login.controller.LoginAction
import expenses_solution.login.model.LoginFormModel
import expenses_solution.login.model.RegisterFormModel
import expenses_solution.login.controller.LoginState
import expenses_solution.login.controller.LoginState.Page.LOGGED_IN
import expenses_solution.login.controller.LoginState.Page.LOGIN
import expenses_solution.login.controller.LoginState.Page.REGISTER
import shareddata.User
import xtracted.view.*

@Composable
fun ApplicationScope.LoginWindow(state: LoginState, trigger: (LoginAction) -> Unit) {
    Window(title          = state.title,
           onCloseRequest = { exitApplication() },
           state          = rememberWindowState(width    = 400.dp,
                                                height   = 580.dp,
                                                position = WindowPosition(Alignment.Center))) {
        LoginUi(state, trigger)
    }
}

@Composable
fun LoginUi(state: LoginState, trigger: (LoginAction) -> Unit) {
    Box(modifier         = Modifier.fillMaxSize()
                                   .padding(30.dp),
        contentAlignment = Alignment.Center){

        when(state.currentPage){
            LOGIN     -> LoginPage(state.loginFormModel, trigger)
            REGISTER  -> RegisterPage(state.registerFormModel, trigger)
            LOGGED_IN -> LoggedInPage(state.user!!)
        }
    }
}

@Composable
fun LoginPage(formModel: LoginFormModel, trigger: (LoginAction) -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {

        with(formModel){
            FormField(attribute     = username,
                      onValueChange = { trigger(LoginAction.SetUserName(it)) })

            FormField(attribute     = password,
                      password      = true,
                      onValueChange = { trigger(LoginAction.SetPassword(it)) })

            AlignRight { ErrorMessage(errorMessage) }
        }

        VSpace(10.dp)

        AlignLeftRight {
            ActionClickableText(trigger = trigger,
                                action  = LoginAction.SwitchToRegister)
            ActionButton(trigger = trigger,
                         action  = LoginAction.Login(formModel.isValid))
        }
    }
}

@Composable
fun RegisterPage(formModel: RegisterFormModel, trigger: (LoginAction) -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {

        with(formModel){
            val captionWidth = 140.dp

            FormField(attribute     = username,
                      captionWidth  = captionWidth,
                      onValueChange = { trigger(LoginAction.SetUserName(it)) })

            FormField(attribute     = password,
                      captionWidth  = captionWidth,
                      password      = true,
                      onValueChange = { trigger(LoginAction.SetPassword(it)) })

            FormField(attribute     = retypedPassword,
                      captionWidth  = captionWidth,
                      password      = true,
                      onValueChange = { trigger(LoginAction.SetRetypedPassword(it)) })

            BooleanFormField(attribute     = supervisor,
                             captionWidth  = captionWidth,
                             onValueChange = { trigger(LoginAction.SetSupervisor(it)) })

            AlignRight { ErrorMessage(formModel.errorMessage) }
        }

        VSpace(10.dp)

        AlignLeftRight {
            ActionClickableText(trigger = trigger,
                                action  = LoginAction.SwitchToLogin)
            ActionButton(trigger = trigger,
                         action  = LoginAction.Register(formModel.isValid))
        }
    }
}


@Composable
fun LoggedInPage(user: User) {
    Column(horizontalAlignment = Alignment.CenterHorizontally,
           verticalArrangement = Arrangement.spacedBy(20.dp)) {
        Text(text     = "Hello",
             fontSize = 32.sp)
        Text(text     = user.name,
             fontSize = 32.sp)
        VulcanSalute()
    }
}



