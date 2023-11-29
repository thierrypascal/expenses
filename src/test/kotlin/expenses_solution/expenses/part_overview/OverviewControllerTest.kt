package expenses_solution.expenses.part_overview

import expenses_solution.expenses.part_overview.controller.OverviewAction
import expenses_solution.expenses.part_overview.controller.OverviewController
import expenses_solution.expenses.shareddata.Expense
import expenses_solution.expenses.sharedrepository.ExpenseRepository
import expenses_solution.login.repository.UserRepository
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import shareddata.User
import xtracted.repository.urlFromResources

class OverviewControllerTest {
    private val url = "/data/spesenDB".urlFromResources()
    private lateinit var expenseRepo: ExpenseRepository
    private lateinit var userRepo: UserRepository
    private lateinit var overviewController: OverviewController
    private lateinit var user: User

    @BeforeEach
    fun setup(){
        expenseRepo = ExpenseRepository(url)
        expenseRepo.deleteAllExpenses()
        userRepo = UserRepository(url)
        userRepo.deleteAllUsers()

        //create user
        user = userRepo.create("username", "1234", false)

        overviewController = OverviewController(user, expenseRepo, {}, {}, {})
    }

    @Test
    fun testMonthlyRelease(){
        //given
        val e1 = expenseRepo.create(Expense(id=-999, userId = user.id))
        val e2 = expenseRepo.create(Expense(id=-998, userId = user.id))

        //when
        overviewController.trigger(OverviewAction.MonthlyRelease)

        //then
        with(overviewController.state){
            assertTrue(allExpenses[0].released)
            assertTrue(allExpenses[1].released)
        }
    }
}