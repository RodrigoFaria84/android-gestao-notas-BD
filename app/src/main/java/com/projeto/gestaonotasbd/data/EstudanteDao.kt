package com.projeto.gestaonotasbd.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface EstudanteDao {

    @Insert
    suspend fun insert(estudante: Estudante)

    @Update
    suspend fun update(estudante: Estudante)

    @Delete
    suspend fun delete(estudante: Estudante)

    @Query("SELECT * FROM estudantes ORDER BY nome")
    fun getAllStudents(): Flow<List<Estudante>>
}
