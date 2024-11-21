package com.example.openclass.presentation.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicText
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.openclass.R
import com.example.openclass.ui.theme.celeste

@Preview
@Composable
fun MenuScreen(
    navigateToRegistroPersonal:() -> Unit = {},
    navigateToTareas:() -> Unit = {},
    navigateToControlAsistencia:() -> Unit = {},
    navigateToDatosContrato:() -> Unit = {},
    navigateToBeneficios:() -> Unit = {},
    navigateToDesempenoEmpleado:() -> Unit = {}
) {
    Column(
        modifier = Modifier
            .background(White)
            .fillMaxSize()
            .padding(50.dp)
    ) {

        Text(
            text = "Menu Principal",
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

        Spacer(modifier = Modifier.weight(1f))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                ImageCard(
                    imageRes = R.drawable.registro_personal,
                    description = "Registro de personal",
                    onClick = { navigateToRegistroPersonal() }
                )
                ImageCard(
                    imageRes = R.drawable.tareas,
                    description = "Tareas",
                    onClick = { navigateToTareas() }
                )
                ImageCard(
                    imageRes = R.drawable.control_asistencias,
                    description = "Control de Asistencia",
                    onClick = { navigateToControlAsistencia() }
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                ImageCard(
                    imageRes = R.drawable.datos_contrato,
                    description = "Datos de contrato",
                    onClick = { navigateToDatosContrato() }
                )
                ImageCard(
                    imageRes = R.drawable.beneficios,
                    description = "Beneficios   ",
                    onClick = { navigateToBeneficios() }
                )
                ImageCard(
                    imageRes = R.drawable.desempeno_empleado,
                    description = "Desempeño de Empleado",
                    onClick = { navigateToDesempenoEmpleado() }
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo2),
                contentDescription = "Logo",
                modifier = Modifier
                    .size(40.dp)
                    .padding(end = 8.dp)
            )
            BasicText(
                text = "OpenClass",
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold
                )
            )
        }
    }
}

@Composable
fun ImageCard(imageRes: Int, description: String, onClick: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(bottom = 16.dp)
            .clickable { onClick() }
    ) {
        Image(
            painter = painterResource(id = imageRes),
            contentDescription = description,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(100.dp)
        )
        Text(
            text = description,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyMedium.copy(
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onBackground
            ),
            modifier = Modifier.padding(top = 8.dp)
        )
    }
}