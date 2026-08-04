package com.projeto.gestaonotasbd.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.projeto.gestaonotasbd.data.Estudante
import com.projeto.gestaonotasbd.repository.EstudanteRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class EstudanteViewModel(
    private val repository: EstudanteRepository
) : ViewModel() {

    val students = repository.students.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = emptyList<Estudante>()
    )

    fun salvar(
        id: Int = 0,
        nome: String,
        nota1: Double,
        nota2: Double
    ) {
        val media = (nota1 + nota2) / 2
        val situacao = if (media >= 7.0) "Aprovado" else "Reprovado"

        val estudante = Estudante(
            id = id,
            nome = nome,
            nota1 = nota1,
            nota2 = nota2,
            media = media,
            situacao = situacao
        )

        viewModelScope.launch {
            if (id == 0) {
                repository.insert(estudante)
            } else {
                repository.update(estudante)
            }
        }
    }

    fun excluir(estudante: Estudante) {
        viewModelScope.launch {
            repository.delete(estudante)
        }
    }
}