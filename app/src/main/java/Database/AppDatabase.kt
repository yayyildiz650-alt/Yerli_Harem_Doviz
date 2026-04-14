package Database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import Database.Dao
import Database.RoomModel

// Room Veritabanı yapılandırması
@Database(entities = [RoomModel::class], version = 1)
 abstract class AppDatabase : RoomDatabase() {

     // Veritabanı işlemlerine erişim için Dao fonksiyonu
     abstract fun userDao(): Dao

     companion object {  
          @Volatile
          private var INSTANCE: AppDatabase? = null

          // Singleton yapısı: Veritabanının sadece bir örneğinin (instance) olmasını sağlar
          fun getDatabase(context: Context): AppDatabase {
               return INSTANCE ?: synchronized(this) {
                    val instance = Room.databaseBuilder(
                         context.applicationContext,
                         AppDatabase::class.java,
                         "app_database"
                    ).build()
                    INSTANCE = instance
                    instance
               }
          }
     }
}
