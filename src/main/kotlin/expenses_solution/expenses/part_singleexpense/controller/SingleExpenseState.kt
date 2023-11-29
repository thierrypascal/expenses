package expenses_solution.expenses.part_singleexpense.controller

import expenses_solution.expenses.part_singleexpense.model.ExpenseFormModel
import shareddata.User


data class SingleExpenseState(val user: User,
                              val title : String,
                              val currentExpense: ExpenseFormModel
)