package com.example.openclass.presentation.initial

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.openclass.R
import com.example.openclass.ui.theme.celeste
import com.example.openclass.ui.theme.grey
import com.example.openclass.ui.theme.mostasa

@Preview
@Composable
fun InitialScreen( navigateToLogin:() -> Unit = {}, navigateToSignUp: () -> Unit = {}){
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(grey,White, grey))),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Spacer(modifier = Modifier.weight(1f))
        Image(painter = painterResource(id = R.drawable.logo1), contentDescription = "")
        Spacer(modifier = Modifier.weight(1f))
        Text("OpenClass",
            color = celeste,
            fontSize = 38.sp,
            fontWeight = FontWeight.Bold
        )
        Text("Gestion de Recursos Humanos")
        Spacer(modifier = Modifier.weight(1f))
        Button(
            onClick = { navigateToSignUp() },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .padding(horizontal = 32.dp),
            colors = ButtonDefaults.buttonColors(containerColor = mostasa))
        {
            Text(text = "Registrate", color = White)
        }
        Spacer(modifier = Modifier.height(8.dp))
        CustomButton(
            painterResource(id = R.drawable.google), "Continuar con Google"
        )
        Spacer(modifier = Modifier.height(8.dp))
        CustomButton(
            painterResource(id = R.drawable.linkedin),
            "Continuar con Linkedin"
        )
        Text(
            text = "Iniciar Sesion",
            color = Black,
            modifier = Modifier.padding(24.dp).clickable { navigateToLogin() },
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.weight(1f))
    }
}

@Composable
fun CustomButton(painter: Painter, title: String){
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
            .padding(horizontal = 32.dp)
            .border(2.dp, Black, CircleShape)
            , contentAlignment = Alignment.CenterStart
    ){
        Image(
            painter = painter,
            contentDescription = "",
            modifier = Modifier.padding(16.dp).size(16.dp)
        )
        Text(
            text = title,
            color = Black,
            modifier = Modifier.fillMaxWidth() ,
            textAlign = TextAlign.Center
        )
    }
}
