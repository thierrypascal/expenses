package expenses_solution.expenses.combined.controller

import shareddata.User
import xtracted.controller.Controller


data class ExpensesState(val title: String,
                         val user: User,
                         val activeController : Controller<*>
                          )
