package com.rohitpandey.domain.usecases

import com.rohitpandey.domain.models.Comment
import com.rohitpandey.domain.models.Post
import com.rohitpandey.domain.models.Result
import com.rohitpandey.domain.repositories.CommentRepository
import com.rohitpandey.domain.repositories.PostRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetPostDetailUseCase @Inject constructor(
    private val postRepository: PostRepository,
    private val commentRepository: CommentRepository,
) {
    fun getPost(id: Int): Flow<Result<Post>> =
        postRepository.getPostById(id)

    fun getComments(postId: Int): Flow<Result<List<Comment>>> =
        commentRepository.getCommentsForPost(postId)
}