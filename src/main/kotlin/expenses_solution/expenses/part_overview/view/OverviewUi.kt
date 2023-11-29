package expenses_solution.expenses.part_overview.view

import java.time.format.DateTimeFormatter
import androidx.compose.foundation.ScrollbarAdapter
import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Divider
import androidx.compose.material.Switch
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.ApplicationScope
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.rememberWindowState
import expenses_solution.expenses.part_overview.controller.OverviewAction
import expenses_solution.expenses.part_overview.controller.OverviewState
import expenses_solution.expenses.shareddata.Expense
import shareddata.User
import xtracted.view.*

@Composable
fun ApplicationScope.OverviewWindow(state: OverviewState, trigger: (OverviewAction) -> Unit) {
    Window(title          = state.title,
           onCloseRequest = { exitApplication() },
           state          = rememberWindowState(width    = 400.dp,
                                                height   = 580.dp,
                                                position = WindowPosition(Alignment.Center))) {
        OverviewUi(state.user, state.allExpenses, trigger)
    }
}

@Composable
fun OverviewUi(user: User, reports: List<Expense>, trigger: (OverviewAction) -> Unit) {
    Column(modifier = Modifier.fillMaxSize().background(Color.White)) {
        Toolbar {
            AlignLeftRight {
                ActionIconStrip(trigger = trigger,
                                listOf(OverviewAction.Create),
                                listOf(OverviewAction.ReloadAllExpenses))

                ActionIconStrip(trigger = trigger,
                    listOf(OverviewAction.MonthlyRelease),
                    listOf(OverviewAction.Logout)
                )
            }
        }

        Text(text = "Expense Management of '${user.name}'",
             fontSize = 16.sp,
             fontWeight = FontWeight.ExtraLight,
             textAlign = TextAlign.Center,
             modifier = Modifier.padding(vertical = 25.dp).fillMaxWidth()
            )


        Box(modifier         = Modifier.padding(5.dp)
            .weight(1.0f)
            .fillMaxWidth(),
            contentAlignment = Alignment.Center){

            if(reports.isEmpty()){
                NoExpenses()
            }
            else {
                ExpensesTable(reports, trigger)
            }
        }
    }
}

@Composable
private fun NoExpenses() {
    Column(horizontalAlignment = Alignment.CenterHorizontally,
           verticalArrangement = Arrangement.spacedBy(20.dp)) {
        Text(text       = "No expenses yet",
             fontWeight = FontWeight.ExtraLight,
             fontSize   = 24.sp)
        VulcanSalute()
    }
}

@Composable
private fun ExpensesTable(reports: List<Expense>, trigger: (OverviewAction) -> Unit) {
    Column(Modifier.padding(start = 9.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.height(35.dp)

                .padding(end = 9.dp)
                .border(0.5.dp, Color.LightGray)
                .background(Color.LightGray)
           ) {
            HeaderCell("Date")
            HeaderCell("Title")
            HeaderCell("Amount")
            HeaderCell("Currency")
            HeaderCell("Cost Centre")
            HeaderCell("Released")
            HeaderCell("Approved")
        }
        Box(Modifier.fillMaxSize()){
            val scrollState = rememberLazyListState()
            LazyColumn(state = scrollState,
                       modifier = Modifier.fillMaxSize()
                                          .align(Alignment.CenterStart)
                                          .padding(end = 9.dp)) {
                items(reports) {
                    Row(verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.clickable { trigger(OverviewAction.SelectExpense(it.id)) }
                                           .height(IntrinsicSize.Max)) {
                        TableCell { Text(it.date.format(DateTimeFormatter.ofPattern("d.M.YY"))) }
                        TableCell { Text(it.title) }
                        TableCell { Text(it.amount.toString()) }
                        TableCell { Text(it.currency.toString()) }
                        TableCell { Text(it.costCentre.toString()) }
                        TableCell { Switch(checked = it.released,
                                           onCheckedChange = {},
                                           enabled = false) }
                        TableCell { Switch(checked = it.approved,
                                           onCheckedChange = {},
                                           enabled = false) }
                    }
                    Divider()
                }
            }
            VerticalScrollbar(adapter  = ScrollbarAdapter(scrollState),
                              modifier = Modifier.align(Alignment.CenterEnd))
        }

    }
}

@Composable
private fun RowScope.HeaderCell(header: String, weight : Float = 1.0f){
    TableCell(weight = weight, borderColor = Color.White) {
        Text(header, color = Color.White)
    }
}

@Composable
private fun RowScope.TableCell(weight : Float = 1.0f, borderColor : Color = Color.LightGray, content: @Composable () -> Unit) {
    Box(Modifier.border(0.5.dp, borderColor)
            .padding(5.dp)
            .weight(weight)
            .fillMaxHeight(),
        contentAlignment = Alignment.Center){
        content()
    }
}