package expenses_solution.expenses.part_overview.controller

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import expenses_solution.expenses.sharedrepository.ExpenseRepository
import shareddata.User
import xtracted.controller.Controller

class OverviewController (user: User,
                          val repository: ExpenseRepository,
                          val onExpenseSelected: (Int) -> Unit,
                          val onCreate:            () -> Unit,
                          val onLogout :           () -> Unit = {}) : Controller<OverviewAction> {

    var state by mutableStateOf(
        OverviewState(title = "Overview Demo",
                                              user = user,
                                              allExpenses = repository.readAllExpenses(user.id)
                                             )
    )

    override fun trigger(action: OverviewAction) {
        if(!action.enabled){
            return
        }

        when(action){
            is OverviewAction.Logout              -> onLogout()
            is OverviewAction.SelectExpense       -> onExpenseSelected(action.expenseId)
            is OverviewAction.Create              -> onCreate()
            is OverviewAction.ReloadAllExpenses   -> reloadAll()
            OverviewAction.MonthlyRelease         -> monthlyRelease()
        }
    }

    private fun reloadAll() {
        state = state.copy(allExpenses =  repository.readAllExpenses(state.user.id))
    }

    private fun monthlyRelease(){
        val allExpenses = repository.readAllExpenses(state.user.id).toMutableList()

        //TODO: make it only clickable if user has expenses
        if (allExpenses.isNotEmpty()){
            for (i in allExpenses.indices){
                val readOnlyCopy = allExpenses[i].copy(released = true)

                allExpenses[i] = readOnlyCopy

                repository.updateExpense(readOnlyCopy)
            }

            state = state.copy(allExpenses = allExpenses)
        }
    }

}