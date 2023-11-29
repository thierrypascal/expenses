package expenses_solution.expenses.part_singleexpense

import expenses_solution.expenses.part_singleexpense.controller.SingleExpenseAction
import expenses_solution.expenses.part_singleexpense.controller.SingleExpenseController
import expenses_solution.expenses.shareddata.*
import expenses_solution.expenses.sharedrepository.ExpenseRepository
import expenses_solution.login.repository.UserRepository
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import shareddata.User
import xtracted.repository.urlFromResources
import java.time.LocalDate
import kotlin.math.cos

class SingleExpenseControllerTest {
    private val url = "/data/spesenDB".urlFromResources()
    private lateinit var expenseRepo: ExpenseRepository
    private lateinit var expense: Expense
    private lateinit var userRepo: UserRepository
    private lateinit var user: User
    private lateinit var singleExpenseController: SingleExpenseController

    @BeforeEach
    fun setup(){
        expenseRepo = ExpenseRepository(url)
        expenseRepo.deleteAllExpenses()
        userRepo = UserRepository(url)
        userRepo.deleteAllUsers()

        //create user and expense
        user    = userRepo.create("username", "1234", false)
        expense = expenseRepo.create(Expense(
            id          =-999,
            userId      = user.id,
            title       = "Title",
            description = "Description",
            date        = LocalDate.now(),
            objectType  = ObjectType.KOSTENSTELLE,
            costCentre  = CostCentre.UNDEFINED,
            service     = Service.SONSTIGES,
            amount      = 25.0f,
            currency    = Currency.CHF,
            released    = false,
            approved    = false
        ))

        singleExpenseController = SingleExpenseController(user, expense.id, expenseRepo, {}, {})
    }

    @Test
    fun testSetTitle(){
        //when
        singleExpenseController.trigger(SingleExpenseAction.SetTitle("NewTitle"))

        //then
        with(singleExpenseController.state.currentExpense){
            assertEquals("NewTitle", title.value)
            assertEquals("NewTitle", title.valueAsText)
            assertTrue(title.isValid)
        }

        //when
        singleExpenseController.trigger(SingleExpenseAction.SetTitle(""))

        //then
        with(singleExpenseController.state.currentExpense){
            assertEquals("NewTitle", title.value)
            assertEquals("", title.valueAsText)
            assertFalse(title.isValid)
        }
    }

    @Test
    fun testSetDescription(){
        //when
        singleExpenseController.trigger(SingleExpenseAction.SetDescription("NewDescription"))

        //then
        with(singleExpenseController.state.currentExpense){
            assertEquals("NewDescription", description.value)
            assertEquals("NewDescription", description.valueAsText)
            assertTrue(description.isValid)
        }

        //when
        singleExpenseController.trigger(SingleExpenseAction.SetDescription(""))

        //then
        with(singleExpenseController.state.currentExpense){
            assertEquals("NewDescription", description.value)
            assertEquals("", description.valueAsText)
            assertFalse(description.isValid)
        }
    }

    @Test
    fun testSetDate(){
        //when
        singleExpenseController.trigger(SingleExpenseAction.SetDate("19.07.22"))

        //then
        with(singleExpenseController.state.currentExpense){
            assertEquals(LocalDate.of(2022, 7, 19), date.value)
            assertEquals("19.07.22", date.valueAsText)
            assertTrue(date.isValid)
        }

        //when
        singleExpenseController.trigger(SingleExpenseAction.SetDate("19ab22"))

        //then
        with(singleExpenseController.state.currentExpense){
            assertEquals(LocalDate.of(2022, 7, 19), date.value)
            assertEquals("19ab22", date.valueAsText)
            assertFalse(date.isValid)
        }
    }

    @Test
    fun testSetObjectType(){
        //when
        singleExpenseController.trigger(SingleExpenseAction.SetObjectType(ObjectType.PSP_ELEMENT))

        //then
        with(singleExpenseController.state.currentExpense){
            assertEquals(ObjectType.PSP_ELEMENT, objectType.value)
            assertEquals("PSP_ELEMENT", objectType.valueAsText)
            assertTrue(objectType.isValid)
        }
    }

    @Test
    fun testSetCostCentre(){
        //when
        singleExpenseController.trigger(SingleExpenseAction.SetCostCentre(CostCentre.Z404))

        //then
        with(singleExpenseController.state.currentExpense){
            assertEquals(CostCentre.Z404, costCentre.value)
            assertEquals("Z404", costCentre.valueAsText)
            assertTrue(costCentre.isValid)
        }
    }

    @Test
    fun testSetService(){
        //when
        singleExpenseController.trigger(SingleExpenseAction.SetService(Service.VERPFLEGUNG))

        //then
        with(singleExpenseController.state.currentExpense){
            assertEquals(Service.VERPFLEGUNG, service.value)
            assertEquals("VERPFLEGUNG", service.valueAsText)
            assertTrue(service.isValid)
        }
    }

    @Test
    fun testSetAmount(){
        //when
        singleExpenseController.trigger(SingleExpenseAction.SetAmount("55.5"))

        //then
        with(singleExpenseController.state.currentExpense){
            assertEquals(55.5f,  amount.value)
            assertEquals("55.5", amount.valueAsText)
            assertTrue(amount.isValid)
        }

        //when
        singleExpenseController.trigger(SingleExpenseAction.SetAmount("abc"))

        //then
        with(singleExpenseController.state.currentExpense){
            assertEquals(55.5f, amount.value)
            assertEquals("abc", amount.valueAsText)
            assertFalse(amount.isValid)
        }
    }

    @Test
    fun testSetCurrency(){
        //when
        singleExpenseController.trigger(SingleExpenseAction.SetCurrency(Currency.USD))

        //then
        with(singleExpenseController.state.currentExpense){
            assertEquals(Currency.USD, currency.value)
            assertEquals("USD", currency.valueAsText)
            assertTrue(currency.isValid)
        }
    }

    @Test
    fun testSetApproved(){
        //when
        singleExpenseController.trigger(SingleExpenseAction.SetApproved(true))

        //then
        with(singleExpenseController.state.currentExpense){
            assertEquals(true, approved.value)
            assertEquals("approved", approved.valueAsText)
            assertTrue(approved.isValid)
        }
    }

    @Test
    fun testSetReleased(){
        //when
        singleExpenseController.trigger(SingleExpenseAction.SetReleased(true))

        //then
        with(singleExpenseController.state.currentExpense){
            assertEquals(true, released.value)
            assertEquals("released", released.valueAsText)
            assertTrue(released.isValid)
        }
    }
}