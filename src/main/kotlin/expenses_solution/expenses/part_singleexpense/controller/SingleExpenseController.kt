package expenses_solution.expenses.part_singleexpense.controller

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import expenses_solution.expenses.part_singleexpense.model.ExpenseFormModel
import expenses_solution.expenses.shareddata.CostCentre
import expenses_solution.expenses.shareddata.Currency
import expenses_solution.expenses.shareddata.ObjectType
import expenses_solution.expenses.shareddata.Service
import expenses_solution.expenses.sharedrepository.ExpenseRepository
import shareddata.User
import xtracted.controller.Controller

class SingleExpenseController(user: User,
                              expenseId: Int,
                              val repository: ExpenseRepository,
                              val onLogout:   () -> Unit,
                              val onFinished: () -> Unit) : Controller<SingleExpenseAction> {

    var state by mutableStateOf(
        SingleExpenseState(
            user           = user,
            title          = "New Expense Demo",
            currentExpense = ExpenseFormModel(repository.readExpense(expenseId)!!)
        )
    )

    override fun trigger(action: SingleExpenseAction) {
        if(!action.enabled){
            return
        }
        when(action){
            is SingleExpenseAction.Update           -> update()
            is SingleExpenseAction.Back             -> back()
            is SingleExpenseAction.Delete           -> delete()
            is SingleExpenseAction.Logout           -> onLogout()
            is SingleExpenseAction.ReloadExpense    -> reloadCurrentExpense()

            is SingleExpenseAction.SetTitle         -> setTitle(action.newValue)
            is SingleExpenseAction.SetDescription   -> setDescription(action.newValue)
            is SingleExpenseAction.SetDate          -> setDate(action.newValue)
            is SingleExpenseAction.SetObjectType    -> setObjectType(action.newValue)
            is SingleExpenseAction.SetCostCentre    -> setCostCentre(action.newValue)
            is SingleExpenseAction.SetService       -> setService(action.newValue)
            is SingleExpenseAction.SetAmount        -> setAmount(action.newValue)
            is SingleExpenseAction.SetCurrency      -> setCurrency(action.newValue)
            is SingleExpenseAction.SetApproved      -> setApproved(action.newValue)
            is SingleExpenseAction.SetReleased      -> setReleased(action.newValue)
        }
    }

    private fun update() {
        with(state.currentExpense) {
            repository.updateExpense(asExpense())
            onFinished()
        }
    }

    private fun back() {
        onFinished()
    }

    private fun delete() {
        with(state.currentExpense) {
            repository.deleteExpense(id.value)
            onFinished()
        }
    }

    private fun reloadCurrentExpense() {
        with(state){
            state = copy(currentExpense = ExpenseFormModel(repository.readExpense(currentExpense.id.value)!!))
        }
    }

    private fun setTitle(newValue: String) {
        with(state.currentExpense) {
            title = if (newValue.isNotBlank()) {
                title.copy(value = newValue, valueAsText = newValue, isValid = true)
            }else{
                title.copy(valueAsText = newValue, isValid = false)
            }
        }
    }

    private fun setDescription(newValue: String) {
        with(state.currentExpense) {
            description = if (newValue.isNotBlank()) {
                description.copy(value = newValue, valueAsText = newValue, isValid = true)
            }else{
                description.copy(valueAsText = newValue, isValid = false)
            }
        }
    }

    private fun setDate(newValue: String) {
        with(state.currentExpense){
            val newDate = newValue.toLocalDateOrNull()

            date = if (null != newDate) {
                date.copy(value = newDate, valueAsText = newDate.format(dateFormatter), isValid = true)
            } else {
                date.copy(valueAsText = newValue, isValid = false)
            }
        }
    }

    private fun setObjectType(newValue: ObjectType) {
        with(state.currentExpense) {
            objectType = objectType.copy(value = newValue, valueAsText = newValue.toString(), isValid = true)
        }
    }

    private fun setCostCentre(newValue: CostCentre) {
        with(state.currentExpense) {
            costCentre = costCentre.copy(value = newValue, valueAsText = newValue.toString(), isValid = true)
        }
    }

    private fun setService(newValue: Service) {
        with(state.currentExpense) {
            service = service.copy(value = newValue, valueAsText = newValue.toString(), isValid = true)
        }
    }

    private fun setAmount(newValue: String) {
        with(state.currentExpense) {
            val newFloat = newValue.toFloatOrNull()

            amount = if (null != newFloat) {
                amount.copy(value = newFloat, valueAsText = newFloat.toString(), isValid = true)
            } else {
                amount.copy(valueAsText = newValue, isValid = false)
            }
        }
    }

    private fun setCurrency(newValue: Currency) {
        with(state.currentExpense) {
            currency = currency.copy(value = newValue, valueAsText = newValue.toString(), isValid = true)
        }
    }


    private fun setApproved(newValue: Boolean) {
        with(state.currentExpense){
            approved = approved.copy(value = newValue, valueAsText = if(newValue) "approved" else "not approved")
        }
    }

    private fun setReleased(newValue: Boolean) {
        with(state.currentExpense){
            released = released.copy(value = newValue, valueAsText = if(newValue) "released" else "not released")
        }
    }

    private val dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yy")

    private fun String.toLocalDateOrNull(): LocalDate? {
        return try {
            LocalDate.parse(this, dateFormatter)
        } catch (e: DateTimeParseException) {
            null
        }
    }
}