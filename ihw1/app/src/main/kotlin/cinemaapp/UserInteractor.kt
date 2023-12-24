package cinemaapp

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class UserInteractor(private val registry: StaffRegistry) {
    private enum class UserState { ChooseOption, SignIn, SignUp, LoggedIn, ExitRequested }

    enum class UserCommand { SellTicket, ReturnTicket, ListSeats, AddFilm, EditFilm, MarkTakenSeat, AddSession, DeleteSession, ListSessions, Exit }

    private var currentState: UserState = UserState.ChooseOption

    fun loginOrRegisterUser(): Boolean {
        while (true) {
            currentState = when (currentState) {
                UserState.ChooseOption -> chooseSignInUp()
                UserState.SignIn -> signInUser()
                UserState.SignUp -> signUpUser()
                UserState.LoggedIn -> return true
                UserState.ExitRequested -> return false
            }
        }
    }

    fun nextCommand(): UserCommand {
        while (true) {
            print("Write number of the option you want to peek:\n" +
                    "     1. Sell the ticket to the customer\n" +
                    "     2. Return ticket\n" +
                    "     3. List all taken seats\n" +
                    "     4. Add new film\n" +
                    "     5. Edit film\n" +
                    "     6. Mark taken seat\n" +
                    "     7. Add new session\n" +
                    "     8. Delete session\n" +
                    "     9. List all sessions\n" +
                    "     10. Exit\n" +
                    "> ")
            System.out.flush()

            val userInput: String? = readLine()
            when (userInput) {
                "1" -> return UserCommand.SellTicket
                "2" -> return UserCommand.ReturnTicket
                "3" -> return UserCommand.ListSeats
                "4" -> return UserCommand.AddFilm
                "5" -> return UserCommand.EditFilm
                "6" -> return UserCommand.MarkTakenSeat
                "7" -> return UserCommand.AddSession
                "8" -> return UserCommand.DeleteSession
                "9" -> return UserCommand.ListSessions
                "10" -> return UserCommand.Exit
            }

            println("Unknown option, expected number from 1 to 10. Please, try again")
        }
    }

    fun notifyExit() = println("Exit requested by user")

    fun notify(prompt: String) = println(prompt)

    fun requestPositiveInt(prompt: String): Int = requestIntAtLeast(1, prompt)

    fun requestIntAtLeast(minValue: Int, prompt: String): Int {
        val msg = "$prompt (input integer number >= $minValue)\n> "
        while (true) {
            print(msg)
            System.out.flush()

            val num: Int? = readInt()
            if (num != null && num >= minValue) {
                return num
            }

            println("Incorrect input. Please, try again")
        }
    }

    fun requestNonEmptyString(prompt: String): String {
        val msg = "$prompt\n> "
        while (true) {
            print(msg)
            System.out.flush()

            val input: String? = readLine()
            if (!input.isNullOrEmpty()) {
                return input
            }

            println("Empty input. Please, try again")
        }
    }

    fun requestString(prompt: String): String {
        val msg = "$prompt\n> "
        while (true) {
            print(msg)
            System.out.flush()

            val input: String? = readLine()
            if (input != null) {
                return input
            }

            println("Empty input. Please, try again")
        }
    }

    fun requestIntBetween(minValue: Int, maxValue: Int, prompt: String): Int {
        val msg = "$prompt (input integer between $minValue and $maxValue)\n> "
        while (true) {
            print(msg)
            System.out.flush()

            val input: Int? = readInt()
            if (input != null && minValue <= input && input <= maxValue) {
                return input
            }

            println("Incorrect input. Please, try again")
        }
    }

    fun requestDate(prompt: String): LocalDateTime? {
        val date: String = requestNonEmptyString("$prompt\n(in format yyyy-MM-dd HH:mm:ss, for example, 2023-01-31 20:30:45)")
        return try {
            LocalDateTime.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
        } catch (ex: Exception) {
            null
        }
    }

    private fun chooseSignInUp(): UserState {
        while (true) {
            print("Write number of the option you want to peek:\n" +
                    "     1. Sign in (using existing login)\n" +
                    "     2. Sign up (create new user)\n" +
                    "     3. Exit\n" +
                    "> ")
            System.out.flush()

            val userInput: String? = readLine()
            if (userInput != null && userInput.length == 1) {
                when (userInput[0]) {
                    '1' -> return UserState.SignIn
                    '2' -> return UserState.SignUp
                    '3' -> return UserState.ExitRequested
                }
            }

            println("Number 1, 2 or 3 was expected. Please, try again")
        }
    }

    private fun signInUser(): UserState {
        while (true) {
            print("Enter login or press Enter to return:\n> ")
            System.out.flush()

            val login: String? = readLine()

            if (login.isNullOrEmpty()) {
                return UserState.ChooseOption
            }

            if (registry.isRegistered(login)) {
                return requestCorrectPassword(login)
            }

            println("User not found. Try again")
        }
    }

    private fun requestCorrectPassword(login: String): UserState {
        while (true) {
            print("Enter password or press Enter to return:\n> ")
            System.out.flush()

            val password: String? = readLine()
            if (password.isNullOrEmpty()) {
                return UserState.ChooseOption
            }

            if (registry.verifyPassword(login, password)) {
                return UserState.LoggedIn
            }

            println("Incorrect password. Please, try again")
        }
    }

    private fun signUpUser(): UserState {
        print("Write name for the new user or press Enter to return:\n> ")
        System.out.flush()

        val username: String? = readLine()
        if (username.isNullOrEmpty()) {
            return UserState.ChooseOption
        }

        if (registry.isRegistered(username)) {
            println("User with this name already exists, redirecting to signing in")
            return signInUser()
        }

        print("Write a new password or press Enter to return:\n> ")
        val password: String? = readLine()
        if (password.isNullOrEmpty()) {
            return UserState.ChooseOption
        }

        var checkPassword: String?
        while (true) {
            print("Write a this password again or press Enter to return:\n> ")
            System.out.flush()

            checkPassword = readLine()
            if (checkPassword.isNullOrEmpty()) {
                return UserState.ChooseOption
            }

            if (password == checkPassword) {
                break
            }

            print("Passwords do not match. Please, try again")
        }

        registry.registerUser(username, password)
        return UserState.LoggedIn
    }

    private fun readInt(): Int? {
        return try {
            readLine()?.toInt()
        } catch (ex: Exception) {
            null
        }
    }
}
