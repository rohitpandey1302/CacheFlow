package com.rohitpandey.data.mapper

import com.rohitpandey.data.local.entity.PostEntity
import com.rohitpandey.domain.models.Post
import com.rohitpandey.network.dto.PostDto

fun PostDto.toEntity(
    isFavorite: Boolean = false,
    isSynced: Boolean = true,
): PostEntity = PostEntity(
    id = id,
    userId = userId,
    title = title,
    body = body,
    isFavourite = isFavorite,
    isSynced = isSynced,
    lastUpdated = System.currentTimeMillis(),
)

fun PostEntity.toDomain(): Post = Post(
    id = id,
    userId = userId,
    title = title,
    body = body,
    isFavourite = isFavourite,
    isSynced = isSynced,
)

fun List<PostEntity>.toDomain(): List<Post> = map { it.toDomain() }

