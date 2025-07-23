package com.unshd.detectionsobjects.core.db.label

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "image_labels")
data class ImageLabelEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val sessionId: String,
    val title: String,
    val description: String,
    val imageUrl: String,
    val uri: String,
    val origen: String,
    val labelText: String,
    val confidence: Float,
    val timestamp: Long
)
