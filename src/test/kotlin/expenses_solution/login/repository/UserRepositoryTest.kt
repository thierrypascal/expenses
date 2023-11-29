package module06.codekitchentime_solution.login.repository

import expenses_solution.login.repository.UserRepository
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import xtracted.repository.urlFromResources

class UserRepositoryTest {
    private lateinit var repository: UserRepository

    @BeforeEach
    fun setup(){
        val url = "/data/spesenDB".urlFromResources()
        repository = UserRepository(url)
    }

    @AfterEach
    fun tearDown(){
        repository.deleteAllUsers()
    }

    @Test
    fun testCount() {
        //initial
        assertEquals(0, repository.countUsers())

        //when
        repository.create("a.b@fhnw.ch", "1234", true)

        //then
        assertEquals(1, repository.countUsers())
    }

    @Test
    fun testCreate(){
        //when
        repository.create("a.b@fhnw.ch", "1234", true)
        repository.create("a.b@fhnw.ch", "1234", true)
        repository.create("I'am.Groot@fhnw.ch", "I've forgotten", true)

        //then
        assertEquals(3, repository.countUsers())
    }

    @Test
    fun testReadByNameAndPassword(){
        //given
        val user = repository.create("a.b@fhnw.ch", "1234", true)

        //when
        val userFromDB = repository.readUser(user.name, user.password)

        //then
        assertEquals(user, userFromDB)
    }

    @Test
    fun testReadById() {
        //given
        val user = repository.create("a.b@fhnw.ch", "1234", true)

        //when
        val userFromDB = repository.readUser(user.id)

        //then
        assertEquals(user, userFromDB)
        assertNull(repository.readUser(999))
    }

    @Test
    fun testReadByIdWithSpecialStrings(){
        //given
        val name = "I'am.Groot@fhnw.ch"
        val password = "I've forgotten"
        val student = true
        val user = repository.create(name, password, student)

        //when
        val userOnDB = repository.readUser(user.id)

        //then
        assertNotNull(userOnDB)
        userOnDB?.let {
            assertEquals(name, userOnDB.name)
            assertEquals(password, userOnDB.password)
            assertEquals(student, userOnDB.supervisor)
        }

    }

    @Test
    fun testReadAll(){
        //given
        val user1 = repository.create("a.b@fhnw.ch", "1234", true)
        val user2 = repository.create("c.d@fhnw.ch", "5678", true)

        //when
        val allUsers = repository.readAllUsers()

        //then
        assertEquals(2, allUsers.size)
        assertTrue(allUsers.contains(user1))
        assertTrue(allUsers.contains(user2))
    }

    @Test
    fun testDeleteUser(){
        //given
        val user = repository.create("a.b@fhnw.ch", "1234", true)

        //when
        repository.deleteUser(user.id)

        //then
        assertNull(repository.readUser(user.id))

    }

    @Test
    fun testDeleteAll() {
        //given
        val user1 = repository.create("a.b@fhnw.ch", "1234", true)
        val user2 = repository.create("c.d@fhnw.ch", "5678", true)

        //when
        repository.deleteAllUsers()

        //then
        assertEquals(0, repository.countUsers())
        assertNull(repository.readUser(user1.id))
        assertNull(repository.readUser(user2.id))
    }

    @Test
    fun testUserNameExists(){
        //given
        val userName = "a.b@fhnw.ch"
        val user = repository.create("a.b@fhnw.ch", "1234", true)

        //then
        assertTrue(repository.nameExists(userName))
        assertFalse(repository.nameExists("unknown Name"))
    }
}