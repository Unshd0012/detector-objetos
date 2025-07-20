package com.unshd.detectionsobjects.core.db.label

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface ImageLabelDao {

    /** Inserta una etiqueta; reemplaza si existe el mismo id. */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(label: ImageLabelEntity): Long

    /** Inserta una lista de etiquetas. */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(labels: List<ImageLabelEntity>): List<Long>

    /** Actualiza una etiqueta. */
    @Update
    suspend fun update(label: ImageLabelEntity)

    /** Elimina una etiqueta. */
    @Delete
    suspend fun delete(label: ImageLabelEntity)

    /** Borra todas las filas de la tabla. */
    @Query("DELETE FROM image_labels")
    suspend fun clearAll()

    /** Obtiene una etiqueta por id. */
    @Query("SELECT * FROM image_labels WHERE id = :id LIMIT 1")
    suspend fun getById(id: Int): ImageLabelEntity?

    /** Devuelve todas las etiquetas ordenadas por fecha descendente. */
    @Query("SELECT * FROM image_labels ORDER BY timestamp DESC")
    suspend fun getAll(): List<ImageLabelEntity>

    /** Filtra por sesión. */
    @Query("SELECT * FROM image_labels WHERE sessionId = :session ORDER BY timestamp DESC")
    suspend fun getBySession(session: String): List<ImageLabelEntity>

}