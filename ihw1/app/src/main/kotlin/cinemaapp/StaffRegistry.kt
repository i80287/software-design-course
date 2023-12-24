package cinemaapp

import java.util.HashMap
import kotlinx.serialization.Serializable
import java.security.MessageDigest
import java.util.Base64

private const val STAFF_REGISTRY_FILESTORAGE = "staff_reg.json"

class StaffRegistry : Registry() {
    @Serializable
    data class StaffUser(val username: String, val password: String)

    private val users: HashMap<String, StaffUser> = deserialize<HashMap<String, StaffUser>>(STAFF_REGISTRY_FILESTORAGE) ?: HashMap()

    private val md: MessageDigest = MessageDigest.getInstance("SHA-256")

    override fun saveToFile() {
        serialize(STAFF_REGISTRY_FILESTORAGE, users)
    }

    fun isRegistered(username: String): Boolean {
        return users.containsKey(username)
    }

    fun registerUser(username: String, password: String) {
        users[username] = StaffUser(username, encodePassword(password))
    }

    fun verifyPassword(username: String, password: String): Boolean {
        val user: StaffUser? = users[username]
        return user != null && encodePassword(password) == user.password
    }

    private fun encodePassword(password: String): String = Base64.getUrlEncoder().encodeToString(md.digest(password.toByteArray(Charsets.UTF_8)))
}
