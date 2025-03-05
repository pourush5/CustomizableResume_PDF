package com.pourush.assignment1prag

import android.content.ContentValues
import android.content.Context
import android.graphics.*
import android.graphics.pdf.PdfDocument
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import java.io.IOException

object PdfGenerator {
    fun generatePdf(context: Context, resume: Resume, fontSize: Float, fontColor: Color, bgColor: Color) {
        val pdfDocument = PdfDocument()
        val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create() // A4 size: 595x842 pixels
        val page = pdfDocument.startPage(pageInfo)
        val canvas = page.canvas

        val androidFontColor = fontColor.toArgb()
        val androidBgColor = bgColor.toArgb()

        val paintBg = Paint().apply {
            color = androidBgColor
            style = Paint.Style.FILL
        }
        canvas.drawRect(0f, 0f, pageInfo.pageWidth.toFloat(), pageInfo.pageHeight.toFloat(), paintBg)

        val paintText = Paint().apply {
            color = androidFontColor
            textSize = fontSize * 2
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        }

        val margin = 50f
        val maxWidth = pageInfo.pageWidth - (2 * margin) // Prevent text from going off-page
        var yPosition = 50f

        fun drawWrappedText(text: String, x: Float, y: Float, paint: Paint): Float {
            val words = text.split(" ")
            val lineBuilder = StringBuilder()
            var currentY = y

            for (word in words) {
                val testLine = if (lineBuilder.isEmpty()) word else "${lineBuilder} $word"
                if (paint.measureText(testLine) > maxWidth) {
                    canvas.drawText(lineBuilder.toString(), x, currentY, paint)
                    currentY += fontSize * 2
                    lineBuilder.clear()
                    lineBuilder.append(word)
                } else {
                    lineBuilder.append(" $word")
                }
            }
            canvas.drawText(lineBuilder.toString(), x, currentY, paint)
            return currentY + fontSize * 2 // Return new y-position
        }

        yPosition = drawWrappedText("Name: ${resume.name}", margin, yPosition, paintText)
        yPosition = drawWrappedText("Contact: ${resume.contactInfo}", margin, yPosition, paintText)
        yPosition = drawWrappedText("Skills: ${resume.skills.joinToString(", ")}", margin, yPosition, paintText)
        yPosition = drawWrappedText("Work Experience:", margin, yPosition, paintText)

        resume.workExperience.forEach { experience ->
            yPosition = drawWrappedText(experience, margin + 20, yPosition, paintText)
        }

        pdfDocument.finishPage(page)

        savePdfToDownloads(context, pdfDocument)

        pdfDocument.close()
    }

    private fun savePdfToDownloads(context: Context, pdfDocument: PdfDocument) {
        val fileName = "Resume_${System.currentTimeMillis()}.pdf"

        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
            put(MediaStore.MediaColumns.MIME_TYPE, "application/pdf")
            put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS)
        }

        val contentResolver = context.contentResolver
        val uri = contentResolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, contentValues)

        if (uri != null) {
            try {
                contentResolver.openOutputStream(uri).use { outputStream ->
                    if (outputStream != null) {
                        pdfDocument.writeTo(outputStream)
                        Toast.makeText(context, "PDF saved in Downloads: $fileName", Toast.LENGTH_LONG).show()
                        Log.d("PdfGenerator", "PDF successfully saved at: $fileName")
                    }
                }
            } catch (e: IOException) {
                Log.e("PdfGenerator", "Error saving PDF: ${e.message}")
                Toast.makeText(context, "Failed to save PDF", Toast.LENGTH_LONG).show()
            }
        } else {
            Toast.makeText(context, "Failed to create file", Toast.LENGTH_LONG).show()
            Log.e("PdfGenerator", "Failed to create MediaStore entry")
        }
    }
}
