package com.rohitpandey.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.rohitpandey.data.local.entity.CommentEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CommentDao {
    @Query("SELECT * FROM comments WHERE postId = :postId ORDER BY id ASC")
    fun observeCommentsForPost(postId: Int): Flow<List<CommentEntity>>

    @Upsert
    suspend fun upsertComments(comments: List<CommentEntity>)

    @Query("DELETE FROM comments WHERE postId = :postId")
    suspend fun clearCommentsForPost(postId: Int)
}