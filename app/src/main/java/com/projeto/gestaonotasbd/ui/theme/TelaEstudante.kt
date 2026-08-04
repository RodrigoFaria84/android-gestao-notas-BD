package com.projeto.gestaonotasbd.ui.theme

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.projeto.gestaonotasbd.viewmodel.EstudanteViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EstudanteScreen(
    viewModel: EstudanteViewModel
) {
    val students by viewModel.students.collectAsStateWithLifecycle()

    var nome by remember { mutableStateOf("") }
    var nota1 by remember { mutableStateOf("") }
    var nota2 by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "Gestão de Notas",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            )
        },
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            EstudanteForm(
                nome = nome,
                nota1 = nota1,
                nota2 = nota2,
                onNomeChange = { nome = it },
                onNota1Change = { nota1 = it },
                onNota2Change = { nota2 = it },
                onSalvar = {
                    val nota1Valor = nota1.toDoubleOrNull()
                    val nota2Valor = nota2.toDoubleOrNull()
                    if (nome.isNotEmpty() && nota1Valor != null && nota2Valor != null) {
                        viewModel.salvar(
                            nome = nome,
                            nota1 = nota1Valor,
                            nota2 = nota2Valor
                        )
                        nome = ""
                        nota1 = ""
                        nota2 = ""
                    }
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn(
                modifier = Modifier.fillMaxWidth()
            ) {
                items(students) { estudante ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(12.dp)
                        ) {
                            Text("Nome: ${estudante.nome}")
                            Text("Nota 1: ${estudante.nota1}")
                            Text("Nota 2: ${estudante.nota2}")
                            Text("Média: ${estudante.media}")
                            Text("Situação: ${estudante.situacao}")

                            Spacer(modifier = Modifier.height(8.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Button(
                                    onClick = {
                                        viewModel.excluir(estudante)
                                    },
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text("Excluir")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}