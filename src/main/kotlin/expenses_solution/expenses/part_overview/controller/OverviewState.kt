package expenses_solution.expenses.part_overview.controller

import expenses_solution.expenses.shareddata.Expense
import shareddata.User

data class OverviewState(val title : String,
                         val user: User,
                         val allExpenses : List<Expense>)