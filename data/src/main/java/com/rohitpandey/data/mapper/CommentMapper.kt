package com.rohitpandey.data.mapper

import com.rohitpandey.data.local.entity.CommentEntity
import com.rohitpandey.domain.models.Comment
import com.rohitpandey.network.dto.CommentDto

fun CommentDto.toEntity(): CommentEntity = CommentEntity(
    id     = id,
    postId = postId,
    name   = name,
    email  = email,
    body   = body,
)

fun CommentEntity.toDomain(): Comment = Comment(
    id     = id,
    postId = postId,
    name   = name,
    email  = email,
    body   = body,
)

fun List<CommentEntity>.toDomain(): List<Comment> = map { it.toDomain() }