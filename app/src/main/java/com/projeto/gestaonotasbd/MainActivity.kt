package com.projeto.gestaonotasbd

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.projeto.gestaonotasbd.data.AppDatabase
import com.projeto.gestaonotasbd.repository.EstudanteRepository
import com.projeto.gestaonotasbd.ui.theme.EstudanteScreen
import com.projeto.gestaonotasbd.ui.theme.GestaonotasBDTheme
import com.projeto.gestaonotasbd.viewmodel.EstudanteViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            GestaonotasBDTheme {
                Surface(modifier = Modifier) {
                    val database = AppDatabase.getDatabase(this@MainActivity)
                    val repository = EstudanteRepository(database.estudanteDao())
                    val viewModel = EstudanteViewModel(repository)
                    EstudanteScreen(viewModel = viewModel)
                }
            }
        }
    }
}
