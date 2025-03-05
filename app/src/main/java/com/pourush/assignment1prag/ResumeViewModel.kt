package com.pourush.assignment1prag

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel

class ResumeViewModel : ViewModel() {
    var fontSize by mutableStateOf(16f)
    var fontColor by mutableStateOf(Color.Black)
    var backgroundColor by mutableStateOf(Color.White)

    val resume = Resume(
        name = "Parnella Charlesbois",
        contactInfo = "568 Port Washington Road\n+1 403-721-6898\nNordegg, AB TDM 2H0\nn.charlesbois@yahoo.com",
        skills = listOf("Electrotyper", "Logging equipment operator", "Foot doctor"),
        workExperience = listOf(
            "Your bus driver - Laboris nostrud consectetur culpa labore magna adipiscing minim autodoloris.",
            "Logging equipment operator - Laboris nostrud consectetur culpa labore magna adipiscing minim autodoloris.",
            "Foot doctor - Laboris nostrud consectetur culpa labore magna adipiscing minim autodoloris."
        )
    )
}