package expenses_solution.expenses.part_singleexpense.model

import java.time.format.DateTimeFormatter
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import expenses_solution.expenses.shareddata.Expense
import xtracted.data.Attribute

class ExpenseFormModel(expense: Expense){
    var id                  by mutableStateOf(Attribute("Id", expense.id, expense.id.toString(), true))
    var userId              by mutableStateOf(Attribute("User ID", expense.userId, expense.id.toString(), true))
    var title             by mutableStateOf(Attribute("Title", expense.title, expense.title, true))
    var description       by mutableStateOf(Attribute("Description", expense.description, expense.description, true))
    var date           by mutableStateOf(Attribute("Date", expense.date, expense.date.format(DateTimeFormatter.ofPattern("d.M.YY")), true))
    var objectType    by mutableStateOf(Attribute("Object Type", expense.objectType, expense.objectType.toString(), true))
    var costCentre    by mutableStateOf(Attribute("Cost Centre", expense.costCentre, expense.costCentre.toString(), true))
    var service          by mutableStateOf(Attribute("Service", expense.service, expense.service.toString(), true))
    var amount            by mutableStateOf(Attribute("Amount", expense.amount, expense.amount.toString(), true))
    var currency        by mutableStateOf(Attribute("Currency", expense.currency, expense.currency.toString(), true))
    var approved        by mutableStateOf(Attribute("Approved", expense.approved, expense.approved.toString(), true))
    var released        by mutableStateOf(Attribute("Read Only", expense.released, expense.released.toString(), true))

    fun asExpense() =
        Expense(id.value, userId.value, title.value, description.value, date.value, objectType.value, costCentre.value, service.value, amount.value, currency.value, approved.value, released.value)
}
