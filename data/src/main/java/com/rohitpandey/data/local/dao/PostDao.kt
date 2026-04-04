package com.rohitpandey.data.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.rohitpandey.data.local.entity.PostEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PostDao {
    @Query("SELECT * FROM posts ORDER BY id ASC")
    fun getPagedPosts(): PagingSource<Int, PostEntity>

    @Query("SELECT * FROM posts WHERE isFavourite = 1 ORDER BY id ASC")
    fun getFavoritePosts(): Flow<List<PostEntity>>

    @Query("SELECT * FROM posts WHERE id = :id")
    fun observePostById(id: Int): Flow<PostEntity?>

    @Query("SELECT * FROM posts WHERE id = :id")
    suspend fun getPostById(id: Int): PostEntity?

    @Upsert
    suspend fun upsertPosts(posts: List<PostEntity>)

    @Upsert
    suspend fun upsertPost(post: PostEntity)

    @Query("UPDATE posts SET isFavourite = NOT isFavourite WHERE id = :postId")
    suspend fun toggleFavorite(postId: Int)

    @Query("DELETE FROM posts WHERE isFavourite = 0")
    suspend fun clearNonFavorites()

    @Query("SELECT COUNT(*) FROM posts")
    suspend fun count(): Int
}