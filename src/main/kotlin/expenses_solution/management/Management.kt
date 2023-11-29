package expenses_solution.management

import androidx.compose.ui.window.application
import expenses_solution.management.controller.ManagementController
import expenses_solution.management.view.ManagementWindow
import expenses_solution.expenses.sharedrepository.ExpenseRepository
import expenses_solution.login.repository.UserRepository
import xtracted.repository.urlFromResources
import xtracted.repository.urlFromWorkingDirectory
import java.util.logging.Level
import java.util.logging.LogManager
import java.util.logging.Logger

fun main() {
    LogManager.getLogManager().getLogger(Logger.GLOBAL_LOGGER_NAME).level = Level.INFO

     val url = "/data/spesenDB".urlFromWorkingDirectory()
    // val url = "/data/spesenDB".urlFromResources()

    val userRepository = UserRepository(url)
    val workReportRepository = ExpenseRepository(url)

    val controller =  ManagementController(userRepository, workReportRepository)

    application {
        ManagementWindow(controller.state)
    }
}