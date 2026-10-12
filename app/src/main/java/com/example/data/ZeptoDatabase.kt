package com.example.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [
        ProductEntity::class,
        CartItemEntity::class,
        BrowsingHistoryEntity::class,
        OrderEntity::class,
        OrderItemEntity::class,
        AddressEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class ZeptoDatabase : RoomDatabase() {
    abstract fun productDao(): ProductDao
    abstract fun cartDao(): CartDao
    abstract fun browsingHistoryDao(): BrowsingHistoryDao
    abstract fun orderDao(): OrderDao
    abstract fun addressDao(): AddressDao

    companion object {
        @Volatile
        private var INSTANCE: ZeptoDatabase? = null

        fun getDatabase(context: Context): ZeptoDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ZeptoDatabase::class.java,
                    "zepto_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
