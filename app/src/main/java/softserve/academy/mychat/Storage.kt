package softserve.academy.mychat

import android.content.Context
import java.io.ObjectInputStream
import java.io.ObjectOutputStream

interface StorageService {
    fun read(): List<String>
    fun write(items: List<String>)
}

class FileStorageService(private val context: Context) : StorageService {
    companion object {
        const val FILE_NAME: String = "cardList.dat"
    }

    override fun read(): List<String> = try {
        ObjectInputStream(context.openFileInput(FILE_NAME)).use {
            it.readObject() as ArrayList<String>
        }
    } catch (_: Exception) {
        emptyList()
    }

    override fun write(items: List<String>) {
        context.openFileOutput(FILE_NAME, Context.MODE_PRIVATE).use {
            ObjectOutputStream(it).writeObject(ArrayList(items))
        }
    }
}