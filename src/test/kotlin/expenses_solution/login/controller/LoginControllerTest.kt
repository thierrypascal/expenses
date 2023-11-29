package module06.codekitchentime_solution.login.controller

import expenses_solution.login.controller.LoginAction
import expenses_solution.login.controller.LoginController
import expenses_solution.login.controller.LoginState
import expenses_solution.login.repository.UserRepository
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import xtracted.repository.urlFromResources

class LoginControllerTest{

    private lateinit var repository: UserRepository
    private lateinit var controller: LoginController

    @BeforeEach
    fun setup(){
        val url = "/data/spesenDB".urlFromResources()
        repository = UserRepository(url)
        controller = LoginController(repository)
    }

    @AfterEach
    fun tearDown(){
        repository.deleteAllUsers()
    }

    @Test
    fun initial(){
        //then
        with(controller.state){
            assertNull(user)
            assertEquals(LoginState.Page.LOGIN, currentPage)
        }
    }

    @Test
    fun testLoginKnownUser(){
        //given
        val mail = "a.b@fhnw.ch"
        val pwd  = "123"
        val userOnDB = repository.create(mail, pwd, false)

        controller.trigger(LoginAction.SetUserName(mail))
        controller.trigger(LoginAction.SetPassword(pwd))

        //when
        controller.trigger(LoginAction.Login(true))

        //then
        with(controller.state){
            assertEquals(userOnDB, user)
            assertEquals(LoginState.Page.LOGGED_IN, currentPage)
            assertTrue(loginFormModel.errorMessage.isEmpty())
        }
    }

    @Test
    fun testLoginKnownUserWrongPasswort(){
        //given
        val mail = "a.b@fhnw.ch"
        val pwd  = "123"

        repository.create(mail, pwd, false)

        controller.trigger(LoginAction.SetUserName(mail))
        controller.trigger(LoginAction.SetPassword("456"))

        //when
        controller.trigger(LoginAction.Login(true))

        //then
        with(controller.state){
            assertNull(user)
            assertEquals(LoginState.Page.LOGIN, currentPage)
            assertTrue(loginFormModel.errorMessage.startsWith("Wrong"))
        }
    }


    @Test
    fun testLoginUnknownUser(){
        //given
        val mail = "a.b@fhnw.ch"
        val pwd  = "123"

        controller.trigger(LoginAction.SetUserName(mail))
        controller.trigger(LoginAction.SetPassword(pwd))

        //when
        controller.trigger(LoginAction.Login(true))

        //then
        with(controller.state){
            assertNull(user)
            assertEquals(LoginState.Page.LOGIN, currentPage)
            assertTrue(loginFormModel.errorMessage.startsWith("Unknown"))
        }
    }

    @Test
    fun testRegisterUser(){
        //given
        val mail = "a.b@fhnw.ch"
        val pwd  = "123"

        controller.trigger(LoginAction.SwitchToRegister)

        controller.trigger(LoginAction.SetUserName(mail))
        controller.trigger(LoginAction.SetPassword(pwd))
        controller.trigger(LoginAction.SetRetypedPassword(pwd))
        controller.trigger(LoginAction.SetSupervisor(false))

        //when
        controller.trigger(LoginAction.Register(true))

        //then
        with(controller.state){
            assertNotNull(user)
            assertEquals(user, repository.readUser(user!!.id))
            assertEquals(LoginState.Page.LOGGED_IN, currentPage)
            assertTrue(loginFormModel.errorMessage.isEmpty())
        }

    }

    @Test
    fun testRegisterExistingUser(){
        //given
        val mail = "a.b@fhnw.ch"
        val pwd  = "123"
        repository.create(mail, pwd, false)

        controller.trigger(LoginAction.SwitchToRegister)

        controller.trigger(LoginAction.SetUserName(mail))
        controller.trigger(LoginAction.SetPassword(pwd))
        controller.trigger(LoginAction.SetRetypedPassword(pwd))
        controller.trigger(LoginAction.SetSupervisor(false))

        //when
        controller.trigger(LoginAction.Register(true))

        //then
        with(controller.state){
            assertNull(user)
            assertEquals(LoginState.Page.REGISTER, currentPage)
            assertTrue(registerFormModel.errorMessage.startsWith("Username"))
        }
    }

    @Test
    fun testRegisterUserDifferentPasswords(){
        //given
        controller.trigger(LoginAction.SwitchToRegister)

        val mail = "a.b@fhnw.ch"
        val pwd  = "123"

        controller.trigger(LoginAction.SetUserName(mail))
        controller.trigger(LoginAction.SetPassword(pwd))
        controller.trigger(LoginAction.SetRetypedPassword("different pwd"))
        controller.trigger(LoginAction.SetSupervisor(false))

        //when
        controller.trigger(LoginAction.Register(true))

        //then
        with(controller.state){
            assertNull(user)
            assertEquals(LoginState.Page.REGISTER, currentPage)
            assertTrue(registerFormModel.errorMessage.startsWith("Passwords"))
        }
    }

}