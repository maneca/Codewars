package com.example.codewars.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.codewars.data.local.converters.ListConverter
import com.example.codewars.data.local.dao.CodewarsDao
import com.example.codewars.data.local.dao.RemoteKeyDao
import com.example.codewars.data.local.entity.CompletedChallengeEntity
import com.example.codewars.data.local.entity.RemoteKeyEntity

@Database(
    entities = [CompletedChallengeEntity::class, RemoteKeyEntity::class],
    version = 1, exportSchema = false
)
@TypeConverters(ListConverter::class)
abstract class CodewarsDatabase : RoomDatabase(){

    abstract val codewarsDao: CodewarsDao

    abstract val remoteKeyDao: RemoteKeyDao
}