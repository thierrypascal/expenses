package expenses_solution.login.repository

import java.sql.ResultSet
import expenses_solution.login.repository.UserRepository.Column.ID
import expenses_solution.login.repository.UserRepository.Column.PASSWORD
import expenses_solution.login.repository.UserRepository.Column.USERNAME
import shareddata.User
import xtracted.repository.*

class UserRepository(private val url: String) {

    private enum class Column {
        ID, USERNAME, PASSWORD, SUPERVISOR
    }

    private val table = "USER"

    fun create(mail: String, password: String, supervisor: Boolean): User {
        val insertStmt = """INSERT INTO $table 
                            |            ($USERNAME, $PASSWORD, ${Column.SUPERVISOR})
                            |    VALUES  (${mail.asSql()}, ${password.asSql()}, $supervisor)""".trimMargin()

        val id = insertAndCreateKey(url        = url,
                                    insertStmt = insertStmt)

        return User(id = id, name = mail, password = password, supervisor = supervisor)
    }

    fun readUser(id: Int) : User? =
        readFirst(url   = url,
                  table = table,
                  where = "$ID = $id",
                  map   = { asUser() })


    fun readUser(userName: String, password: String): User? =
        readFirst(url   = url,
                  table = table,
                  where = "$USERNAME = ${userName.asSql()} AND $PASSWORD = ${password.asSql()}",
                  map   = { asUser() })


    fun readAllUsers() : List<User> =
        readAll(url   = url,
                table = table,
                map   = { asUser() })


    fun nameExists(userName: String) : Boolean =
        null != readFirst(url     = url,
                          table   = table,
                          columns = "ID",
                          where   = "$USERNAME = ${userName.asSql()}",
                          map     = { this }) // kein Mapping notwendig, es ist nur interessant, ob es einen Treffer gibt

    fun deleteUser(id: Int) =
        delete(url   = url,
               table = table,
               id    = id)

    fun deleteAllUsers() =
        deleteAll(url   = url,
                  table = table)


    fun countUsers(): Int =
        count(url   = url,
              table = table)


    private fun ResultSet.asUser()  = User(id       = getInt(1),
                                           name     = getString(2),
                                           password = getString(3),
                                           supervisor  = getBoolean(4)
                                          )



}

/*
CREATE TABLE USER (
   ID                   INTEGER       PRIMARY KEY AUTOINCREMENT,
   FHNW_MAIL            VARCHAR(150),
   PASSWORD             VARCHAR(20),
   SUPERVISOR           BOOLEAN
 );
 */
