package expenses_solution.expenses.part_singleexpense.view

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.ApplicationScope
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.rememberWindowState
import expenses_solution.expenses.part_singleexpense.controller.SingleExpenseAction
import expenses_solution.expenses.part_singleexpense.controller.SingleExpenseState
import expenses_solution.expenses.part_singleexpense.model.ExpenseFormModel
import expenses_solution.expenses.shareddata.CostCentre
import expenses_solution.expenses.shareddata.Currency
import expenses_solution.expenses.shareddata.ObjectType
import expenses_solution.expenses.shareddata.Service
import shareddata.User
import xtracted.view.*

@Composable
fun ApplicationScope.SingleExpenseWindow(state: SingleExpenseState, trigger: (SingleExpenseAction) -> Unit) {
    Window(
        title = state.title,
        onCloseRequest = { exitApplication() },
        state = rememberWindowState(
            width = 400.dp,
            height = 580.dp,
            position = WindowPosition(Alignment.Center)
        )
    ) {
        SingleExpenseUi(state.user, state.currentExpense, trigger)
    }
}

@Composable
fun SingleExpenseUi(user: User, expense: ExpenseFormModel, trigger: (SingleExpenseAction) -> Unit) {
    if (expense.released.value){
        SingleExpenseUiReadOnly(expense, trigger)
    }else{
        SingleExpenseUiEditable(user, expense, trigger)
    }
}

@Composable
fun SingleExpenseUiEditable(user: User, expense: ExpenseFormModel, trigger: (SingleExpenseAction) -> Unit) {
    val scrollState = rememberScrollState()

    Column {
        Toolbar {
            AlignLeftRight {
                ActionIconStrip(
                    trigger,
                    listOf(SingleExpenseAction.Back),
                    listOf(SingleExpenseAction.Delete),
                    listOf(SingleExpenseAction.ReloadExpense)
                )

                ActionIcon(trigger, SingleExpenseAction.Logout)
            }
        }

        Box {
            with(expense) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(20.dp),
                    modifier = Modifier.verticalScroll(scrollState).align(Alignment.TopStart).padding(20.dp)
                ) {

                    FormField(attribute = title, onValueChange = { trigger(SingleExpenseAction.SetTitle(it)) })
                    FormField(
                        attribute = description,
                        onValueChange = { trigger(SingleExpenseAction.SetDescription(it)) })
                    FormField(attribute = date, onValueChange = { trigger(SingleExpenseAction.SetDate(it)) })
                    SelectionFormField(
                        attribute = objectType,
                        allItems = ObjectType.values().toList(),
                        onItemClick = { trigger(SingleExpenseAction.SetObjectType(it)) })
                    SelectionFormField(
                        attribute = costCentre,
                        allItems = CostCentre.values().toList(),
                        onItemClick = { trigger(SingleExpenseAction.SetCostCentre(it)) })
                    SelectionFormField(
                        attribute = service,
                        allItems = Service.values().toList(),
                        onItemClick = { trigger(SingleExpenseAction.SetService(it)) })
                    FormField(attribute = amount, onValueChange = { trigger(SingleExpenseAction.SetAmount(it)) })
                    SelectionFormField(
                        attribute = currency,
                        allItems = Currency.values().toList(),
                        onItemClick = { trigger(SingleExpenseAction.SetCurrency(it)) })
                    //TODO: upload receipts
                    if (user.supervisor){
                        BooleanFormField(attribute = approved, onValueChange = { trigger(SingleExpenseAction.SetApproved(it)) }, enabled = true)
                    }

                    Spacer(Modifier.height(20.dp))

                    ActionButton(trigger, SingleExpenseAction.Update)
                }
            }

            VerticalScrollbar(adapter = ScrollbarAdapter(scrollState), modifier = Modifier.align(Alignment.CenterEnd))
        }
    }
}

@Composable
fun SingleExpenseUiReadOnly(expense: ExpenseFormModel, trigger: (SingleExpenseAction) -> Unit) {
    val scrollState = rememberScrollState()

    Column(modifier = Modifier.background(Color.White)) {
        Toolbar {
            AlignLeftRight {
                ActionIconStrip(
                    trigger,
                    listOf(SingleExpenseAction.Back),
                    listOf(SingleExpenseAction.ReloadExpense)
                )

                ActionIcon(trigger, SingleExpenseAction.Logout)
            }
        }

        Box {
            with(expense) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(20.dp),
                    modifier = Modifier.verticalScroll(scrollState).align(Alignment.TopStart).padding(20.dp)
                ) {

                    ReadOnlyFormField(attribute = title)
                    ReadOnlyFormField(attribute = description)
                    ReadOnlyFormField(attribute = date)
                    ReadOnlyFormField(attribute = objectType)
                    ReadOnlyFormField(attribute = costCentre)
                    ReadOnlyFormField(attribute = service)
                    ReadOnlyFormField(attribute = amount)
                    ReadOnlyFormField(attribute = currency)
                    //TODO: show receipts
                    BooleanFormField(attribute = released, onValueChange = {}, enabled = false)
                    BooleanFormField(attribute = approved, onValueChange = {}, enabled = false)
                }
            }

            VerticalScrollbar(adapter = ScrollbarAdapter(scrollState), modifier = Modifier.align(Alignment.CenterEnd))
        }
    }
}