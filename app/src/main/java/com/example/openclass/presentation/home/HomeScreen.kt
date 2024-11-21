package com.example.openclass.presentation.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.google.firebase.firestore.FirebaseFirestore


@Composable
fun HomeScreen(db:FirebaseFirestore){
    Column (
        Modifier
            .fillMaxSize()
            .background(White)
    ){
        Text("Popular artist",
            color = Black,
            fontWeight = FontWeight.Bold,
            fontSize = 30.sp)
    }
}


//fun createArtist(db:FirebaseFirestore){
//
//    val random = (1..10000).random()
//    val artist = Artist(name = "Random $random", numberOfSongs = random)
//    db.collection("artists").add(artist)
//        .addOnSuccessListener {
//            Log.i("Aris", "Succes")
//        }
//        .addOnFailureListener{
//            Log.i("Aris", "Failure")
//        }
//        .addOnCompleteListener{
//            Log.i("Aris", "Complete")
//        }
//}