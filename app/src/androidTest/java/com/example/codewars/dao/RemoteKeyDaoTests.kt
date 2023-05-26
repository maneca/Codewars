package com.example.codewars.dao

import androidx.room.Room
import androidx.test.filters.SmallTest
import androidx.test.platform.app.InstrumentationRegistry
import com.example.codewars.data.local.CodewarsDatabase
import com.example.codewars.data.local.dao.RemoteKeyDao
import com.example.codewars.data.local.entity.RemoteKeyEntity
import com.google.common.truth.Truth
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test

@SmallTest
class RemoteKeyDaoTests {
    private lateinit var database: CodewarsDatabase
    private lateinit var dao: RemoteKeyDao

    companion object{
        val remoteKeysEntity = listOf(RemoteKeyEntity(
            label = "56f8fe6a2e6c0dc83b0008a7",
            prevKey = null,
            nextKey = 1
        ),
            RemoteKeyEntity(
                label = "56d8ae9237123036d3001b54",
                prevKey = null,
                nextKey = 1
            ))
    }

    @Before
    fun setup() {
        database = Room.inMemoryDatabaseBuilder(
            InstrumentationRegistry.getInstrumentation().targetContext,
            CodewarsDatabase::class.java)
            .build()

        dao = database.remoteKeyDao()
    }

    @After
    fun teardown() {
        database.close()
    }

    @Test
    fun insertRemoteKey() = runBlocking{
        var remoteKey = dao.remoteKeyByQuery(remoteKeysEntity[0].label)

        Assert.assertNull(remoteKey)

        dao.insertOrReplace(remoteKeysEntity)
        remoteKey = dao.remoteKeyByQuery(remoteKeysEntity[0].label)
        Truth.assertThat(remoteKey).isEqualTo(remoteKeysEntity[0])
    }

    @Test
    fun deleteRemoteKey() = runBlocking{
        var remoteKey = dao.remoteKeyByQuery(remoteKeysEntity[0].label)

        Assert.assertNull(remoteKey)

        dao.insertOrReplace(remoteKeysEntity)
        remoteKey = dao.remoteKeyByQuery(remoteKeysEntity[0].label)
        Truth.assertThat(remoteKey).isEqualTo(remoteKeysEntity[0])

        dao.deleteAll()
        remoteKey = dao.remoteKeyByQuery(remoteKeysEntity[0].label)
        Assert.assertNull(remoteKey)
    }
}