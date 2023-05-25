package com.example.codewars

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
        val remoteKeyEntity = RemoteKeyEntity(
            label = "page",
            nextKey = 1
        )
    }

    @Before
    fun setup() {
        database = Room.inMemoryDatabaseBuilder(
            InstrumentationRegistry.getInstrumentation().targetContext,
            CodewarsDatabase::class.java)
            .build()

        dao = database.remoteKeyDao
    }

    @After
    fun teardown() {
        database.close()
    }

    @Test
    fun insertRemoteKey() = runBlocking{
        var remoteKey = dao.remoteKeyByQuery("page")

        Assert.assertNull(remoteKey)

        dao.insertOrReplace(remoteKeyEntity)
        remoteKey = dao.remoteKeyByQuery("page")
        Truth.assertThat(remoteKey).isEqualTo(remoteKeyEntity)
    }

    @Test
    fun deleteRemoteKey() = runBlocking{
        var remoteKey = dao.remoteKeyByQuery("page")

        Assert.assertNull(remoteKey)

        dao.insertOrReplace(remoteKeyEntity)
        remoteKey = dao.remoteKeyByQuery("page")
        Truth.assertThat(remoteKey).isEqualTo(remoteKeyEntity)

        dao.deleteByQuery("page")
        remoteKey = dao.remoteKeyByQuery("page")
        Assert.assertNull(remoteKey)
    }
}