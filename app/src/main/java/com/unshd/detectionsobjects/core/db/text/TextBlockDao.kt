package com.unshd.detectionsobjects.core.db.text

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface TextBlockDao {

    /** Inserta un bloque de texto; reemplaza si ya existe el mismo id. */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(block: TextBlockEntity): Long

    /** Inserta una lista de bloques. */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(blocks: List<TextBlockEntity>): List<Long>

    /** Actualiza un bloque existente. */
    @Update
    suspend fun update(block: TextBlockEntity)

    /** Elimina un bloque. */
    @Delete
    suspend fun delete(block: TextBlockEntity)

    /** Borra todos los registros. */
    @Query("DELETE FROM text_blocks")
    suspend fun clearAll()

    /** Obtiene un bloque por id. */
    @Query("SELECT * FROM text_blocks WHERE id = :id LIMIT 1")
    suspend fun getById(id: Int): TextBlockEntity?

    /** Devuelve todos los bloques ordenados por fecha descendente. */
    @Query("SELECT * FROM text_blocks ORDER BY timestamp DESC")
     fun getAll(): Flow<List<TextBlockEntity>>

    /** Filtra por sesión. */
    @Query("SELECT * FROM text_blocks WHERE sessionId = :session ORDER BY timestamp DESC")
    suspend fun getBySession(session: String): List<TextBlockEntity>
}