package com.projeto.gestaonotasbd.viewmodel

import com.projeto.gestaonotasbd.data.Estudante
import com.projeto.gestaonotasbd.data.EstudanteDao
import com.projeto.gestaonotasbd.repository.EstudanteRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class EstudanteViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var fakeRepository: FakeEstudanteRepository
    private lateinit var viewModel: EstudanteViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        fakeRepository = FakeEstudanteRepository(FakeDaoVazio())
        viewModel = EstudanteViewModel(fakeRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun salvar_comNotasAprovadas_deveDefinirSituacaoAprovado() = runTest(testDispatcher) {
        val nome = "João"
        val nota1 = 8.0
        val nota2 = 8.0

        viewModel.salvar(
            id = 0,
            nome = nome,
            nota1 = nota1,
            nota2 = nota2
        )
        
        testDispatcher.scheduler.advanceUntilIdle()

        val estudanteSalvo = fakeRepository.ultimoEstudanteInserido
        
        assertEquals(nome, estudanteSalvo?.nome)
        assertEquals(8.0, estudanteSalvo!!.media, 0.01)
        assertEquals("Aprovado", estudanteSalvo.situacao)
    }

    @Test
    fun salvar_comNotasReprovadas_deveDefinirSituacaoReprovado() = runTest(testDispatcher) {
        val nome = "Maria"
        val nota1 = 4.0
        val nota2 = 6.0

        viewModel.salvar(
            id = 0,
            nome = nome,
            nota1 = nota1,
            nota2 = nota2
        )

        testDispatcher.scheduler.advanceUntilIdle()

        val estudanteSalvo = fakeRepository.ultimoEstudanteInserido
        
        assertEquals(nome, estudanteSalvo?.nome)
        assertEquals(5.0, estudanteSalvo!!.media, 0.01)
        assertEquals("Reprovado", estudanteSalvo.situacao)
    }

    @Test
    fun salvar_comMediaExatamente7_deveAprovar() = runTest(testDispatcher) {
        val nome = "Pedro"
        val nota1 = 7.0
        val nota2 = 7.0

        viewModel.salvar(
            id = 0,
            nome = nome,
            nota1 = nota1,
            nota2 = nota2
        )

        testDispatcher.scheduler.advanceUntilIdle()

        val estudanteSalvo = fakeRepository.ultimoEstudanteInserido
        
        assertEquals("Aprovado", estudanteSalvo?.situacao)
    }

    @Test
    fun salvar_comIdZero_deveInsertarNovoEstudante() = runTest(testDispatcher) {
        viewModel.salvar(
            id = 0,
            nome = "Ana",
            nota1 = 8.0,
            nota2 = 9.0
        )

        testDispatcher.scheduler.advanceUntilIdle()

        assertTrue("Repository deve ter recebido insert", fakeRepository.fezInsert)
    }

    @Test
    fun salvar_comIdDiferente_deveAtualizarEstudante() = runTest(testDispatcher) {
        viewModel.salvar(
            id = 5,
            nome = "Carlos",
            nota1 = 7.5,
            nota2 = 8.5
        )

        testDispatcher.scheduler.advanceUntilIdle()

        assertTrue("Repository deve ter recebido update", fakeRepository.fezUpdate)
    }

    @Test
    fun excluir_deveDeletearEstudante() = runTest(testDispatcher) {
        val estudante = Estudante(
            id = 1,
            nome = "Lucas",
            nota1 = 6.0,
            nota2 = 8.0,
            media = 7.0,
            situacao = "Aprovado"
        )

        viewModel.excluir(estudante)

        testDispatcher.scheduler.advanceUntilIdle()

        assertTrue("Repository deve ter recebido delete", fakeRepository.fezDelete)
        assertEquals(estudante, fakeRepository.ultimoEstudanteDeletado)
    }

    private class FakeEstudanteRepository(dao: EstudanteDao) : EstudanteRepository(dao) {
        var fezInsert = false
        var fezUpdate = false
        var fezDelete = false
        var ultimoEstudanteInserido: Estudante? = null
        var ultimoEstudanteDeletado: Estudante? = null

        override suspend fun insert(student: Estudante) {
            fezInsert = true
            ultimoEstudanteInserido = student
        }

        override suspend fun update(student: Estudante) {
            fezUpdate = true
        }

        override suspend fun delete(student: Estudante) {
            fezDelete = true
            ultimoEstudanteDeletado = student
        }
    }

    private class FakeDaoVazio : EstudanteDao {
        override suspend fun insert(estudante: Estudante) {}
        override suspend fun update(estudante: Estudante) {}
        override suspend fun delete(estudante: Estudante) {}
        override fun getAllStudents(): Flow<List<Estudante>> = MutableStateFlow(emptyList())
    }
}

