package expenses_solution.expenses.sharedrepository

import expenses_solution.expenses.shareddata.*
import java.sql.ResultSet
import java.time.LocalDate
import expenses_solution.expenses.sharedrepository.ExpenseRepository.Column.APPROVED
import expenses_solution.expenses.sharedrepository.ExpenseRepository.Column.RELEASED
import expenses_solution.expenses.sharedrepository.ExpenseRepository.Column.TITLE
import expenses_solution.expenses.sharedrepository.ExpenseRepository.Column.DATE
import expenses_solution.expenses.sharedrepository.ExpenseRepository.Column.ID
import expenses_solution.expenses.sharedrepository.ExpenseRepository.Column.USER_ID
import expenses_solution.expenses.sharedrepository.ExpenseRepository.Column.DESCRIPTION
import expenses_solution.expenses.sharedrepository.ExpenseRepository.Column.OBJECT_TYPE
import expenses_solution.expenses.sharedrepository.ExpenseRepository.Column.COST_CENTRE
import expenses_solution.expenses.sharedrepository.ExpenseRepository.Column.SERVICE
import expenses_solution.expenses.sharedrepository.ExpenseRepository.Column.AMOUNT
import expenses_solution.expenses.sharedrepository.ExpenseRepository.Column.CURRENCY
import xtracted.repository.*

class ExpenseRepository(val url: String) {
    private enum class Column {
        ID, USER_ID, TITLE, DESCRIPTION, DATE, OBJECT_TYPE, COST_CENTRE, SERVICE, AMOUNT, CURRENCY, APPROVED, RELEASED
    }

    private val table = "EXPENSES"

    fun create(initial: Expense): Expense {
        val insertStmt = """INSERT INTO $table 
                            |            ($USER_ID,          $TITLE,                    ${DESCRIPTION},                 $DATE,                   $OBJECT_TYPE,                       $COST_CENTRE,                       $SERVICE,                        $AMOUNT,           $CURRENCY,                        $APPROVED,           $RELEASED)
                            |    VALUES  (${initial.userId}, ${initial.title.asSql()},  ${initial.description.asSql()}, ${initial.date.asSql()}, ${initial.objectType.name.asSql()}, ${initial.costCentre.name.asSql()}, ${initial.service.name.asSql()}, ${initial.amount}, ${initial.currency.name.asSql()}, ${initial.approved}, ${initial.released})""".trimMargin()

        val id = insertAndCreateKey(url        = url,
                                    insertStmt = insertStmt)

        return initial.copy(id = id)
    }

    fun readExpense(id: Int): Expense?  =
        readFirst(url   = url,
                  table = table,
                  where = "$ID = $id",
                  map   = { asExpense() })

    fun readAllExpenses(userId: Int): List<Expense> =
        readAll(url     = url,
                table   = table,
                where   = "$USER_ID = $userId",
                map     = { asExpense()})

    fun updateExpense(report: Expense) =
        update(url         = url,
              table        = table,
              id           = report.id,
              setStatement = """SET $TITLE = ${report.title.asSql()},
                  |$DESCRIPTION = ${report.description.asSql()},
                  |$DATE        = ${report.date.asSql()},
                  |$OBJECT_TYPE = ${report.objectType.name.asSql()},
                  |$COST_CENTRE = ${report.costCentre.name.asSql()},
                  |$SERVICE     = ${report.service.name.asSql()},
                  |$AMOUNT      = ${report.amount},
                  |$CURRENCY    = ${report.currency.name.asSql()},
                  |$APPROVED    = ${report.approved},
                  |$RELEASED    = ${report.released}""".trimMargin())

    fun deleteExpense(id: Int) =
        delete(url   = url,
               table = table,
               id    = id)

    fun deleteAllExpenses() =
        deleteAll(url = url,
                  table = table)

    fun countExpenses(): Int =
        count(url   = url,
              table = table)


    private fun ResultSet.asExpense() = Expense(
        id          = getInt(1),
        userId      = getInt(2),
        title       = getString(3),
        description = getString(4),
        date        = LocalDate.parse(getString(5)),
        objectType  = ObjectType.valueOf(getString(6)),
        costCentre  = CostCentre.valueOf(getString(7)),
        service     = Service.valueOf(getString(8)),
        amount      = getFloat(9),
        currency    = Currency.valueOf(getString(10)),
        approved    = getBoolean(11),
        released    = getBoolean(12)
    )
}

/*

 CREATE TABLE EXPENSES (
   ID                   INTEGER       PRIMARY KEY AUTOINCREMENT,
   USER_ID              INTEGER,
   TITLE                VARCHAR(100),
   DESCRIPTION          VARCHAR(200),
   DATE                 DATE,
   OBJECT_TYPE          VARCHAR(50),
   COST_CENTRE          VARCHAR(50),
   SERVICE              VARCHAR(50),
   AMOUNT               REAL,
   CURRENCY             VARCHAR(3),
   APPROVED             BOOLEAN,
   RELEASED             BOOLEAN
 );

 */