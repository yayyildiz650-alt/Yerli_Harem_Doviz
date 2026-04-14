package Database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

// DAO (Data Access Object): Veritabanı sorgularının tanımlandığı arayüz
@Dao
interface Dao {

    // Veritabanına yeni bir varlık eklemek için kullanılır
    @Insert
    fun insert(roomModel: RoomModel)

    // Veritabanından bir varlığı silmek için kullanılır
    @Delete
    fun delete(roomModel: RoomModel)

    // Veritabanındaki tüm verileri liste şeklinde çekmek için kullanılır
    @Query("SELECT * FROM table_name")
    fun getAll(): List<RoomModel>

    companion object {
        // Not: Bu kısım normalde Room ile kullanılmaz, gerekirse silinebilir.
        fun delete(silinecekModel: Database.RoomModel) {}
    }
}
