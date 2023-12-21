package hwtask1;

import java.io.File;
import java.util.HashMap;
import kotlinx.serialization.decodeFromString;
import kotlinx.serialization.encodeToString;
import kotlinx.serialization.json.Json;

public class RegistryMapSerializer(val fileName: String) {
    public inline fun <reified T>serialize(map: HashMap<String, T>) {
        File(fileName).writeText(Json.encodeToString<HashMap<String, T>>(map));
    }

    public inline fun <reified T>deserialize(): HashMap<String, T> {
        val file = File(fileName);
        if (file.exists()) {
            return Json.decodeFromString<HashMap<String, T>>(file.readText(Charsets.UTF_8));
        }

        return HashMap<String, T>();
    }
};
