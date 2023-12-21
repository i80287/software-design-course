package hwtask1;

import java.util.HashMap;
import kotlinx.serialization.Serializable;

const val STAFF_REGISTRY_FILESTORAGE = "staff_reg.json";

public class StaffRegistry {
    @Serializable
    public data class StaffUser(public val username: String, public val password: String);

    @Serializable
    private val users: HashMap<String, StaffUser> = RegistryMapSerializer(STAFF_REGISTRY_FILESTORAGE).deserialize<StaffUser>();

    public fun saveRegistryState() {
        RegistryMapSerializer(STAFF_REGISTRY_FILESTORAGE).serialize<StaffUser>(users);
    }

    public fun isRegistered(username: String): Boolean {
        return users.containsKey(username);
    }

    public fun registerUser(username: String, password: String): Unit {
        users.put(username, StaffUser(username, password));
    }

    public fun verifyPassword(username: String, password: String): Boolean {
        val user: StaffUser? = users.get(username);
        return user != null && password.equals(user.password);
    }
};
