package expenses_solution.expenses.part_overview.controller

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector
import xtracted.controller.Action

sealed class OverviewAction(
        override val name: String,
        override val icon: ImageVector? = null,
        override val enabled: Boolean) : Action {

    object Create                             : OverviewAction("New Expense", Icons.Filled.AddCircle, true)
    object Logout                             : OverviewAction("Logout",     Icons.Filled.Logout, true)
    object ReloadAllExpenses                  : OverviewAction("Reload All", Icons.Filled.Cached, true)
    object MonthlyRelease                     : OverviewAction("Monthly Release", Icons.Filled.CloudUpload, true)
    class  SelectExpense(val expenseId: Int)  : OverviewAction("Select Expense", null, true)
}