package expenses_solution.management.view

import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.ApplicationScope
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.rememberWindowState
import expenses_solution.management.controller.ManagementState
import expenses_solution.expenses.combined.controller.ExpensesController
import expenses_solution.expenses.combined.view.ExpensesUi
import expenses_solution.login.controller.LoginController
import expenses_solution.login.view.LoginUi

@Composable
fun ApplicationScope.ManagementWindow(state: ManagementState) {
    Window(title          = "${state.title} of '${state.user?.name?: "nobody"}'",
           onCloseRequest = { exitApplication() },
           state          = rememberWindowState(width    = 580.dp,
                                                height   = 550.dp,
                                                position = WindowPosition(Alignment.Center))) {
        ManagementUi(state)
    }
}

@Composable
fun ManagementUi(state: ManagementState) {
    when(state.activeController){
        is LoginController -> showLoginUi(state.activeController)
        is ExpensesController -> showWorkReportUi(state.activeController)
    }
}

@Composable
private fun showLoginUi(controller: LoginController) {
    LoginUi(state   = controller.state,
            trigger = { controller.trigger(it)} )

}

@Composable
private fun showWorkReportUi(controller: ExpensesController) {
    ExpensesUi(state = controller.state )
}
