package com.unshd.detectionsobjects.core.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.unshd.detectionsobjects.core.db.label.ImageLabelDao
import com.unshd.detectionsobjects.core.db.label.ImageLabelEntity
import com.unshd.detectionsobjects.core.db.text.TextBlockDao
import com.unshd.detectionsobjects.core.db.text.TextBlockEntity

@Database(
    entities = [ImageLabelEntity::class, TextBlockEntity::class],
    version = 3, //Cada cambio en la base de datos o tabla requiere un nuevo número.
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun imageLabelDao(): ImageLabelDao
    abstract fun textBlockDao(): TextBlockDao
}