package com.projeto.gestaonotasbd.ui.theme

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.text.KeyboardOptions

@Composable
fun EstudanteForm(
    nome: String,
    nota1: String,
    nota2: String,
    onNomeChange: (String) -> Unit,
    onNota1Change: (String) -> Unit,
    onNota2Change: (String) -> Unit,
    onSalvar: () -> Unit,
    isEditing: Boolean = false
) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = if (isEditing) "Editar Aluno" else "Cadastrar Aluno",
                style = MaterialTheme.typography.titleLarge
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = nome,
                onValueChange = onNomeChange,
                label = { Text("Nome") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = nota1,
                onValueChange = onNota1Change,
                label = { Text("Nota 1") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = nota2,
                onValueChange = onNota2Change,
                label = { Text("Nota 2") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = onSalvar,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(if (isEditing) "Atualizar" else "Cadastrar")
            }
        }
    }
}