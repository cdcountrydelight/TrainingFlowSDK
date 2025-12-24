package com.cd.trainingapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.cd.trainingapp.ui.theme.TrainingAppTheme
import com.cd.trainingsdk.presentation.TrainingFlowNavGraph

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TrainingAppTheme {
                TrainingFlowNavGraph(
"eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyX2lkIjo1NDIxMiwiZXhwIjoxNzY2NjM5NDAxLCJpYXQiOjE3NjY1NTMwMDF9.giyPnSJFRXZuzu2D6iWqD5TfjmkPYxI8evTBBdbhpIo",
                    appName = "CD Partner App",
                    packageName = "deliveryapp.countrydelight.in.deliveryapp",
                    isProdEnv = false
                ) {

                }
            }
        }
    }
}