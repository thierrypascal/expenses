package expenses_solution.management.controller

import shareddata.User
import xtracted.controller.Controller


data class ManagementState(val title: String,
                           val activeController: Controller<*>,
                           val user: User?
                           )