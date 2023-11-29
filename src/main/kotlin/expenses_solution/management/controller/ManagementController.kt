package expenses_solution.management.controller

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import expenses_solution.expenses.combined.controller.ExpensesController
import expenses_solution.expenses.sharedrepository.ExpenseRepository
import expenses_solution.login.controller.LoginController
import expenses_solution.login.repository.UserRepository
import shareddata.User

class ManagementController(private val userRepository: UserRepository,
                           private val expensesRepository: ExpenseRepository
) {

    var state by mutableStateOf(
        ManagementState(title            = "Expense Management EDEA",
                                                 activeController = createLoginController(),
                                                 user             = null)
    )

    private fun switchToExpenses(user: User) {
       state = state.copy(activeController = createExpensesController(user),
                          user             = user)
    }

    private fun switchToLogin() {
        state = state.copy(activeController = createLoginController(),
                           user             = null)
    }

    private fun createLoginController() =
        LoginController(repository = userRepository,
                        onLoggedIn = { switchToExpenses(it) })

    private fun createExpensesController(user: User) =
        ExpensesController(user       = user,
                             repository = expensesRepository,
                             onLogout   = { switchToLogin() })
}