package com.example.myapplication

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase

@Entity(tableName = "MenuItem")
data class MenuItem(
    @PrimaryKey val id: Int,
    val title: String,
    val description: String,
    val price: String,
    val image: String,
    val category: String,
)

@Dao
interface MenuItemDao {
    @Insert
    fun insertAll(menuItems: List<MenuItem>)
    @Query("SELECT * FROM MenuItem")
    fun getAll(): LiveData<List<MenuItem>>
    @Query("SELECT (SELECT COUNT(*) FROM MenuItem) == 0")
    fun isEmpty(): Boolean
    @Query("SELECT category FROM MenuItem")
    fun getCategories(): LiveData<List<String>>
}

@Database(entities = [MenuItem::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun menuItemDao(): MenuItemDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "menu_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}