package com.rohitpandey.network.api

import com.rohitpandey.network.dto.CommentDto
import com.rohitpandey.network.dto.PostDto
import com.rohitpandey.network.dto.UserDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PostApiService {
    @GET("posts")
    suspend fun getPosts(
        @Query("page")  page: Int,
        @Query("limit") limit: Int = PAGE_SIZE,
    ): List<PostDto>

    @GET("posts/{id}")
    suspend fun getPostById(@Path("id") id: Int): PostDto

    @GET("posts/{id}/comments")
    suspend fun getCommentsForPost(@Path("id") postId: Int): List<CommentDto>

    @GET("users/{id}")
    suspend fun getUserById(@Path("id") userId: Int): UserDto

    companion object {
        const val PAGE_SIZE = 20
    }
}