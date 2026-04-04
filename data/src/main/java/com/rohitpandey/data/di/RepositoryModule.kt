package com.rohitpandey.data.di

import com.rohitpandey.data.repository.CommentRepositoryImpl
import com.rohitpandey.data.repository.PostRepositoryImpl
import com.rohitpandey.domain.repositories.CommentRepository
import com.rohitpandey.domain.repositories.PostRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun bindPostRepository(impl: PostRepositoryImpl): PostRepository

    @Binds
    @Singleton
    abstract fun bindCommentRepository(impl: CommentRepositoryImpl): CommentRepository
}