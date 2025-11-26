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
                    "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyX2lkIjo1NDIxMiwiZXhwIjoxNzY0MTgwNzUwLCJpYXQiOjE3NjQwOTQzNTB9.N_f1jeNwgY1kGywTlRbEqPmQqISfZko1r_FRMWlh2Gc",
                    appName = "CD Partner App",
                    packageName = "deliveryapp.countrydelight.in.deliveryapp",
                    isProdEnv = false
                ) {

                }
            }
        }
    }
}