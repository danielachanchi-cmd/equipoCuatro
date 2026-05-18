package com.example.equipocuatro.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.equipocuatro.model.Reto
import com.example.equipocuatro.utils.Constants.NAME_BD

@Database(entities = [Reto::class], version = 1)
abstract class RetoDB: RoomDatabase(){

    abstract fun retoDao(): RetoDao

    companion object{
        fun getDatabase(context: Context): RetoDB {
            return Room.databaseBuilder(
                context.applicationContext,
                RetoDB::class.java,
                NAME_BD
            ).build()
        }
    }
}
