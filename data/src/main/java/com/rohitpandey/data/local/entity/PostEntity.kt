package com.rohitpandey.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "posts")
data class PostEntity(
    @PrimaryKey val id: Int,
    val userId: Int,
    val title: String,
    val body: String,
    val isFavourite: Boolean = false,
    val isSynced: Boolean = false,
    val lastUpdated: Long = System.currentTimeMillis(),
)