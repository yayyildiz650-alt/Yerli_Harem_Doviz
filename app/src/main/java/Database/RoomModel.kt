package Database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "table_name")

data class RoomModel (

    @PrimaryKey(autoGenerate = true) // Otomatik artan bir id oluşturuyoruz
    val id: Int,

    //Hangi değişkenlerin tutulacağını bildiriyoruz..
    val commadityName: String,
    val amount: Double,
    val buyingPrice: Double

)