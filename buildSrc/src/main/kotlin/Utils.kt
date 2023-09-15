import java.io.File
import java.util.Properties

///////////////////////////////////////////////////////////////////////////
// Properties Utils
///////////////////////////////////////////////////////////////////////////
fun readProperties(propertiesFile: File) = Properties().apply {
    propertiesFile.inputStream().use { fis ->
        load(fis)
    }
}

fun Properties.require(key: String): Any =
    getOrElse(key) { throw IllegalArgumentException("Key => $key is missing!") }