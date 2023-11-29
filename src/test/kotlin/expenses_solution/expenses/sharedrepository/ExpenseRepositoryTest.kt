package expenses_solution.expenses.sharedrepository

import expenses_solution.expenses.shareddata.*
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import xtracted.repository.urlFromResources
import java.time.LocalDate

class ExpenseRepositoryTest {
    private val url = "/data/spesenDB".urlFromResources()
    private lateinit var repository: ExpenseRepository

    @BeforeEach
    fun setup(){
        repository = ExpenseRepository(url)
        repository.deleteAllExpenses()
    }

    @Test
    fun testCreate(){
        //given
        val initial = Expense(id = -999, userId = 1)

        //when
        repository.create(initial)

        //then
        assertEquals(1, repository.countExpenses())
    }

    @Test
    fun testRead(){
        //given
        val expense = repository.create(Expense(id=-999, userId = 1))

        //when
        val reportFromDB = repository.readExpense(expense.id)

        //then
        assertEquals(expense, reportFromDB)
        Assertions.assertNull(repository.readExpense(999))
    }

    @Test
    fun testReadAllReports(){
        //given
        val expense = repository.create(Expense(id= -999, userId = 1))
        repository.create(Expense(id = -999, userId = 2))

        //when
        val allExpenses = repository.readAllExpenses(1)

        //then
        assertEquals(1, allExpenses.size)
        assertEquals(expense, allExpenses[0])

        assertTrue(repository.readAllExpenses(999).isEmpty())
    }

    @Test
    fun testUpdateReport(){
        //given
        val expense      = repository.create(Expense(id=-999, userId = 1))
        val title = "Title"
        val description = "Description"
        val newDate     = LocalDate.now()
        val objectType = ObjectType.KOSTENSTELLE
        val costCentre = CostCentre.UNDEFINED
        val service = Service.SONSTIGES
        val amount = 25.0f
        val currency = Currency.CHF
        val released = false
        val newApproved = true

        val updatedExpense = expense.copy(
            title = title,
            description = description,
            date      = newDate,
            objectType = objectType,
            costCentre = costCentre,
            service = service,
            amount = amount,
            currency = currency,
            released = released,
            approved  = newApproved)

        //when
        repository.updateExpense(updatedExpense)

        //then
        assertEquals(updatedExpense, repository.readExpense(updatedExpense.id))
    }

    @Test
    fun testDeleteReport(){
        //given
        val expense = repository.create(Expense(id=-999, userId = 1))

        //when
        repository.deleteExpense(expense.id)

        //then
        Assertions.assertNull(repository.readExpense(expense.id))
    }
}