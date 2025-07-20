package com.unshd.detectionsobjects.core.db.text

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "text_blocks")
data class TextBlockEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val sessionId: String,
    val title: String,
    val fullText: String,
    val imageUri: String?,
    val timestamp: Long
)