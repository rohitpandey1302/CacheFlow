package com.rohitpandey.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.rohitpandey.data.local.entity.RemoteKeyEntity

@Dao
interface RemoteKeyDao {
    @Query("SELECT * FROM remote_keys WHERE postId = :postId")
    suspend fun remoteKeyByPostId(postId: Int): RemoteKeyEntity?

    @Upsert
    suspend fun upsertAll(keys: List<RemoteKeyEntity>)

    @Query("DELETE FROM remote_keys")
    suspend fun clearAll()

    @Query("SELECT createdAt FROM remote_keys ORDER BY createdAt DESC LIMIT 1")
    suspend fun latestCreatedAt(): Long?
}