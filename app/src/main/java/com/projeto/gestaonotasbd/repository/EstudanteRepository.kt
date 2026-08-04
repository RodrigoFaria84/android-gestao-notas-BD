package com.projeto.gestaonotasbd.repository

import com.projeto.gestaonotasbd.data.Estudante
import com.projeto.gestaonotasbd.data.EstudanteDao

open class EstudanteRepository(
    private val dao: EstudanteDao
) {
    open val students = dao.getAllStudents()

    open suspend fun insert(student: Estudante) {
        dao.insert(student)
    }

    open suspend fun update(student: Estudante) {
        dao.update(student)
    }

    open suspend fun delete(student: Estudante) {
        dao.delete(student)
    }
}