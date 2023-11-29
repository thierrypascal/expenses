package expenses_solution.expenses.shareddata

import java.time.LocalDate

data class Expense(val id: Int,
                   val userId: Int,
                   val title: String = "",
                   val description: String = "",
                   val date: LocalDate    = LocalDate.now(),
                   val objectType: ObjectType = ObjectType.KOSTENSTELLE,
                   val costCentre: CostCentre = CostCentre.UNDEFINED,
                   val service: Service = Service.SONSTIGES,
                   val amount: Float = 0.0f,
                   val currency: Currency = Currency.CHF,
                   val approved: Boolean  = false,
                   val released: Boolean  = false
)
