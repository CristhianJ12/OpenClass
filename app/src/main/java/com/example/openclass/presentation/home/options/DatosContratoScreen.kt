package com.example.openclass.presentation.home.options

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.openclass.ui.theme.celeste

@Composable
fun DatosContratoScreen() {


    Column(
        modifier = Modifier
            .background(White)
            .fillMaxSize()
            .padding(50.dp)
    ) {
        Text(
            text = "Datos del Contrato",
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.displayMedium.copy(
                fontSize = 45.sp,
                fontWeight = FontWeight.Bold,
                color = celeste
            ),
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(bottom = 16.dp)
        )
    }
}