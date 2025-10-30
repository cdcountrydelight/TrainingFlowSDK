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
                    "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyX2lkIjo1NDIxMiwiZXhwIjoxNzYxODk1MTY1LCJpYXQiOjE3NjE4MDg3NjV9.2hdSSaw-hO5xUas8vvK-EU1yMYIG4RyP0sLKQtWVPdM",
                    appName = "CD Partner App",
                    packageName = "deliveryapp.countrydelight.in.deliveryapp"
                ) {

                }
            }
        }
    }
}