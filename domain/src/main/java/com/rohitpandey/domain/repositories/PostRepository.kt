package com.rohitpandey.domain.repositories

import androidx.paging.PagingData
import com.rohitpandey.domain.models.Post
import com.rohitpandey.domain.models.Result
import kotlinx.coroutines.flow.Flow

interface PostRepository {
    fun getPagedPosts(): Flow<PagingData<Post>>

    fun getPostById(id: Int): Flow<Result<Post>>

    fun getFavouritePosts(): Flow<List<Post>>

    suspend fun toggleFavourite(postId: Int)

    suspend fun syncPosts(): Result<Unit>
}