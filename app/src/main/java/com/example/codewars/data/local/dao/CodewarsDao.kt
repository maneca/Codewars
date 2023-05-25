package com.example.codewars.data.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.codewars.data.local.entity.CompletedChallengeEntity

@Dao
interface CodewarsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllCompletedChallenges(challenges: List<CompletedChallengeEntity>)

    @Query("SELECT * FROM completedChallenges")
    fun getCompletedChallenges() : PagingSource<Int, CompletedChallengeEntity>

    @Query("DELETE FROM completedChallenges")
    fun deleteCompletedChallenges()
}