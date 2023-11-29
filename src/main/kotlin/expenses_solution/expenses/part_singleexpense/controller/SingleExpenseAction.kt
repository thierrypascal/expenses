package expenses_solution.expenses.part_singleexpense.controller

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector
import expenses_solution.expenses.shareddata.CostCentre
import expenses_solution.expenses.shareddata.Currency
import expenses_solution.expenses.shareddata.ObjectType
import expenses_solution.expenses.shareddata.Service
import xtracted.controller.Action

sealed class SingleExpenseAction(override val name: String,
                                 override val icon: ImageVector? = null,
                                 override val enabled: Boolean) : Action {

    object Update           : SingleExpenseAction("Save",               Icons.Filled.Save, true)
    object Back             : SingleExpenseAction("Back",               Icons.Filled.ArrowBack, true)
    object Delete           : SingleExpenseAction("Delete",             Icons.Filled.RemoveCircle, true)
    object Logout           : SingleExpenseAction("Logout",             Icons.Filled.Logout, true)
    object ReloadExpense    : SingleExpenseAction("Reload Expenses",    Icons.Filled.Cached, true)

    class SetTitle          (val newValue: String)      : SingleExpenseAction("Set Title", null, true)
    class SetDescription    (val newValue: String)      : SingleExpenseAction("Set Description", null, true)
    class SetDate           (val newValue: String)      : SingleExpenseAction("Set Date", null, true)
    class SetObjectType     (val newValue: ObjectType)  : SingleExpenseAction("Set Object Type", null, true)
    class SetCostCentre     (val newValue: CostCentre)  : SingleExpenseAction("Set Cost Centre", null, true)
    class SetService        (val newValue: Service)     : SingleExpenseAction("Set Service", null, true)
    class SetAmount         (val newValue: String)      : SingleExpenseAction("Set Amount", null, true)
    class SetCurrency       (val newValue: Currency)    : SingleExpenseAction("Set Currency", null, true)
    class SetApproved       (val newValue: Boolean)     : SingleExpenseAction("Set Approved", null, true)
    class SetReleased       (val newValue: Boolean)     : SingleExpenseAction("Set Released", null, true)
}