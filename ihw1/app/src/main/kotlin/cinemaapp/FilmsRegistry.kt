package cinemaapp

import kotlin.time.Duration

private const val FILMS_REGISTRY_FILE_STOREAGE = "films_reg.json"

class FilmsRegistry : Registry() {
    private val films: HashMap<String, Film> = deserialize<HashMap<String, Film>>(FILMS_REGISTRY_FILE_STOREAGE) ?: HashMap()

    override fun saveToFile() {
        serialize(FILMS_REGISTRY_FILE_STOREAGE, films)
    }

    fun addFilm(name: String, description: String, duration: Duration): Film {
        val film = Film(name, description, duration)
        films[name] = film
        return film
    }

    fun getFilm(name: String): Film? = films[name]

    fun renameFilm(oldName: String, newName: String): Boolean {
        val film: Film = films[oldName] ?: return false
        val prevFilmWithNewName: Film? = films.putIfAbsent(newName, film)
        if (prevFilmWithNewName != null) {
            return false
        }

        film.name = newName
        return true
    }
    
    fun deleteFilm(name: String): Film? = films.remove(name)
}