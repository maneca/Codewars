package com.example.codewars.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.codewars.data.local.converters.ListConverter
import com.example.codewars.data.local.dao.CodewarsDao
import com.example.codewars.data.local.dao.RemoteKeyDao
import com.example.codewars.data.local.entity.CompletedChallengeEntity
import com.example.codewars.data.local.entity.RemoteKeyEntity
import com.example.codewars.utils.DATABASE_NAME

@Database(
    entities = [CompletedChallengeEntity::class, RemoteKeyEntity::class],
    version = 1, exportSchema = false
)
@TypeConverters(ListConverter::class)
abstract class CodewarsDatabase : RoomDatabase(){

    abstract val codewarsDao: CodewarsDao
    abstract val remoteKeyDao: RemoteKeyDao

    companion object {
        @Volatile private var instance: CodewarsDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance
            ?: synchronized(LOCK) {
                instance
                    ?: buildDatabase(
                        context
                    ).also { instance = it }
            }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(context.applicationContext, CodewarsDatabase::class.java, DATABASE_NAME)
                .build()
    }
}