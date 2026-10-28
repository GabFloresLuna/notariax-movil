package cl.duoc.myapplication.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [UsuarioEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun usuarioDao(): UsuarioDao

    companion object {
        // Se crea una sola vez en MainActivity y la usa toda la app
        var instancia: AppDatabase? = null
            private set

        fun getDatabase(context: Context): AppDatabase {
            if (instancia == null) {
                instancia = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "notariax_db"
                ).build()
            }
            return instancia!!
        }
    }
}
