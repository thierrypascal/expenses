package expenses_solution.expenses.combined.controller

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import expenses_solution.expenses.part_overview.controller.OverviewController
import expenses_solution.expenses.part_singleexpense.controller.SingleExpenseController
import expenses_solution.expenses.shareddata.Expense
import expenses_solution.expenses.sharedrepository.ExpenseRepository
import shareddata.User
import xtracted.controller.Action
import xtracted.controller.Controller

class ExpensesController(user: User, val repository: ExpenseRepository, val onLogout: () -> Unit) :
    Controller<Action> {

    var state by mutableStateOf(
        ExpensesState(title            = "Expenses Demo",
                                                user             = user,
                                                activeController = createOverviewController(user))
    )

    override fun trigger(action: Action) {
        throw IllegalStateException("ExpensesController doesn't have actions. ")
    }

    private fun switchToExpense(reportId: Int) {
        state = state.copy(activeController = createSingleExpenseController(state.user, reportId))
    }

    private fun switchToNewExpense() {
        val newReport = repository.create(Expense(-999, state.user.id))
        switchToExpense(newReport.id)
    }

    private fun switchToOverview() {
        state = state.copy(activeController = createOverviewController(state.user))
    }

    private fun createOverviewController(user: User) =
        OverviewController(user             = user,
                           repository       = repository,
                           onLogout         = onLogout,
                           onCreate         = { switchToNewExpense() },
                           onExpenseSelected = { switchToExpense(it) })

    private fun createSingleExpenseController(user: User, reportId: Int) =
        SingleExpenseController(user = user,
                               expenseId   = reportId,
                               repository = repository,
                               onFinished = { switchToOverview() },
                               onLogout   = { onLogout() })
}