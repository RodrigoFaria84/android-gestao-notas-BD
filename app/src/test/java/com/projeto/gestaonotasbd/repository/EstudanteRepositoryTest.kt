package com.projeto.gestaonotasbd.repository

import com.projeto.gestaonotasbd.data.Estudante
import com.projeto.gestaonotasbd.data.EstudanteDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class EstudanteRepositoryTest {

    private val fakeDao = FakeEstudanteDao()
    private val repository = EstudanteRepository(fakeDao)

    @Test
    fun insert_deveAdicionarEstudante() = runBlocking {
        val estudante = estudantePadrao()

        repository.insert(estudante)

        assertEquals(1, fakeDao.getAllStudents().first().size)
        assertEquals(estudante, fakeDao.getAllStudents().first().first())
    }

    @Test
    fun update_deveAtualizarEstudanteExistente() = runBlocking {
        val original = estudantePadrao(id = 1, nome = "Ana")
        val atualizado = original.copy(nome = "Ana Paula")
        fakeDao.insert(original)

        repository.update(atualizado)

        assertEquals("Ana Paula", fakeDao.getAllStudents().first().first().nome)
    }

    @Test
    fun delete_deveRemoverEstudante() = runBlocking {
        val estudante = estudantePadrao(id = 1)
        fakeDao.insert(estudante)

        repository.delete(estudante)

        assertTrue(fakeDao.getAllStudents().first().isEmpty())
    }

    @Test
    fun students_deveExporFluxoDoDao() = runBlocking {
        val estudante = estudantePadrao(id = 10, nome = "Carlos")

        repository.insert(estudante)

        val resultado = repository.students.first()
        assertEquals(listOf(estudante), resultado)
    }

    private fun estudantePadrao(
        id: Int = 0,
        nome: String = "Aluno",
        nota1: Double = 8.0,
        nota2: Double = 6.0
    ): Estudante {
        return Estudante(
            id = id,
            nome = nome,
            nota1 = nota1,
            nota2 = nota2,
            media = (nota1 + nota2) / 2,
            situacao = if ((nota1 + nota2) / 2 >= 7.0) "Aprovado" else "Reprovado"
        )
    }

    private class FakeEstudanteDao : EstudanteDao {
        private val estudantes = MutableStateFlow<List<Estudante>>(emptyList())

        override suspend fun insert(estudante: Estudante) {
            estudantes.value = estudantes.value + estudante
        }

        override suspend fun update(estudante: Estudante) {
            estudantes.value = estudantes.value.map {
                if (it.id == estudante.id) estudante else it
            }
        }

        override suspend fun delete(estudante: Estudante) {
            estudantes.value = estudantes.value.filterNot { it.id == estudante.id }
        }

        override fun getAllStudents(): Flow<List<Estudante>> = estudantes
    }
}

