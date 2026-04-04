package com.rohitpandey.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.rohitpandey.data.DataConstants
import com.rohitpandey.data.local.dao.CommentDao
import com.rohitpandey.data.local.dao.PostDao
import com.rohitpandey.data.local.dao.RemoteKeyDao
import com.rohitpandey.data.local.entity.CommentEntity
import com.rohitpandey.data.local.entity.PostEntity
import com.rohitpandey.data.local.entity.RemoteKeyEntity

@Database(
    entities = [
        PostEntity::class,
        CommentEntity::class,
        RemoteKeyEntity::class,
    ],
    version = DataConstants.DATABASE_VERSION,
    exportSchema = true,
)

abstract class AppDatabase : RoomDatabase() {
    abstract fun postDao(): PostDao
    abstract fun commentDao(): CommentDao
    abstract fun remoteKeyDao(): RemoteKeyDao
}
