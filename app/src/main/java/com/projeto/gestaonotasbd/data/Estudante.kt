package com.projeto.gestaonotasbd.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "estudantes")

data class Estudante(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val nome: String,
    val nota1: Double,
    val nota2: Double,
    val media: Double,
    val situacao: String
)
