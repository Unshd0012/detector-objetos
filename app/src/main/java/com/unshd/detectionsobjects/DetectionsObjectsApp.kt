package com.unshd.detectionsobjects

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.unshd.detectionsobjects.core.db.AppDatabase
import com.unshd.detectionsobjects.core.db.label.ImageLabelDao
import com.unshd.detectionsobjects.core.db.text.TextBlockDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.HiltAndroidApp
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Qualifier
import javax.inject.Singleton

@HiltAndroidApp
class DetectionsObjectsApp : Application(){

}

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext ctx: Context
    ): AppDatabase =
        Room.databaseBuilder(ctx, AppDatabase::class.java, "detections.db")
            .fallbackToDestructiveMigration(true)   // ‑‑> borra todas las tablas si el esquema cambia
            .build()

    @Provides
    fun provideImageLabelDao(db: AppDatabase): ImageLabelDao = db.imageLabelDao()

    @Provides
    fun provideTextBlockDao(db: AppDatabase): TextBlockDao = db.textBlockDao()
}

@Module
@InstallIn(SingletonComponent::class)
object CoroutineModule {
    @Provides @IoDispatcher fun provideIoDispatcher(): CoroutineDispatcher = Dispatchers.IO

    @Retention(AnnotationRetention.BINARY)
    @Qualifier
    annotation class IoDispatcher
}