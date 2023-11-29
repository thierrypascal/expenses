package expenses_solution.login.controller

import expenses_solution.login.model.LoginFormModel
import expenses_solution.login.model.RegisterFormModel
import expenses_solution.login.repository.UserRepository
import shareddata.User
import xtracted.controller.Controller

class LoginController(val repository: UserRepository,
                      val onLoggedIn : (User) -> Unit = {}) : Controller<LoginAction> {
    val state = LoginState()


    override fun trigger(action: LoginAction){
        if(!action.enabled){
            return
        }

        when (action){
            is LoginAction.Login              -> login()
            is LoginAction.Register           -> register()
            is LoginAction.SetPassword        -> setPassword(action.password)
            is LoginAction.SetRetypedPassword -> setRetypedPassword(action.password)
            is LoginAction.SetSupervisor      -> setSupervisor(action.supervisor)
            is LoginAction.SetUserName        -> setUserName(action.userName)
            is LoginAction.SwitchToLogin      -> state.currentPage = LoginState.Page.LOGIN
            is LoginAction.SwitchToRegister   -> state.currentPage = LoginState.Page.REGISTER
        }
    }

    private fun setUserName(userName: String) {
        with(state){
            when(currentPage){
                LoginState.Page.LOGIN -> updateName(loginFormModel, userName)
                LoginState.Page.REGISTER -> updateName(registerFormModel, userName)

                else -> {throw IllegalStateException()}
            }
        }
    }

    private fun setPassword(password: String) {
        with(state){
            when(currentPage){
                LoginState.Page.LOGIN -> updatePassword(loginFormModel, password)
                LoginState.Page.REGISTER -> updatePassword(registerFormModel, password)

                else -> {throw IllegalStateException()}
            }
        }
    }

    private fun setRetypedPassword(password: String) {
        updateRetypedPassword(state.registerFormModel, password)
    }

    private fun setSupervisor(supervisor: Boolean) {
        updateSupervisor(state.registerFormModel, supervisor)
    }

    private fun setUser(user: User){
        state.user = user
        state.currentPage = LoginState.Page.LOGGED_IN
        onLoggedIn(user)
    }

    private fun login(){
        with(state.loginFormModel){
            when {
                !repository.nameExists(username.value) -> {
                    errorMessage = "Unknown User"
                }
                else                               -> {
                    val user = repository.readUser(userName = username.value, password = password.value)
                    if(null != user){
                        setUser(user)
                    } else {
                        errorMessage = "Wrong Password"
                    }
                }
            }
        }
    }

    private fun register(){
        with(state.registerFormModel){
            when {
                repository.nameExists(username.value)       -> {
                    errorMessage = "Username already exists"
                }
                password.value != retypedPassword.value -> {
                    errorMessage = "Passwords are not the same"
                }
                else                                    -> {
                    setUser(repository.create(username.value, password.value, supervisor.value))
                }
            }
        }
    }

    private fun updateName(model: LoginFormModel, newValue: String) =
        with(model) {
            username = username.copy(value       = newValue,
                             valueAsText = newValue,
                             isValid     = true)
            errorMessage = ""
        }

    private fun updateName(model: RegisterFormModel, newValue: String) {
        with(model){
            username = username.copy(value       = newValue,
                             valueAsText = newValue,
                             isValid     = true)
            errorMessage = ""
        }
    }

    private fun updatePassword(model: LoginFormModel, newValue: String) {
        with(model){
            password = password.copy(value       = newValue,
                                     valueAsText = newValue,
                                     isValid     = newValue.isNotBlank())
            errorMessage = ""
        }
    }

    private fun updatePassword(model: RegisterFormModel, newValue: String) =
        with(model) {
            password = password.copy(value       = newValue,
                                     valueAsText = newValue,
                                     isValid     = newValue.isNotBlank())
            errorMessage = ""
        }

    private fun updateRetypedPassword(model: RegisterFormModel, newValue: String) {
        with(model){
            retypedPassword = retypedPassword.copy(value       = newValue,
                                                   valueAsText = newValue,
                                                   isValid     = newValue.isNotBlank() && newValue == model.password.value)
            errorMessage = ""
        }
    }

    private fun updateSupervisor(model: RegisterFormModel, newValue: Boolean) {
        with(model){
            supervisor = supervisor.copy(value       = newValue,
                                   valueAsText = if(newValue) "Supervisor" else "Employee", )
            errorMessage = ""
        }
    }
}