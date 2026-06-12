package com.example.equipocuatro.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.equipocuatro.model.Reto


@Database(entities = [Reto::class], version = 1)
abstract class RetoDB: RoomDatabase(){
    abstract fun retoDao(): RetoDao
}
