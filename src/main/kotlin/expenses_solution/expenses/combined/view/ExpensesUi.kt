package expenses_solution.expenses.combined.view

import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.ApplicationScope
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.rememberWindowState
import expenses_solution.expenses.combined.controller.ExpensesState
import expenses_solution.expenses.part_overview.controller.OverviewController
import expenses_solution.expenses.part_overview.view.OverviewUi
import expenses_solution.expenses.part_singleexpense.controller.SingleExpenseController
import expenses_solution.expenses.part_singleexpense.view.SingleExpenseUi

@Composable
fun ApplicationScope.CombinedWindow(state: ExpensesState) {
    Window(title          = state.title,
           onCloseRequest = { exitApplication() },
           state          = rememberWindowState(width    = 580.dp,
                                                height   = 550.dp,
                                                position = WindowPosition(Alignment.Center))) {

        ExpensesUi(state)
    }
}

@Composable
fun ExpensesUi(state: ExpensesState) {
    when(state.activeController){
        is OverviewController -> showOverviewUi(state.activeController)
        is SingleExpenseController -> showSingleExpenseUi(state.activeController)
    }
}

@Composable
private fun showSingleExpenseUi(controller: SingleExpenseController) {
    SingleExpenseUi(user    = controller.state.user,
                    expense  = controller.state.currentExpense,
                    trigger = { controller.trigger(it)})
}

@Composable
private fun showOverviewUi(controller: OverviewController) {
    OverviewUi(user    = controller.state.user,
               reports = controller.state.allExpenses,
               trigger = { controller.trigger(it)})
}
