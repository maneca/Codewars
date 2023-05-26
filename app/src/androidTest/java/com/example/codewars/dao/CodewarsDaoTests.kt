package com.example.codewars.dao

import androidx.room.Room
import androidx.test.filters.SmallTest
import androidx.test.platform.app.InstrumentationRegistry
import com.example.codewars.data.local.CodewarsDatabase
import com.example.codewars.data.local.dao.CodewarsDao
import com.example.codewars.data.local.entity.CompletedChallengeEntity
import com.example.codewars.domain.model.CompletedChallenge
import com.example.codewars.util.getData
import com.google.common.truth.Truth
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test

@SmallTest
class CodewarsDaoTests {
    private lateinit var database: CodewarsDatabase
    private lateinit var dao: CodewarsDao

    companion object{
        val completedChallengeA = CompletedChallengeEntity(
            id = "1",
            name = "Challenge A",
            slug = "Challenge A",
            completedAt = "2017-04-06T16:32:09Z",
            completedLanguages = listOf( "javascript",
                "coffeescript",
                "ruby",
                "javascript",
                "ruby",
                "javascript",
                "ruby",
                "coffeescript",
                "javascript",
                "ruby",
                "coffeescript")
        )

        val completedChallengeB = CompletedChallengeEntity(
            id = "2",
            name = "Challenge B",
            slug = "Challenge B",
            completedAt = "2017-04-06T16:32:09Z",
            completedLanguages = listOf( "javascript",
                "coffeescript",
                "ruby",
                "javascript",
                "ruby",
                "javascript",
                "ruby",
                "coffeescript",
                "javascript",
                "ruby",
                "coffeescript")

        )
    }

    @Before
    fun setup() {
        database = Room.inMemoryDatabaseBuilder(
            InstrumentationRegistry.getInstrumentation().targetContext,
            CodewarsDatabase::class.java)
            .build()

        dao = database.codewarsDao()
    }

    @After
    fun teardown() {
        database.close()
    }

    @Test
    fun insertAllCompletedChallenges() = runBlocking{
        var challenges = dao.getCompletedChallenges()

        Truth.assertThat(challenges.getData()).isEqualTo(emptyList<CompletedChallenge>())

        dao.insertAllCompletedChallenges(listOf(completedChallengeA, completedChallengeB))
        challenges = dao.getCompletedChallenges()
        Truth.assertThat(challenges.getData().size).isEqualTo(2)
    }

    @Test
    fun deleteCompletedChallenges() = runBlocking{
        var challenges = dao.getCompletedChallenges()

        Truth.assertThat(challenges.getData()).isEqualTo(emptyList<CompletedChallenge>())

        dao.insertAllCompletedChallenges(listOf(completedChallengeA, completedChallengeB))
        challenges = dao.getCompletedChallenges()
        Truth.assertThat(challenges.getData().size).isEqualTo(2)

        dao.deleteCompletedChallenges()
        challenges = dao.getCompletedChallenges()
        Truth.assertThat(challenges.getData().size).isEqualTo(0)
    }
}