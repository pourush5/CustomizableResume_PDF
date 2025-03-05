package com.pourush.assignment1prag
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun HomeScreen(viewModel: ResumeViewModel) {
    val context = LocalContext.current
    val resume = viewModel.resume

    var fontSize by remember { mutableStateOf(viewModel.fontSize) }
    var fontColor by remember { mutableStateOf(Color.Black) }
    var backgroundColor by remember { mutableStateOf(Color.White) }

    val requestPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (!isGranted) {
            Toast.makeText(context, "Storage permission denied. Cannot save PDF.", Toast.LENGTH_LONG).show()
        }
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
            .padding(16.dp)
    ) {
        Text(
            "Font Size: ${fontSize.toInt()}sp",
            color = fontColor,
            style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 18.sp)
        )
        Slider(
            value = fontSize,
            onValueChange = { fontSize = it },
            valueRange = 12f..24f
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            "Font Color",
            color = fontColor,
            style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 18.sp)
        )
        ColorPicker(selectedColor = fontColor) { fontColor = it }

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            "Background Color",
            color = fontColor,
            style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 18.sp)
        )
        ColorPicker(selectedColor = backgroundColor) { backgroundColor = it }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = resume.name,
            fontSize = fontSize.sp,
            color = fontColor,
            style = TextStyle(fontWeight = FontWeight.Bold, fontSize = fontSize.sp)
        )
        Text(
            text = resume.contactInfo,
            fontSize = fontSize.sp,
            color = fontColor,
            style = TextStyle(fontWeight = FontWeight.Medium, fontSize = fontSize.sp)
        )
        Text(
            text = "Skills: ${resume.skills.joinToString(", ")}",
            fontSize = fontSize.sp,
            color = fontColor,
            style = TextStyle(fontWeight = FontWeight.Medium, fontSize = fontSize.sp)
        )
        Text(
            text = "Work Experience:",
            fontSize = fontSize.sp,
            color = fontColor,
            style = TextStyle(fontWeight = FontWeight.Bold, fontSize = fontSize.sp)
        )

        resume.workExperience.forEach { experience ->
            Text(
                text = experience,
                fontSize = fontSize.sp,
                color = fontColor,
                style = TextStyle(fontWeight = FontWeight.Light, fontSize = fontSize.sp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                PdfGenerator.generatePdf(context, resume, fontSize, fontColor, backgroundColor)
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Generate PDF")
        }
    }
}

@Composable
fun ColorPicker(selectedColor: Color, onColorSelected: (Color) -> Unit) {
    val colors = listOf(Color.Black, Color.Red, Color.Blue, Color.Green, Color.Gray, Color.Magenta)

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        colors.forEach { color ->
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(color)
                    .border(2.dp, if (selectedColor == color) Color.White else Color.Transparent, CircleShape)
                    .clickable { onColorSelected(color) }
            )
        }
    }
}
